package com.tll.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.AuxDataRequest.AuxDataType;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

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
public final class ModelChangeManager {

	/**
	 * ModelChangeAuxDataCommand
	 * @author jpk
	 */
	final class ModelChangeAuxDataCommand extends AuxDataCommand {

		/**
		 * Constructor
		 * @param sourcingWidget
		 * @param auxDataRequest
		 */
		public ModelChangeAuxDataCommand(Widget sourcingWidget, AuxDataRequest auxDataRequest) {
			super(sourcingWidget, auxDataRequest);
		}

		@Override
		protected void handleSuccess(AuxDataPayload result) {
			super.handleSuccess(result);
			//if(!result.hasErrors()) {
			sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY, (Model) null, null));
			//}
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

		/**
		 * Constructor
		 * @param sourcingWidget
		 */
		public ModelChangeCrudCommand(Widget sourcingWidget) {
			super(sourcingWidget);
		}

		@Override
		protected void handleSuccess(EntityPayload result) {
			super.handleSuccess(result);
			switch(crudOp) {
				case LOAD:
					sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.LOADED, result.getEntity(), result.getStatus()));
					break;
				case ADD:
					sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.ADDED, result.getEntity(), result.getStatus()));
					break;
				case UPDATE:
					sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.UPDATED, result.getEntity(), result.getStatus()));
					break;
				case PURGE:
					sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.DELETED, result.getEntityRef(), result
							.getStatus()));
					break;
				default:
					throw new IllegalStateException();
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
		final ModelChangeAuxDataCommand adc = new ModelChangeAuxDataCommand(sourcingWidget, adr);
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
	public boolean fetchModelPrototype(Widget sourcingWidget, IEntityType entityType) {
		if(!AuxDataCache.instance().isCached(AuxDataType.ENTITY_PROTOTYPE, entityType)) {
			final AuxDataRequest adr = new AuxDataRequest();
			adr.requestEntityPrototype(entityType);
			final ModelChangeAuxDataCommand adc = new ModelChangeAuxDataCommand(sourcingWidget, adr);
			adc.execute();
			return true;
		}
		sourcingWidget.fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY));
		return false;
	}

	/**
	 * Handles the fetching of a model given a model ref. A subsequent model
	 * change event is anticipated.
	 * @param sourcingWidget
	 * @param entityKey The reference of the entity to fetch
	 * @param entityOptions
	 * @param adr
	 */
	public void loadModel(Widget sourcingWidget, ModelKey entityKey, EntityOptions entityOptions, AuxDataRequest adr) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(sourcingWidget);
		cmd.load(entityKey);
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
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(sourcingWidget);
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
	 * @param entityKey The key of the entity to delete
	 * @param entityOptions
	 */
	public void deleteModel(Widget sourcingWidget, ModelKey entityKey, EntityOptions entityOptions) {
		final ModelChangeCrudCommand cmd = new ModelChangeCrudCommand(sourcingWidget);
		cmd.purge(entityKey);
		cmd.setEntityOptions(entityOptions);
		cmd.execute();
	}
}
