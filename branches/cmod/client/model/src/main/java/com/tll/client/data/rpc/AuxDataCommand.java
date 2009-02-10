/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.common.data.rpc.IAuxDataServiceAsync;

/**
 * AuxDataCommand
 * @author jpk
 */
public final class AuxDataCommand extends RpcCommand<AuxDataPayload> {

	private static final IAuxDataServiceAsync svc;
	static {
		svc = (IAuxDataServiceAsync) GWT.create(IAuxDataService.class);
	}

	private final Widget sourcingWidget;

	private final AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 * @param sourcingWidget
	 * @param auxDataRequest
	 */
	public AuxDataCommand(Widget sourcingWidget, AuxDataRequest auxDataRequest) {
		super();
		this.sourcingWidget = sourcingWidget;
		this.auxDataRequest = auxDataRequest;
	}

	@Override
	protected Widget getSourcingWidget() {
		return sourcingWidget;
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
