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
import com.tll.client.ui.msg.Msgs;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.AbstractModelRequest;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.ICrudService;
import com.tll.common.data.rpc.ICrudServiceAsync;
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
	 * Loads model data.
	 * @param source The sourcing widget on which a subsequent
	 *        {@link ModelChangeEvent} is fired upon rpc return if it is non-
	 *        <code>null</code>.
	 * @param search the required search criteria
	 * @param adr optional aux data request
	 * @return new rpc command ready for execution
	 */
	public static Command loadModel(Widget source, ISearch search, AuxDataRequest adr) {
		final CrudCommand cmd = new CrudCommand();
		cmd.setSource(source);
		cmd.load(search, AuxDataCacheHelper.filterRequest(adr));
		return cmd;
	}

	/**
	 * Persists model data.
	 * @param source The sourcing widget on which a subsequent
	 *        {@link ModelChangeEvent} is fired upon rpc return if it is non-
	 *        <code>null</code>.
	 * @param model The model to persist
	 * @return the rpc command ready for execution
	 */
	public static Command persistModel(Widget source, Model model) {
		final CrudCommand cmd = new CrudCommand();
		cmd.setSource(source);
		if(model.isNew()) {
			cmd.add(model);
		}
		else {
			cmd.update(model);
		}
		return cmd;
	}

	/**
	 * Deletes model data.
	 * @param source The sourcing widget on which a subsequent
	 *        {@link ModelChangeEvent} is fired upon rpc return if it is non-
	 *        <code>null</code>.
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
	protected AbstractModelRequest modelRequest;

	/**
	 * Sets the state of this command for loading an entity by primary key.
	 * @param search the criteria by which the model data is loaded
	 * @param auxDataRequest optional aux data request
	 */
	public void load(ISearch search, AuxDataRequest auxDataRequest) {
		if(search == null || !search.isSet()) {
			throw new IllegalArgumentException("Null or un-set search criteria");
		}
		final LoadRequest<ISearch> elr = new LoadRequest<ISearch>(search);
		elr.setAuxDataRequest(AuxDataCacheHelper.filterRequest(auxDataRequest));
		this.modelRequest = elr;
		crudOp = CrudOp.LOAD;
	}

	/**
	 * Sets the state of this command for adding an entity.
	 * @param model The model data to add.
	 */
	public final void add(Model model) {
		if(model == null || !model.isNew()) {
			throw new IllegalArgumentException("A non-null and new entity must be specified.");
		}
		final PersistRequest pr = new PersistRequest(model, false);
		modelRequest = pr;
		crudOp = CrudOp.ADD;
	}

	/**
	 * Sets the state of this command for updating an entity.
	 * @param dirtyModel The model data containing only the properties that were changed
	 */
	public final void update(Model dirtyModel) {
		if(dirtyModel == null || dirtyModel.isNew()) {
			throw new IllegalArgumentException("A non-null and non-new entity must be specified.");
		}
		final PersistRequest pr = new PersistRequest(dirtyModel, true);
		modelRequest = pr;
		crudOp = CrudOp.UPDATE;
	}

	/**
	 * Sets the state of this command for purging an entity.
	 * @param pk The primary key of the entity to purge expressed as a
	 *        {@link ModelKey}.
	 */
	public final void purge(ModelKey pk) {
		if(pk == null || !pk.isSet()) {
			throw new IllegalArgumentException("A set entity reference must be specified.");
		}
		modelRequest = new PurgeRequest(pk);
		crudOp = CrudOp.PURGE;
	}

	@Override
	protected void doExecute() {
		switch(crudOp) {
		case LOAD:
			svc.load((LoadRequest<?>) modelRequest, getAsyncCallback());
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
			Status status = result.getStatus();
			if(status.hasErrors()) {
				Msgs.post(status.getMsgs(), source);
				return;
			}
			source.fireEvent(new ModelChangeEvent(mop, result.getModel(), result.getRef()));
		}
	}
}
