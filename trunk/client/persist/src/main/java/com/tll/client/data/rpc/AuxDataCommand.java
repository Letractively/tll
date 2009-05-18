/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeEvent.ModelChangeOp;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.common.data.rpc.IAuxDataServiceAsync;

/**
 * AuxDataCommand - Retrieves aux data from the server.
 * <p>
 * Fires {@link ModelChangeEvent}s of type: {@link ModelChangeOp#AUXDATA_READY}
 * if the source is non-<code>null</code> upon rpc return.
 * @author jpk
 */
public class AuxDataCommand extends RpcCommand<AuxDataPayload> {

	private static final IAuxDataServiceAsync svc;
	static {
		svc = (IAuxDataServiceAsync) GWT.create(IAuxDataService.class);
	}

	/**
	 * Handles the fetching of model related auxiliary data. A subsequent model
	 * change event is anticipated if a non-<code>null</code> {@link Command} is
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
		if(adr == null) return null;
		final AuxDataCommand cmd = new AuxDataCommand(adr);
		cmd.setSource(source);
		return cmd;
	}

	private final AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 * @param auxDataRequest
	 */
	public AuxDataCommand(AuxDataRequest auxDataRequest) {
		super();
		this.auxDataRequest = auxDataRequest;
	}

	@Override
	protected void doExecute() {
		svc.handleAuxDataRequest(auxDataRequest, getAsyncCallback());
	}

	@Override
	protected void handleSuccess(AuxDataPayload result) {
		// cache the results
		AuxDataCacheHelper.cache(result);
		super.handleSuccess(result);
		if(source != null)
			source.fireEvent(new ModelChangeEvent(ModelChangeOp.AUXDATA_READY, null, null, result.getStatus()));
	}

}
