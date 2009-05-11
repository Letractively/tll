package com.tll.client.model;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.rpc.AuxDataCacheHelper;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
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
 * ModelChangeManager - Factory that generates {@link Command}s ready for
 * execution that trigger {@link ModelChangeEvent}s.
 * @author jpk
 */
public final class ModelChangeManager {

	/**
	 * ModelChangeAuxDataCommand
	 * @author jpk
	 */
	private static final class ModelChangeAuxDataCommand extends AuxDataCommand {

		/**
		 * Constructor
		 * @param source
		 * @param auxDataRequest
		 */
		public ModelChangeAuxDataCommand(Widget source, AuxDataRequest auxDataRequest) {
			super(auxDataRequest);
			setSource(source);
		}

		@Override
		protected void handleSuccess(AuxDataPayload result) {
			super.handleSuccess(result);
			if(source != null)
				source.fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY, null, null, result.getStatus()));
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
	private static final class ModelChangeCrudCommand extends CrudCommand {

		private final ModelKey modelKey;

		/**
		 * Constructor
		 * @param source
		 * @param modelKey
		 */
		public ModelChangeCrudCommand(Widget source, ModelKey modelKey) {
			setSource(source);
			this.modelKey = modelKey;
		}

		@Override
		protected void handleSuccess(EntityPayload result) {
			super.handleSuccess(result);
			if(source == null) return;
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
			source.fireEvent(new ModelChangeEvent(mop, result.getEntity(), modelKey, result.getStatus()));
		}
	}

	/**
	 * Constructor
	 */
	private ModelChangeManager() {
	}

	/**
	 * Handles the fetching of needed auxiliary data. A subsequent model change
	 * event is anticipated if a non-<code>null</code> {@link Command} is
	 * returned.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param adr the aux data request
	 * @return A new {@link Command} instance only if aux data is actually needed
	 *         and <code>null</code> when no aux data is needed. I.e.: it is
	 *         already cached on the client.
	 */
	public static Command fetchAuxData(Widget source, AuxDataRequest adr) {
		// do we need any aux data from the server?
		adr = AuxDataCacheHelper.filterRequest(adr);
		return adr == null ? null : new ModelChangeAuxDataCommand(source, adr);
	}

	/**
	 * Handles the fetching of a prototypical model given an entity type. A
	 * subsequent model change event is anticipated if a non-<code>null</code>
	 * {@link Command} is returned.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param entityType The entity type of the model entity to fetch
	 * @return A new {@link Command} instance if the model prototype must be
	 *         fetched from the server and <code>null</code> when the prototype is
	 *         already cached on the client.
	 */
	public static Command fetchModelPrototype(Widget source, IEntityType entityType) {
		if(!AuxDataCache.get().isCached(AuxDataType.ENTITY_PROTOTYPE, entityType)) {
			final AuxDataRequest adr = new AuxDataRequest();
			adr.requestEntityPrototype(entityType);
			return new ModelChangeAuxDataCommand(source, adr);
		}
		return null;
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
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(source, key);
		cmd.load(key, AuxDataCacheHelper.filterRequest(adr));
		cmd.setEntityOptions(entityOptions);
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
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(source, model.getKey());
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
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param key The key identifying the model
	 * @param entityOptions
	 * @return the rpc command ready for execution
	 */
	public static Command deleteModel(Widget source, ModelKey key, EntityOptions entityOptions) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(source, key);
		cmd.purge(key);
		cmd.setEntityOptions(entityOptions);
		return cmd;
	}
}
