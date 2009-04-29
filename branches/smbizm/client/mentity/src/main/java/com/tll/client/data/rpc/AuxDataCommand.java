/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.common.data.rpc.IAuxDataServiceAsync;

/**
 * AuxDataCommand
 * @author jpk
 */
class AuxDataCommand extends RpcCommand<AuxDataPayload> {

	private static final IAuxDataServiceAsync svc;
	static {
		svc = (IAuxDataServiceAsync) GWT.create(IAuxDataService.class);
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
	}

}
