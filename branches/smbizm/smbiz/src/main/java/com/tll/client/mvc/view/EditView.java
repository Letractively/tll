/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.tll.client.model.IHasModelChangeHandlers;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractRpcAndModelAwareView<EditViewInitializer> implements IEditHandler, IHasModelChangeHandlers {

	/**
	 * The model reference used to subsequently fetch the actual model subject to
	 * editing if necessary.
	 */
	private ModelKey modelKey;

	/**
	 * The model subject to editing.
	 */
	private Model model;

	/**
	 * Optional entity options when performing model change ops.
	 */
	private final EntityOptions entityOptions;

	/**
	 * The dedicated global msg display for this view.
	 */
	private final GlobalMsgPanel gmp;

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
		gmp = new GlobalMsgPanel();
		editPanel = new EditPanel(gmp, fieldPanel, true, false);
		editPanel.addEditHandler(this);

		this.entityOptions = entityOptions;

		addWidget(gmp);
		addWidget(editPanel);

		// edit views shall notify other open views of model changes
		addModelChangeHandler(ViewManager.get());
	}

	/**
	 * @return All possible aux data that may be accessed throughout the life of
	 *         the edit panel.
	 */
	protected abstract AuxDataRequest getNeededAuxData();

	public String getLongViewName() {
		String s = modelKey.getEntityType().getPresentationName();
		if(modelKey.getName() != null) {
			s += " " + modelKey.getName();
		}
		return "Edit " + s;
	}

	@Override
	public String getShortViewName() {
		String s = modelKey.getName();
		if(s == null) {
			// fallback to the entity type
			s = modelKey.getEntityType().getPresentationName();
		}
		return "Edit " + s;
	}

	private void setModelRef(ModelKey modelRef) {
		if(modelRef == null || !modelRef.isSet()) {
			throw new IllegalArgumentException("Invalid model ref specified");
		}
		this.modelKey = modelRef;
	}

	@Override
	protected final void doInitialization(EditViewInitializer initializer) {
		model = initializer.getModel();
		if(model == null) {
			setModelRef(initializer.getModelKey());
		}
		else {
			setModelRef(model.getKey());
		}
	}

	@Override
	protected void doRefresh() {
		model = null;
		reload();
	}

	private void reload() {
		Command cmd = null;
		if(model == null) {
			// we need to fetch the model first
			// NOTE: needed aux data will be fetched with this rpc call
			cmd = ModelChangeManager.loadModel(this, modelKey, entityOptions, getNeededAuxData());
		}
		else {
			cmd = ModelChangeManager.fetchAuxData(this, getNeededAuxData());
			if(cmd == null) {
				editPanel.setModel(model);
				return;
			}
		}
		if(cmd != null) {
			cmd.execute();
		}
	}

	@Override
	protected void doDestroy() {
		editPanel.setModel(null); // forces clean-up of bindings and listeners
	}

	public final void onEdit(EditEvent event) {
		Command cmd = null;
		switch(event.getOp()) {
			case CANCEL:
				ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), false, false));
				break;
			case ADD:
			case UPDATE:
				cmd = ModelChangeManager.persistModel(this, model, entityOptions);
				break;
			case DELETE:
				if(!model.isNew()) {
					cmd = ModelChangeManager.deleteModel(this, modelKey, entityOptions);
				}
				break;
		}
		if(cmd != null) {
			// addRpcHandler(new RpcUiHandler(getViewWidget()));
			cmd.execute();
		}
	}

	@Override
	protected final boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		final ModelKey mkey = event.getModelKey();
		if((event.getSource() == this) || (mkey != null && mkey.equals(modelKey))) {
			return true;
		}
		return false;
	}

	@Override
	public HandlerRegistration addModelChangeHandler(IModelChangeHandler handler) {
		return addHandler(handler, ModelChangeEvent.TYPE);
	}

	@Override
	protected void handleModelChangeError(ModelChangeEvent event) {
		gmp.clear();
		editPanel.applyFieldErrors(event.getStatus().getMsgs(MsgAttr.FIELD.flag));
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		gmp.clear();
		switch(event.getChangeOp()) {

			case LOADED:
				model = event.getModel();
				// NOTE we fall through
			case AUXDATA_READY:
				reload();
				return;

			case ADDED:
				model = event.getModel();
				gmp.add(new Msg(model.descriptor() + " added", MsgLevel.INFO), null);
				editPanel.setModel(model);
				break;

			case UPDATED:
				model = event.getModel();
				gmp.add(new Msg(model.descriptor() + " updated", MsgLevel.INFO), null);
				editPanel.setModel(model);
				break;

			case DELETED:
				// we need to defer so as not to interfere with ViewManager's
				// onModelChangeEvent iteration
				// otherwise we get a ConcurrentModificationException
				DeferredCommand.addCommand(new Command() {

					@Override
					public void execute() {
						ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), true, true));
					}
				});
				break;
		}
	}
}
