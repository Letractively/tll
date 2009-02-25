/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * IAdminContextServiceAsync
 * @author jpk
 */
public interface IAdminContextServiceAsync {

	void getAdminContext(AsyncCallback<AdminContextPayload> callback);
}
