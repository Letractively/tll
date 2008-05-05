/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;

/**
 * AuxDataCommand
 * @author jpk
 */
public class AuxDataCommand extends RpcCommand<AuxDataPayload> {

	private static final IAuxDataServiceAsync svc;
	static {
		svc = (IAuxDataServiceAsync) GWT.create(IAuxDataService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/aux");
	}

	private final AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 * @param auxDataRequest
	 */
	public AuxDataCommand(Widget sourcingWidget, AuxDataRequest auxDataRequest) {
		super(sourcingWidget);
		assert auxDataRequest != null;
		this.auxDataRequest = auxDataRequest;
	}

	@Override
	protected void doExecute() {
		svc.handleAuxDataRequest(auxDataRequest, getAsyncCallback());
	}

	@Override
	protected void handleSuccess(AuxDataPayload result) {
		// cache the results
		AuxDataCache.instance().cache(result);
		super.handleSuccess(result);
	}

}
