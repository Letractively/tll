/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;

/**
 * IAuxDataService
 * @author jpk
 */
public interface IAuxDataService extends RemoteService {

	/**
	 * Handles a request for "auxiliary" data.
	 * @param request The auxiliary data request
	 * @return Auxiliary payload
	 */
	AuxDataPayload handleAuxDataRequest(AuxDataRequest request);

}
