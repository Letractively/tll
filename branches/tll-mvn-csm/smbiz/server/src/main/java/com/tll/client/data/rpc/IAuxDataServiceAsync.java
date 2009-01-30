/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;

/**
 * IAuxDataServiceAsync
 * @author jpk
 */
public interface IAuxDataServiceAsync {

	void handleAuxDataRequest(AuxDataRequest request, AsyncCallback<AuxDataPayload> callback);

}
