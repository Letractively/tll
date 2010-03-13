/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;

public interface IModelDataServiceAsync {

	void handleModelDataRequest(ModelDataRequest request, AsyncCallback<ModelDataPayload> callback);

}
