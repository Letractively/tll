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
import com.tll.client.event.ModelChangeEventDispatcher;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.util.StringUtil;

/**
 * CrudCommand - Issues CRUD commands to the server.
 * @author jpk
 */
public class CrudCommand extends RpcCommand<EntityPayload> implements ISourcesCrudEvents {

	public static final int CRUD_OP_RECIEVE_EMPTY_ENTITY = 0;
	public static final int CRUD_OP_LOAD = 1;
	public static final int CRUD_OP_ADD = 2;
	public static final int CRUD_OP_UPDATE = 3;
	public static final int CRUD_OP_PURGE = 4;

	private static final ICrudServiceAsync svc;
	static {
		svc = (ICrudServiceAsync) GWT.create(ICrudService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/crud");
	}

	private final CrudListenerCollection crudListeners = new CrudListenerCollection();

	private int crudOp = -1;
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
	public final void receiveEmpty(String entityType, boolean generate) {
		if(StringUtil.isEmpty(entityType)) {
			throw new IllegalStateException("An entity type must be specified.");
		}
		entityRequest = new EntityGetEmptyRequest(entityType, generate);
		crudOp = CRUD_OP_RECIEVE_EMPTY_ENTITY;
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
		crudOp = CRUD_OP_LOAD;
	}

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param entityType The entity type
	 */
	public final void loadByName(String entityType, String name) {
		if(entityType == null || name == null) {
			throw new IllegalStateException("An entity type and name must be specified.");
		}
		entityRequest = new EntityLoadRequest(entityType, name);
		crudOp = CRUD_OP_LOAD;
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
		crudOp = CRUD_OP_ADD;
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
		crudOp = CRUD_OP_UPDATE;
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
		crudOp = CRUD_OP_PURGE;
	}

	public final void addCrudListener(ICrudListener listener) {
		crudListeners.add(listener);
	}

	public final void removeCrudListener(ICrudListener listener) {
		crudListeners.remove(listener);
	}

	private void clear() {
		crudOp = -1;
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
			case CRUD_OP_RECIEVE_EMPTY_ENTITY:
				svc.getEmptyEntity((EntityGetEmptyRequest) entityRequest, getAsyncCallback());
				break;

			case CRUD_OP_LOAD:
				if(entityRequest.auxDataRequest != null) {
					entityRequest.auxDataRequest = AuxDataCache.instance().filterRequest(entityRequest.auxDataRequest);
				}
				svc.load((EntityLoadRequest) entityRequest, getAsyncCallback());
				break;

			case CRUD_OP_ADD:
			case CRUD_OP_UPDATE:
				svc.persist((EntityPersistRequest) entityRequest, getAsyncCallback());
				break;

			case CRUD_OP_PURGE:
				svc.purge((EntityPurgeRequest) entityRequest, getAsyncCallback());
				break;

			default:
				throw new IllegalStateException("Unknown crud op: " + crudOp);
		}
	}

	@Override
	protected void handleSuccess(EntityPayload result) {
		super.handleSuccess(result);
		AuxDataCache.instance().cache(result);
		crudListeners.fireCrudEvent(new CrudEvent(sourcingWidget, crudOp, entityRequest, result));

		// fire app wide model change event if applicable
		if(!result.hasErrors()) {
			switch(crudOp) {
				case CRUD_OP_ADD: {
					ModelChangeEventDispatcher.instance().fireOnModelChange(
							new ModelChangeEvent(sourcingWidget, ModelChangeOp.ADD, result.getEntity()));
					break;
				}
				case CRUD_OP_UPDATE: {
					ModelChangeEventDispatcher.instance().fireOnModelChange(
							new ModelChangeEvent(sourcingWidget, ModelChangeOp.UPDATE, result.getEntity()));
					break;
				}
				case CRUD_OP_PURGE: {
					ModelChangeEventDispatcher.instance().fireOnModelChange(
							new ModelChangeEvent(sourcingWidget, ModelChangeOp.DELETE, result.getEntityRef()));
					break;
				}
			}
		}

		clear();
	}
}
