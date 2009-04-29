package com.tll.client.data.rpc;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.ModelChangeDispatcher;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
import com.tll.common.cache.AuxDataType;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * ModelChangeManager - Issues RPC requests for model CRUD and aux data related
 * requests in a centralized manner.
 * @author jpk
 */
public final class ModelChangeManager {

	/**
	 * ModelChangeAuxDataCommand
	 * @author jpk
	 */
	final class ModelChangeAuxDataCommand extends AuxDataCommand {

		/**
		 * Constructor
		 * @param auxDataRequest
		 */
		public ModelChangeAuxDataCommand(AuxDataRequest auxDataRequest) {
			super(auxDataRequest);
		}

		@Override
		protected void handleSuccess(AuxDataPayload result) {
			super.handleSuccess(result);
			ModelChangeDispatcher.get().fireEvent(
					new ModelChangeEvent(ModelChangeOp.AUXDATA_READY, (Model) null, result.getStatus()));
		}

		@Override
		protected void handleFailure(Throwable caught) {
			super.handleFailure(caught);
		}
	}

	/**
	 * ModelChangeCrudCommand
	 * @author jpk
	 */
	final class ModelChangeCrudCommand extends CrudCommand {

		@Override
		protected void handleSuccess(EntityPayload result) {
			super.handleSuccess(result);
			switch(crudOp) {
				case LOAD:
					ModelChangeDispatcher.get().fireEvent(
							new ModelChangeEvent(ModelChangeOp.LOADED, result.getEntity(), result.getStatus()));
					break;
				case ADD:
					ModelChangeDispatcher.get().fireEvent(
							new ModelChangeEvent(ModelChangeOp.ADDED, result.getEntity(), result.getStatus()));
					break;
				case UPDATE:
					ModelChangeDispatcher.get().fireEvent(
							new ModelChangeEvent(ModelChangeOp.UPDATED, result.getEntity(), result.getStatus()));
					break;
				case PURGE:
					ModelChangeDispatcher.get().fireEvent(
							new ModelChangeEvent(ModelChangeOp.DELETED, result.getEntityRef(), result
									.getStatus()));
					break;
				default:
					throw new IllegalStateException("Unhandled crud op: " + crudOp);
			}
		}

		@Override
		protected void handleFailure(Throwable caught) {
			super.handleFailure(caught);
		}

	}

	private static ModelChangeManager instance = new ModelChangeManager();

	public static ModelChangeManager get() {
		return instance;
	}

	/**
	 * Constructor
	 */
	private ModelChangeManager() {
	}

	/**
	 * Handles the fetching of needed auxiliary data. A subsequent model change
	 * event is anticipated if aux data is found needed (<code>true</code> is
	 * returned).
	 * @param adr the aux data request
	 * @return A new {@link IRpcCommand} instance only if aux data is actually
	 *         needed and <code>null</code> when no aux data is needed. I.e.: it
	 *         is already cached on the client.
	 */
	public IRpcCommand fetchAuxData(AuxDataRequest adr) {
		// do we need any aux data from the server?
		adr = AuxDataCacheHelper.filterRequest(adr);
		return adr == null ? null : new ModelChangeAuxDataCommand(adr);
	}

	/**
	 * Handles the fetching of a prototypical model given an entity type. A
	 * subsequent model change event is anticipated. Used preliminarily for adding
	 * entities. A subsequent model change event is anticipated if aux data is
	 * found needed (<code>true</code> is returned).
	 * @param entityType The entity type of the model entity to fetch
	 * @return A new {@link IRpcCommand} instance if the model prototype must be
	 *         fetched from the server and <code>null</code> when the prototype is
	 *         already cached on the client.
	 */
	public IRpcCommand fetchModelPrototype(IEntityType entityType) {
		if(!AuxDataCache.get().isCached(AuxDataType.ENTITY_PROTOTYPE, entityType)) {
			final AuxDataRequest adr = new AuxDataRequest();
			adr.requestEntityPrototype(entityType);
			return new ModelChangeAuxDataCommand(adr);
		}
		ModelChangeDispatcher.get().fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY));
		return null;
	}

	/**
	 * Handles the fetching of a model given a model ref. A subsequent model
	 * change event is anticipated.
	 * @param entityKey The reference of the entity to fetch
	 * @param entityOptions
	 * @param adr
	 * @return the rpc command ready for execution
	 */
	public IRpcCommand loadModel(ModelKey entityKey, EntityOptions entityOptions, AuxDataRequest adr) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand();
		cmd.load(entityKey, AuxDataCacheHelper.filterRequest(adr));
		cmd.setEntityOptions(entityOptions);
		return cmd;
	}

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event. A subsequent model change event is anticipated.
	 * @param model The model to persist
	 * @param entityOptions
	 * @return the rpc command ready for execution
	 */
	public IRpcCommand persistModel(Model model, EntityOptions entityOptions) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand();
		if(model.isNew()) {
			cmd.add(model);
		}
		else {
			cmd.update(model);
		}
		cmd.setEntityOptions(entityOptions);
		return cmd;
	}

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param entityKey The key of the entity to delete
	 * @param entityOptions
	 * @return the rpc command ready for execution
	 */
	public IRpcCommand deleteModel(ModelKey entityKey, EntityOptions entityOptions) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand();
		cmd.purge(entityKey);
		cmd.setEntityOptions(entityOptions);
		return cmd;
	}
}
