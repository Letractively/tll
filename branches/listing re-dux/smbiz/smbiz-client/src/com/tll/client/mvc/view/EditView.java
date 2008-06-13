/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.UnloadViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.model.AbstractModelChangeHandler;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.Dispatcher;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldGroupPanel;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractView implements IEditListener {

	/**
	 * The Panel containing the UI edit Widgets.
	 */
	private final EditPanel editPanel;

	/**
	 * Handles model change events
	 */
	private final IModelChangeHandler modelChangeHandler;

	private boolean modelChangeHandled;

	/**
	 * The unique model reference.
	 */
	private RefKey modelRef;

	/**
	 * Constructor
	 * @param fldGrpPnl The field group panel. May NOT be <code>null</code>.
	 * @param entityOptions
	 */
	public EditView(FieldGroupPanel fldGrpPnl, final EntityOptions entityOptions) {
		super();

		editPanel = new EditPanel(fldGrpPnl, true, false);
		editPanel.addEditListener(this);

		modelChangeHandler = new AbstractModelChangeHandler() {

			@Override
			protected Widget getSourcingWidget() {
				return EditView.this;
			}

			@Override
			protected AuxDataRequest getNeededAuxData() {
				return EditView.this.getNeededAuxData();
			}

			@Override
			protected EntityOptions getEntityOptions() {
				return entityOptions;
			}
		};

		modelChangeHandler.addModelChangeListener(this);

		addWidget(editPanel);
	}

	protected abstract AuxDataRequest getNeededAuxData();

	public String getLongViewName() {
		String s = modelRef.getType().getName();
		if(modelRef.getName() != null) {
			s += " " + modelRef.getName();
		}
		return "Edit " + s;
	}

	@Override
	public String getShortViewName() {
		String s = modelRef.getName();
		if(s == null) {
			// fallback to the entity type
			s = modelRef.getType().getName();
		}
		return "Edit " + s;
	}

	@Override
	public final ShowViewRequest newViewRequest() {
		return new EditViewRequest(this, getViewClass(), modelRef);
	}

	private void setModelRef(RefKey modelRef) {
		if(modelRef == null || !modelRef.isSet()) {
			throw new IllegalArgumentException("Invalid model ref specified");
		}
		this.modelRef = modelRef;
	}

	@Override
	protected final void doInitialization(ViewRequestEvent viewRequest) {
		assert viewRequest instanceof EditViewRequest;
		EditViewRequest r = (EditViewRequest) viewRequest;
		Model model = r.getModel();
		if(model == null) {
			setModelRef(r.getModelRef());
		}
		else {
			setModelRef(model.getRefKey());
			editPanel.setModel(model);
		}
	}

	public final void refresh() {
		if(!editPanel.isModelLoaded()) {
			// we need to fetch the model first
			// NOTE: needed aux data will be fetched with this rpc call
			modelChangeHandler.handleModelLoad(modelRef);
		}
		else if(!modelChangeHandler.handleAuxDataFetch()) {
			editPanel.refresh();
		}
	}

	@Override
	protected void doDestroy() {
		// no-op
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		if(modelChangeHandled) {
			modelChangeHandled = false; // reset
			return;
		}
		if(event.isError()) {
			editPanel.applyMsgs(event.getErrors());
			return;
		}
		switch(event.getChangeOp()) {

			case LOADED:
				editPanel.setModel(event.getModel());
				// NOTE we fall through
			case AUXDATA_READY:
				refresh();
				// NOTE we bail as we don't need to dissemminate these model changes to
				// the other views
				return;

			case ADDED:
			case UPDATED:
				editPanel.setModel(event.getModel());
				refresh();
				break;
			case DELETED:
				Dispatcher.instance().dispatch(new UnloadViewRequest(EditView.this, getViewKey(), true));
				break;
		}

		// dispatch to other loaded view flagging this view as already handled this
		// event
		modelChangeHandled = true;
		ViewManager.instance().onModelChangeEvent(event);
	}

	public final void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case CANCEL:
				Dispatcher.instance().dispatch(new UnloadViewRequest(EditView.this, getViewKey(), false));
				break;
			case SAVE:
				modelChangeHandler.handleModelPersist(event.getModel());
				break;
		}
	}

}
