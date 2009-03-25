/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractModelAwareView<EditViewRequest> implements IEditHandler {

	/**
	 * The model reference used to subsequently fetch the actual model subject to
	 * editing if necessary.
	 */
	private ModelKey modelRef;

	/**
	 * The model subject to editing.
	 */
	private Model model;

	/**
	 * Optional entity options when performing model change ops.
	 */
	private final EntityOptions entityOptions;

	/**
	 * The Panel containing the UI edit Widgets.
	 */
	private final EditPanel editPanel;

	/**
	 * Constructor
	 * @param fieldPanel The required field panel
	 * @param entityOptions Optional entity options
	 */
	public EditView(FieldPanel<?> fieldPanel, final EntityOptions entityOptions) {
		super();

		// TODO add global msg panel ref
		editPanel = new EditPanel(null, fieldPanel, true, false);
		editPanel.addEditHandler(this);

		this.entityOptions = entityOptions;

		addWidget(editPanel);
	}

	/**
	 * @return All possible aux data that may be accessed throughout the life of
	 *         the edit panel.
	 */
	protected abstract AuxDataRequest getNeededAuxData();

	public String getLongViewName() {
		String s = modelRef.getEntityType().getPresentationName();
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
			s = modelRef.getEntityType().getPresentationName();
		}
		return "Edit " + s;
	}

	private void setModelRef(ModelKey modelRef) {
		if(modelRef == null || !modelRef.isSet()) {
			throw new IllegalArgumentException("Invalid model ref specified");
		}
		this.modelRef = modelRef;
	}

	@Override
	protected final void doInitialization(EditViewRequest viewRequest) {
		model = viewRequest.getModel();
		if(model == null) {
			setModelRef(viewRequest.getModelKey());
		}
		else {
			setModelRef(model.getRefKey());
		}
	}

	public final void refresh() {
		model = null;
		doRefresh();
	}

	private void doRefresh() {
		if(model == null) {
			// we need to fetch the model first
			// NOTE: needed aux data will be fetched with this rpc call
			ModelChangeManager.get().loadModel(this, modelRef, entityOptions, getNeededAuxData());
		}
		else if(!ModelChangeManager.get().fetchAuxData(this, getNeededAuxData())) {
			editPanel.setModel(model);
		}
	}

	@Override
	protected void doDestroy() {
		// no-op
		editPanel.setModel(null); // forces clean-up of bindings and listeners
	}

	public final void onEdit(EditEvent event) {
		switch(event.getOp()) {
			case CANCEL:
				ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), false));
				break;
			case ADD:
			case UPDATE:
				ModelChangeManager.get().persistModel(this, model, entityOptions);
				break;
			case DELETE:
				if(!model.isNew()) {
					ModelChangeManager.get().deleteModel(this, modelRef, entityOptions);
				}
				break;
		}
	}

	@Override
	protected final boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		if((event.getSource() == this) || (event.getModelRef() != null && event.getModelRef().equals(modelRef))) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleModelChangeError(ModelChangeEvent event) {
		editPanel.applyFieldErrors(event.getStatus().getFieldMsgs());
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		switch(event.getChangeOp()) {

			case LOADED:
				model = event.getModel();
				// NOTE we fall through
			case AUXDATA_READY:
				doRefresh();
				// NOTE we bail as we don't need to dissemminate these model changes to
				// the other views
				return;

			case ADDED:
			case UPDATED:
				doRefresh();
				break;

			case DELETED:
				ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), true));
				break;
		}
	}
}
