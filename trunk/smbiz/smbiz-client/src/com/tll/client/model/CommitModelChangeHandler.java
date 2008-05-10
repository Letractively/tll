package com.tll.client.model;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.IRpcListener;
import com.tll.client.event.ISourcesModelChangeEvents;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.RpcEvent;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;
import com.tll.client.msg.Msg;

/**
 * CommitModelChangeHandler - Handles model changes via {@link CrudCommand}s.
 * @author jpk
 */
public abstract class CommitModelChangeHandler implements IRpcListener, ICrudListener, ISourcesModelChangeEvents, IModelChangeHandler {

	private final ModelChangeListenerCollection listeners = new ModelChangeListenerCollection();

	/**
	 * @return The necessary sourcing Widget.
	 */
	protected abstract Widget getSourcingWidget();

	/**
	 * @return The optional entity options used for fetching and persisting entity
	 *         models.
	 */
	protected abstract EntityOptions getEntityOptions();

	/**
	 * @return The needed aux data. May be <code>null</code> if no aux data
	 *         needed.
	 */
	protected abstract AuxDataRequest getNeededAuxData();

	public void addModelChangeListener(IModelChangeListener listener) {
		listeners.add(listener);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		listeners.remove(listener);
	}

	private CrudCommand createCrudCommand() {
		CrudCommand cmd = new CrudCommand(getSourcingWidget());
		cmd.addCrudListener(this);
		return cmd;
	}

	public boolean handleAuxDataFetch() {
		// do we need any aux data from the server?
		AuxDataRequest adr = getNeededAuxData();
		adr = adr == null ? null : AuxDataCache.instance().filterRequest(adr);
		if(adr == null) return false;
		final AuxDataCommand adc = new AuxDataCommand(getSourcingWidget(), adr);
		adc.addRpcListener(this);
		adc.execute();
		return true;
	}

	public void handleModelFetch(RefKey modelRef) {
		CrudCommand cmd = createCrudCommand();
		cmd.load(modelRef);
		cmd.setEntityOptions(getEntityOptions());

		// request needed aux data
		AuxDataRequest adr = getNeededAuxData();
		if(adr.size() > 0) cmd.setAuxDataRequest(adr);

		cmd.execute();
	}

	public void handleModelAdd(Model model) {
		CrudCommand cmd = createCrudCommand();
		cmd.add(model);
		cmd.setEntityOptions(getEntityOptions());
		cmd.execute();
	}

	public void handleModelUpdate(Model model) {
		CrudCommand cmd = createCrudCommand();
		cmd.update(model);
		cmd.setEntityOptions(getEntityOptions());
		cmd.execute();
	}

	public void handleModelDelete(RefKey modelRef) {
		CrudCommand cmd = createCrudCommand();
		cmd.purge(modelRef);
		cmd.setEntityOptions(getEntityOptions());
		cmd.execute();
	}

	public void onRpcEvent(RpcEvent event) {
		if(!event.getPayload().hasErrors()) {
			ModelChangeEvent mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.AUXDATA_READY, (Model) null, null);
			listeners.fireOnModelChange(mce);
		}
	}

	public void onCrudEvent(CrudEvent event) {

		final List<Msg> errors = event.getPayload().getStatus().getFieldMsgs();

		ModelChangeEvent mce = null;

		switch(event.getCrudOp()) {

			case LOAD:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.LOADED, event.getPayload().getEntity(), errors);
				break;

			case ADD:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.ADDED, event.getPayload().getEntity(), errors);
				break;

			case UPDATE:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.UPDATED, event.getPayload().getEntity(), errors);
				break;

			case PURGE:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.DELETED, event.getPayload().getEntityRef(), errors);
				break;
		}

		if(mce != null) {
			listeners.fireOnModelChange(mce);
		}
	}
}
