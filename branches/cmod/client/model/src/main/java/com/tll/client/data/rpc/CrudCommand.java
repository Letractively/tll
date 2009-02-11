/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.data.rpc.ICrudService;
import com.tll.common.data.rpc.ICrudServiceAsync;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;
import com.tll.common.search.ISearch;

/**
 * CrudCommand - Issues CRUD commands to the server.
 * @author jpk
 */
public final class CrudCommand extends RpcCommand<EntityPayload> implements ISourcesCrudEvents {

	public enum CrudOp {
		FETCH_PROTOTYPE,
		LOAD,
		ADD,
		UPDATE,
		PURGE;
	}

	private static final ICrudServiceAsync svc;
	static {
		svc = (ICrudServiceAsync) GWT.create(ICrudService.class);
	}

	private final CrudListenerCollection crudListeners = new CrudListenerCollection();

	private final Widget sourcingWidget;
	private CrudOp crudOp;
	private EntityRequest entityRequest;

	/**
	 * Constructor
	 * @param sourcingWidget
	 */
	public CrudCommand(Widget sourcingWidget) {
		super();
		this.sourcingWidget = sourcingWidget;
	}

	@Override
	protected Widget getSourcingWidget() {
		return sourcingWidget;
	}

	/**
	 * Sets the state of this command for receiving an empty entity.
	 * @param entityType
	 * @param generate
	 */
	public final void fetchPrototype(IEntityType entityType, boolean generate) {
		if(entityType == null) {
			throw new IllegalArgumentException("An entity type must be specified.");
		}
		entityRequest = new EntityPrototypeRequest(entityType, generate);
		crudOp = CrudOp.FETCH_PROTOTYPE;
	}

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param entityRef
	 */
	public final void load(RefKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalArgumentException("A set entity reference must be specified.");
		}
		entityRequest = new EntityLoadRequest(entityRef);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for loading a named entity by name.
	 * @param entityType The entity type
	 * @param name The name of the named entity
	 */
	public final void loadByName(IEntityType entityType, String name) {
		if(entityType == null || name == null) {
			throw new IllegalArgumentException("An entity type and name must be specified.");
		}
		entityRequest = new EntityLoadRequest(entityType, name);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for loading an entity by business key.
	 * @param search The search holding the business key properties
	 */
	public final void loadByBusinessKey(ISearch search) {
		if(search == null) {
			throw new IllegalArgumentException("A business key search must be specified.");
		}
		entityRequest = new EntityLoadRequest(search);
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
			throw new IllegalArgumentException("A non-null and non-new entity must be specified.");
		}
		entityRequest = new EntityPersistRequest(entity);
		crudOp = CrudOp.UPDATE;
	}

	/**
	 * Sets the state of this command for purging an entity.
	 * @param entityRef The ref of the entity to be purged.
	 */
	public final void purge(RefKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalArgumentException("A set entity reference must be specified.");
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
			case FETCH_PROTOTYPE:
				svc.getEmptyEntity((EntityPrototypeRequest) entityRequest, getAsyncCallback());
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
