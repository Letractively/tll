/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.ModelRequest;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.rpc.ICrudService;
import com.tll.common.data.rpc.ICrudServiceAsync;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.search.ISearch;

/**
 * CrudCommand - Issues CRUD commands to the server.
 * <p>
 * Fires {@link ModelChangeEvent}s on the source widget if non-<code>null</code>.
 * @author jpk
 */
public class CrudCommand extends RpcCommand<ModelPayload> {

	public enum CrudOp {
		LOAD,
		ADD,
		UPDATE,
		PURGE;
	}

	/**
	 * Handles the fetching of a model given a model ref. A subsequent model
	 * change event is anticipated.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param key The key identifying the model
	 * @param entityOptions
	 * @param adr
	 * @return the rpc command ready for execution
	 */
	public static Command loadModel(Widget source, ModelKey key, EntityOptions entityOptions, AuxDataRequest adr) {
		final CrudCommand cmd = new CrudCommand();
		cmd.setSource(source);
		cmd.loadByPrimaryKey(key, entityOptions, AuxDataCacheHelper.filterRequest(adr));
		return cmd;
	}

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event. A subsequent model change event is anticipated.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param model The model to persist
	 * @param entityOptions
	 * @return the rpc command ready for execution
	 */
	public static Command persistModel(Widget source, Model model, EntityOptions entityOptions) {
		final CrudCommand cmd = new CrudCommand();
		cmd.setSource(source);
		if(model.isNew()) {
			cmd.add(model, entityOptions);
		}
		else {
			cmd.update(model, entityOptions);
		}
		return cmd;
	}

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param key The key identifying the model
	 * @return the rpc command ready for execution
	 */
	public static Command deleteModel(Widget source, ModelKey key) {
		final CrudCommand cmd = new CrudCommand();
		cmd.setSource(source);
		cmd.purge(key);
		return cmd;
	}

	private static final ICrudServiceAsync svc;
	static {
		svc = (ICrudServiceAsync) GWT.create(ICrudService.class);
	}

	protected CrudOp crudOp;
	protected ModelRequest modelRequest;

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param entityRef
	 * @param eoptions optional entity options
	 * @param auxDataRequest optional aux data request
	 */
	public void loadByPrimaryKey(ModelKey entityRef, EntityOptions eoptions, AuxDataRequest auxDataRequest) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalArgumentException("A set entity reference must be specified.");
		}
		final EntityLoadRequest elr = new EntityLoadRequest(entityRef);
		elr.setAuxDataRequest(auxDataRequest);
		elr.setEntityOptions(eoptions);
		this.modelRequest = elr;
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for loading a named entity by name.
	 * @param entityType The entity type
	 * @param name The name of the named entity
	 */
	public void loadByName(IEntityType entityType, String name) {
		if(entityType == null || name == null) {
			throw new IllegalArgumentException("An entity type and name must be specified.");
		}
		modelRequest = new EntityLoadRequest(entityType, name);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for loading model data by the given search
	 * param.
	 * @param search a generic search instance
	 */
	public void loadBySearch(ISearch search) {
		if(search == null) {
			throw new IllegalArgumentException("Null search");
		}
		modelRequest = new EntityLoadRequest(search);
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for adding an entity.
	 * @param entity The entity to add.
	 * @param eoptions optional entity options
	 */
	public final void add(Model entity, EntityOptions eoptions) {
		if(entity == null || !entity.isNew()) {
			throw new IllegalArgumentException("A non-null and new entity must be specified.");
		}
		final PersistRequest pr = new PersistRequest(entity);
		pr.setEntityOptions(eoptions);
		modelRequest = pr;
		crudOp = CrudOp.ADD;
	}

	/**
	 * Sets the state of this command for updating an entity.
	 * @param entity The entity to update.
	 * @param eoptions optional entity options
	 */
	public final void update(Model entity, EntityOptions eoptions) {
		if(entity == null || entity.isNew()) {
			throw new IllegalArgumentException("A non-null and non-new entity must be specified.");
		}
		final PersistRequest pr = new PersistRequest(entity);
		pr.setEntityOptions(eoptions);
		modelRequest = pr;
		crudOp = CrudOp.UPDATE;
	}

	/**
	 * Sets the state of this command for purging an entity.
	 * @param entityRef The ref of the entity to be purged.
	 */
	public final void purge(ModelKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalArgumentException("A set entity reference must be specified.");
		}
		modelRequest = new PurgeRequest(entityRef);
		crudOp = CrudOp.PURGE;
	}

	@Override
	protected void doExecute() {
		switch(crudOp) {
			case LOAD:
				svc.load((EntityLoadRequest) modelRequest, getAsyncCallback());
				break;

			case ADD:
			case UPDATE:
				svc.persist((PersistRequest) modelRequest, getAsyncCallback());
				break;

			case PURGE:
				svc.purge((PurgeRequest) modelRequest, getAsyncCallback());
				break;

			default:
				throw new IllegalStateException("Unknown crud op: " + crudOp);
		}
	}

	@Override
	protected void handleSuccess(ModelPayload result) {
		// cache aux data first
		AuxDataCacheHelper.cache(result);
		super.handleSuccess(result);
		if(source != null) {
			// fire a model change event
			ModelChangeOp mop;
			switch(crudOp) {
				case LOAD:
					mop = ModelChangeOp.LOADED;
					break;
				case ADD:
					mop = ModelChangeOp.ADDED;
					break;
				case UPDATE:
					mop = ModelChangeOp.UPDATED;
					break;
				case PURGE:
					mop = ModelChangeOp.DELETED;
					break;
				default:
					throw new IllegalStateException("Unhandled crud op: " + crudOp);
			}
			source.fireEvent(new ModelChangeEvent(mop, result.getEntity(), result.getRef(), result.getStatus()));
		}
	}
}
