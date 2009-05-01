/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.data.rpc.IRpcCommand;
import com.tll.client.data.rpc.ModelChangeManager;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.RpcUiHandler;
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
import com.tll.common.msg.Msg.MsgLevel;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractModelAwareView<EditViewInitializer> implements IEditHandler {

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
	 * The Panel containing the UI edit Widgets.
	 */
	private final EditPanel editPanel;

	private final GlobalMsgPanel gmp;

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
	protected final void doInitialization(EditViewInitializer viewRequest) {
		model = viewRequest.getModel();
		if(model == null) {
			setModelRef(viewRequest.getModelKey());
		}
		else {
			setModelRef(model.getKey());
		}
	}

	@Override
	public final void refresh() {
		model = null;
		doRefresh();
	}

	private void doRefresh() {
		if(model == null) {
			// we need to fetch the model first
			// NOTE: needed aux data will be fetched with this rpc call
			final IRpcCommand cmd = ModelChangeManager.get().loadModel(modelKey, entityOptions, getNeededAuxData());
			cmd.addRpcHandler(new RpcUiHandler(ViewManager.get().getParentViewPanel()));
			cmd.execute();
		}
		else {
			final IRpcCommand cmd = ModelChangeManager.get().fetchAuxData(getNeededAuxData());
			if(cmd == null) {
				editPanel.setModel(model);
			}
			else {
				cmd.addRpcHandler(new RpcUiHandler(getViewWidget()));
				cmd.execute();
			}
		}
	}

	@Override
	protected void doDestroy() {
		editPanel.setModel(null); // forces clean-up of bindings and listeners
	}

	public final void onEdit(EditEvent event) {
		IRpcCommand cmd = null;
		switch(event.getOp()) {
			case CANCEL:
				ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), false, false));
				break;
			case ADD:
			case UPDATE:
				cmd = ModelChangeManager.get().persistModel(model, entityOptions);
				break;
			case DELETE:
				if(!model.isNew()) {
					cmd = ModelChangeManager.get().deleteModel(modelKey, entityOptions);
				}
				break;
		}
		if(cmd != null) {
			cmd.addRpcHandler(new RpcUiHandler(getViewWidget()));
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
	protected void handleModelChangeError(ModelChangeEvent event) {
		gmp.clear();
		editPanel.applyFieldErrors(event.getStatus().getFieldMsgs());
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		gmp.clear();
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
				model = event.getModel();
				gmp.add(new Msg(model.descriptor() + " added", MsgLevel.INFO));
				editPanel.setModel(model);
				break;

			case UPDATED:
				model = event.getModel();
				gmp.add(new Msg(model.descriptor() + " updated", MsgLevel.INFO));
				editPanel.setModel(model);
				break;

			case DELETED:
				ViewManager.get().dispatch(new UnloadViewRequest(getViewKey(), true, true));
				break;
		}
	}
}
