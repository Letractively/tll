/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.server.rpc;

import com.tll.SystemError;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.server.AppServletUtil;
import com.tll.server.RequestContext;

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
			AppServletUtil.handleException(rc, payload.getStatus(), se, null, true);
		}
		catch(RuntimeException re) {
			AppServletUtil.handleException(rc, payload.getStatus(), re, null, true);
			throw re;
		}
		return payload;
	}

}
