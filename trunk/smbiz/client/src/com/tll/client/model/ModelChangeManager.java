package com.tll.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.AuxDataRequest.AuxDataType;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.CrudEvent;
import com.tll.client.data.rpc.ICrudListener;
import com.tll.client.data.rpc.IRpcListener;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
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

	private static ModelChangeManager instance = new ModelChangeManager();

	public static ModelChangeManager instance() {
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
	 * @param sourcingWidget
	 * @param adr
	 * @return <code>true</code> only if aux data is actually needed and
	 *         <code>false</code> when no aux data is needed. I.e.: it is already
	 *         cached on the client.
	 */
	public boolean fetchAuxData(Widget sourcingWidget, AuxDataRequest adr) {
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
	 * @param sourcingWidget
	 * @param entityType The entity type of the model entity to fetch
	 * @return <code>true</code> if the model prototype must be fetched from the
	 *         server and <code>false</code> when the prototype is already cached
	 *         on the client.
	 */
	public boolean fetchModelPrototype(Widget sourcingWidget, EntityType entityType) {
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
	 * @param sourcingWidget
	 * @param modelRef The reference of the model to fetch
	 * @param entityOptions
	 * @param adr
	 */
	public void loadModel(Widget sourcingWidget, RefKey modelRef, EntityOptions entityOptions, AuxDataRequest adr) {
		CrudCommand cmd = createCrudCommand(sourcingWidget);
		cmd.load(modelRef);
		cmd.setEntityOptions(entityOptions);
		cmd.setAuxDataRequest(AuxDataCache.instance().filterRequest(adr));
		cmd.execute();
	}

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event. A subsequent model change event is anticipated.
	 * @param sourcingWidget
	 * @param model The model to persist
	 * @param entityOptions
	 */
	public void persistModel(Widget sourcingWidget, Model model, EntityOptions entityOptions) {
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
	 * @param sourcingWidget
	 * @param modelRef The model to delete
	 * @param entityOptions
	 */
	public void deleteModel(Widget sourcingWidget, RefKey modelRef, EntityOptions entityOptions) {
		CrudCommand cmd = createCrudCommand(sourcingWidget);
		cmd.purge(modelRef);
		cmd.setEntityOptions(entityOptions);
		cmd.execute();
	}

	public void onRpcEvent(RpcEvent event) {
		if(!event.getPayload().hasErrors()) {
			ModelChangeEvent mce = new ModelChangeEvent(event.getSource(), ModelChangeOp.AUXDATA_READY, (Model) null, null);
			listeners.fireOnModelChange(mce);
		}
	}

	public void onCrudEvent(CrudEvent event) {

		ModelChangeEvent mce = null;

		switch(event.getCrudOp()) {

			case LOAD:
				mce =
						new ModelChangeEvent(event.getSource(), ModelChangeOp.LOADED, event.getPayload().getEntity(), event
								.getPayload().getStatus());
				break;

			case ADD:
				mce =
						new ModelChangeEvent(event.getSource(), ModelChangeOp.ADDED, event.getPayload().getEntity(), event
								.getPayload().getStatus());
				break;

			case UPDATE:
				mce =
						new ModelChangeEvent(event.getSource(), ModelChangeOp.UPDATED, event.getPayload().getEntity(), event
								.getPayload().getStatus());
				break;

			case PURGE:
				mce =
						new ModelChangeEvent(event.getSource(), ModelChangeOp.DELETED, event.getPayload().getEntityRef(), event
								.getPayload().getStatus());
				break;
		}

		if(mce != null) {
			listeners.fireOnModelChange(mce);
		}
	}
}
