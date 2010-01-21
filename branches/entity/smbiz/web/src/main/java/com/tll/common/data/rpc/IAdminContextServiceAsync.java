/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.model.ModelKey;

/**
 * IAdminContextServiceAsync
 * @author jpk
 */
public interface IAdminContextServiceAsync {

	void getAdminContext(AsyncCallback<AdminContextPayload> callback);

	void changeCurrentAccount(ModelKey accountRef, AsyncCallback<AdminContextPayload> callback);
}
