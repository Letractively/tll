package com.tll.client.model;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.AuxDataRequest.AuxDataType;
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
import com.tll.model.EntityType;

/**
 * ModelChangeManager - Acts as a mediator centralizing app-wide handling of
 * model change events ensuring the model changes are fully disseminated
 * throughout the app thus keeping all open views in sync with the underlying
 * model.
 * <p>
 * Consequently, <em>all</em> model change (CRUD ops) should be directed here
 * and no "naked" {@link CrudCommand}s should exist in the app.
 * @author jpk
 */
public final class ModelChangeManager implements IRpcListener, ICrudListener, ISourcesModelChangeEvents {

	private static ModelChangeManager instance;

	public static ModelChangeManager instance() {
		if(instance == null) {
			instance = new ModelChangeManager();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private ModelChangeManager() {
	}

	private final ModelChangeListenerCollection listeners = new ModelChangeListenerCollection();

	public void addModelChangeListener(IModelChangeListener listener) {
		listeners.add(listener);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		listeners.remove(listener);
	}

	private CrudCommand createCrudCommand(Widget sourcingWidget) {
		CrudCommand cmd = new CrudCommand(sourcingWidget);
		cmd.addCrudListener(this);
		return cmd;
	}

	/**
	 * Handles the fetching of needed auxiliary data. A subsequent model change
	 * event is anticipated if aux data is found needed (<code>true</code> is
	 * returned).
	 * @return <code>true</code> only if aux data is actually needed and
	 *         <code>false</code> when no aux data is needed. I.e.: it is already
	 *         cached on the client.
	 */
	public boolean handleAuxDataFetch(Widget sourcingWidget, AuxDataRequest adr) {
		// do we need any aux data from the server?
		adr = AuxDataCache.instance().filterRequest(adr);
		if(adr == null) return false;
		final AuxDataCommand adc = new AuxDataCommand(sourcingWidget, adr);
		adc.addRpcListener(this);
		adc.execute();
		return true;
	}

	/**
	 * Handles the fetching of a prototypical model given an entity type. A
	 * subsequent model change event is anticipated. Used preliminarily for adding
	 * entities. A subsequent model change event is anticipated if aux data is
	 * found needed (<code>true</code> is returned).
	 * @param entityType The entity type of the model entity to fetch
	 * @return <code>true</code> if the model prototype must be fetched from the
	 *         server and <code>false</code> when the prototype is already cached
	 *         on the client.
	 */
	public boolean handleModelPrototypeFetch(Widget sourcingWidget, EntityType entityType) {
		if(!AuxDataCache.instance().isCached(AuxDataType.ENTITY_PROTOTYPE, entityType)) {
			AuxDataRequest adr = new AuxDataRequest();
			adr.requestEntityPrototype(entityType);
			final AuxDataCommand adc = new AuxDataCommand(sourcingWidget, adr);
			adc.addRpcListener(this);
			adc.execute();
			return true;
		}
		listeners.fireOnModelChange(new ModelChangeEvent(sourcingWidget, ModelChangeOp.AUXDATA_READY));
		return false;
	}

	/**
	 * Handles the fetching of a model given a model ref. A subsequent model
	 * change event is anticipated.
	 * @param modelRef The reference of the model to fetch
	 */
	public void handleModelLoad(Widget sourcingWidget, RefKey modelRef, EntityOptions entityOptions, AuxDataRequest adr) {
		CrudCommand cmd = createCrudCommand(sourcingWidget);
		cmd.load(modelRef);
		cmd.setEntityOptions(entityOptions);
		cmd.setAuxDataRequest(AuxDataCache.instance().filterRequest(adr));
		cmd.execute();
	}

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event. A subsequent model change event is anticipated.
	 * @param model The model to persist
	 */
	public void handleModelPersist(Widget sourcingWidget, Model model, EntityOptions entityOptions) {
		CrudCommand cmd = createCrudCommand(sourcingWidget);
		if(model.isNew()) {
			cmd.add(model);
		}
		else {
			cmd.update(model);
		}
		cmd.setEntityOptions(entityOptions);
		cmd.execute();
	}

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param modelRef The model to delete
	 */
	public void handleModelDelete(Widget sourcingWidget, RefKey modelRef, EntityOptions entityOptions) {
		CrudCommand cmd = createCrudCommand(sourcingWidget);
		cmd.purge(modelRef);
		cmd.setEntityOptions(entityOptions);
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
