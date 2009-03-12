/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;

/**
 * IAuxDataServiceAsync
 * @author jpk
 */
public interface IAuxDataServiceAsync {

	void handleAuxDataRequest(AuxDataRequest request, AsyncCallback<AuxDataPayload> callback);

}
