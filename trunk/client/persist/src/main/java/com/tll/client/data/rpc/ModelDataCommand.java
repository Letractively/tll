/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.ModelCache;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
import com.tll.client.ui.msg.Msgs;
import com.tll.common.cache.ModelDataType;
import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.data.rpc.IModelDataService;
import com.tll.common.data.rpc.IModelDataServiceAsync;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.msg.Status;

/**
 * Retrieves model data from the server.
 * <p>
 * Fires {@link ModelChangeEvent}s of type: {@link ModelChangeOp#AUXDATA_READY}
 * if the source is non-<code>null</code> upon rpc return.
 * @author jpk
 */
public class ModelDataCommand extends RpcCommand<ModelDataPayload> {

	/**
	 * Eliminates those requests already present in the {@link ModelCache}.
	 * @param adr The aux data request to filter.
	 * @return The filtered data request to send to the server or
	 *         <code>null</code> if the filtering yields no needed aux data.
	 */
	public static ModelDataRequest filterRequest(ModelDataRequest adr) {
		if(adr == null) return null;
		final ModelCache adc = ModelCache.get();
		final ModelDataRequest sadr = new ModelDataRequest();

		// entities
		Iterator<IEntityType> ets = adr.getEntityRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				final IEntityType et = ets.next();
				if(!adc.isCached(ModelDataType.ENTITY, et)) {
					sadr.requestEntityList(et);
				}
			}
		}

		// entity prototypes
		ets = adr.getEntityPrototypeRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				final IEntityType et = ets.next();
				if(!adc.isCached(ModelDataType.ENTITY_PROTOTYPE, et)) {
					sadr.requestEntityPrototype(et);
				}
			}
		}

		return sadr.size() > 0 ? sadr : null;
	}

	/**
	 * Caches the resultant aux data received from the server.
	 * @param payload The aux data payload
	 */
	public static void cache(ModelDataPayload payload) {

		final ModelCache mc = ModelCache.get();

		// entity lists
		final Map<IEntityType, List<Model>> egm = payload.getEntityMap();
		if(egm != null) {
			for(final Map.Entry<IEntityType, List<Model>> e : egm.entrySet()) {
				mc.cacheEntityList(e.getKey(), e.getValue());
			}
		}

		// entity prototypes
		final Set<Model> eps = payload.getEntityPrototypes();
		if(eps != null) {
			for(final Model p : eps) {
				mc.cacheEntityPrototype(p);
			}
		}
	}
	
	private static final IModelDataServiceAsync svc;
	static {
		svc = (IModelDataServiceAsync) GWT.create(IModelDataService.class);
	}

	/**
	 * Handles the fetching of model related model data. A subsequent model
	 * change event is anticipated if a non-<code>null</code> {@link Command} is
	 * returned.
	 * @param source The sourcing widget on which the {@link ModelChangeEvent} is
	 *        fired
	 * @param adr the aux data request
	 * @return A new {@link Command} instance only if aux data is actually needed
	 *         and <code>null</code> when no aux data is needed. I.e.: it is
	 *         already cached on the client.
	 */
	public static Command fetchModelData(Widget source, ModelDataRequest adr) {
		// do we need any model data from the server?
		adr = filterRequest(adr);
		if(adr == null) return null;
		final ModelDataCommand cmd = new ModelDataCommand(adr);
		cmd.setSource(source);
		return cmd;
	}

	private final ModelDataRequest modelDataRequest;

	/**
	 * Constructor
	 * @param modelDataRequest
	 */
	public ModelDataCommand(ModelDataRequest modelDataRequest) {
		super();
		this.modelDataRequest = modelDataRequest;
	}

	@Override
	protected void doExecute() {
		svc.handleModelDataRequest(modelDataRequest, getAsyncCallback());
	}

	@Override
	protected void handleSuccess(ModelDataPayload result) {
		// cache the results
		cache(result);
		super.handleSuccess(result);
		if(source != null) {
			Status status = result.getStatus();
			if(status.hasErrors()) {
				Msgs.post(status.getMsgs(), source);
				return;
			}
			source.fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY, null, null));
		}
	}

}
