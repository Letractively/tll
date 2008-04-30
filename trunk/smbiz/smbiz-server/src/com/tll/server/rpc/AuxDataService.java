/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.server.rpc;

import com.tll.SystemError;
import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.IAuxDataService;
import com.tll.server.RequestContext;
import com.tll.server.ServletUtil;

/**
 * AuxDataService - Implementation of IAuxDataService
 * @author jpk
 */
public class AuxDataService extends RpcServlet implements IAuxDataService {

	private static final long serialVersionUID = -1401752670047477269L;

	public AuxDataPayload handleAuxDataRequest(AuxDataRequest request) {
		final RequestContext rc = getRequestContext();
		final AuxDataPayload payload = new AuxDataPayload(new Status());
		try {
			AuxDataHandler.getAuxData(rc, request, payload);
		}
		catch(SystemError se) {
			ServletUtil.handleException(rc, payload, se, null, true);
		}
		catch(RuntimeException re) {
			ServletUtil.handleException(rc, payload, re, null, true);
			throw re;
		}
		return payload;
	}

}
