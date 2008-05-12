/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityGetEmptyRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;
import com.tll.client.data.EntityRequest;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.ISourcesCrudEvents;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.model.EntityType;

/**
 * CrudCommand - Issues CRUD commands to the server.
 * @author jpk
 */
public class CrudCommand extends RpcCommand<EntityPayload> implements ISourcesCrudEvents {

	public enum CrudOp {
		// TODO change to LOAD_NEW ???
		RECIEVE_EMPTY_ENTITY,
		LOAD,
		ADD,
		UPDATE,
		PURGE;
	}

	private static final ICrudServiceAsync svc;
	static {
		svc = (ICrudServiceAsync) GWT.create(ICrudService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/crud");
	}

	private final CrudListenerCollection crudListeners = new CrudListenerCollection();

	private CrudOp crudOp;
	private EntityRequest entityRequest;

	/**
	 * Constructor
	 * @param sourcingWidget
	 */
	public CrudCommand(Widget sourcingWidget) {
		super(sourcingWidget);
	}

	/**
	 * Sets the state of this command for receiving an empty entity.
	 * @param entityType
	 * @param generate
	 */
	public final void receiveEmpty(EntityType entityType, boolean generate) {
		if(entityType == null) {
			throw new IllegalStateException("An entity type must be specified.");
		}
		entityRequest = new EntityGetEmptyRequest(entityType, generate);
		crudOp = CrudOp.RECIEVE_EMPTY_ENTITY;
	}

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param entityRef
	 */
	public final void load(RefKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalStateException("A set entity reference must be specified.");
		}
		entityRequest = new EntityLoadRequest(entityRef);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param entityType The entity type
	 */
	public final void loadByName(EntityType entityType, String name) {
		if(entityType == null || name == null) {
			throw new IllegalStateException("An entity type and name must be specified.");
		}
		entityRequest = new EntityLoadRequest(entityType, name);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for adding an entity.
	 * @param entity The entity to add.
	 */
	public final void add(Model entity) {
		if(entity == null || entity.isNew()) {
			throw new IllegalArgumentException("A non-null and new entity must be specified.");
		}
		entityRequest = new EntityPersistRequest(entity);
		crudOp = CrudOp.ADD;
	}

	/**
	 * Sets the state of this command for updating an entity.
	 * @param entity The entity to update.
	 */
	public final void update(Model entity) {
		if(entity == null || entity.isNew()) {
			throw new IllegalStateException("A non-null and non-new entity must be specified.");
		}
		entityRequest = new EntityPersistRequest(entity);
		crudOp = CrudOp.UPDATE;
	}

	/**
	 * Sets the state of this command for persisting the given entity.
	 * @param entity
	 */
	public final void persist(Model entity) {
		if(entity == null) {
			throw new IllegalArgumentException("A non-null entity must be specified.");
		}
		if(entity.isNew())
			add(entity);
		else
			update(entity);
	}

	/**
	 * Sets the state of this command for purging an entity.
	 * @param entityRef The ref of the entity to be purged.
	 */
	public final void purge(RefKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalStateException("A set entity reference must be specified.");
		}
		entityRequest = new EntityPurgeRequest(entityRef);
		crudOp = CrudOp.PURGE;
	}

	public final void addCrudListener(ICrudListener listener) {
		crudListeners.add(listener);
	}

	public final void removeCrudListener(ICrudListener listener) {
		crudListeners.remove(listener);
	}

	private void clear() {
		crudOp = null;
		entityRequest = null;
	}

	public final void setEntityOptions(EntityOptions options) {
		if(entityRequest == null) {
			throw new IllegalStateException("No entity request specified");
		}
		entityRequest.entityOptions = options;
	}

	public final void setAuxDataRequest(AuxDataRequest auxDataRequest) {
		if(entityRequest == null) {
			throw new IllegalStateException("No entity request specified");
		}
		entityRequest.auxDataRequest = auxDataRequest;
	}

	@Override
	protected void doExecute() {
		switch(crudOp) {
			case RECIEVE_EMPTY_ENTITY:
				svc.getEmptyEntity((EntityGetEmptyRequest) entityRequest, getAsyncCallback());
				break;

			case LOAD:
				svc.load((EntityLoadRequest) entityRequest, getAsyncCallback());
				break;

			case ADD:
			case UPDATE:
				svc.persist((EntityPersistRequest) entityRequest, getAsyncCallback());
				break;

			case PURGE:
				svc.purge((EntityPurgeRequest) entityRequest, getAsyncCallback());
				break;

			default:
				throw new IllegalStateException("Unknown crud op: " + crudOp);
		}
	}

	@Override
	protected void handleSuccess(EntityPayload result) {
		super.handleSuccess(result);

		// cache aux data
		AuxDataCache.instance().cache(result);

		crudListeners.fireCrudEvent(new CrudEvent(sourcingWidget, crudOp, entityRequest, result));

		clear();
	}
}
