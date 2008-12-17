/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.UnloadViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.model.Model;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldPanel;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractView implements IEditListener {

	/**
	 * The model reference used to subsequently fetch the actual model subject to
	 * editing if necessary.
	 */
	private RefKey modelRef;

	/**
	 * The model subject to editing.
	 */
	private Model model;

	/**
	 * The Panel containing the UI edit Widgets.
	 */
	private final EditPanel editPanel;

	/**
	 * Optional entity options when performing model change ops.
	 */
	private final EntityOptions entityOptions;

	/**
	 * Constructor
	 * @param fldGrpPnl The field group panel. May NOT be <code>null</code>.
	 * @param entityOptions
	 */
	public EditView(FieldPanel fldGrpPnl, final EntityOptions entityOptions) {
		super();

		editPanel = new EditPanel(fldGrpPnl, true, false);
		editPanel.addEditListener(this);

		this.entityOptions = entityOptions;

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
		model = r.getModel();
		if(model == null) {
			setModelRef(r.getModelRef());
		}
		else {
			setModelRef(model.getRefKey());
		}
	}

	public final void refresh() {
		if(model == null) {
			// we need to fetch the model first
			// NOTE: needed aux data will be fetched with this rpc call
			ModelChangeManager.instance().handleModelLoad(this, modelRef, entityOptions, getNeededAuxData());
		}
		else if(!ModelChangeManager.instance().handleAuxDataFetch(this, getNeededAuxData())) {
			editPanel.setEditMode(model.isNew());
			editPanel.bindModel(model);
			editPanel.getFields().draw();
		}
	}

	@Override
	protected void doDestroy() {
		// no-op
	}

	public final void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case CANCEL:
				ViewManager.instance().dispatch(new UnloadViewRequest(this, getViewKey(), false));
				break;
			case ADD:
			case UPDATE:
				if(editPanel.updateModel()) {
					ModelChangeManager.instance().handleModelPersist(this, model, entityOptions);
				}
				break;
			case DELETE:
				if(!model.isNew()) {
					ModelChangeManager.instance().handleModelDelete(this, modelRef, entityOptions);
				}
				break;
		}
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		if(event.isError()) {
			editPanel.applyErrorMsgs(event.getErrors());
			return;
		}
		switch(event.getChangeOp()) {

			case LOADED:
				model = event.getModel();
				// NOTE we fall through
			case AUXDATA_READY:
				refresh();
				// NOTE we bail as we don't need to dissemminate these model changes to
				// the other views
				return;

			case ADDED:
			case UPDATED:
				model = event.getModel();
				refresh();
				break;
			case DELETED:
				ViewManager.instance().dispatch(new UnloadViewRequest(this, getViewKey(), true));
				break;
		}
	}
}
