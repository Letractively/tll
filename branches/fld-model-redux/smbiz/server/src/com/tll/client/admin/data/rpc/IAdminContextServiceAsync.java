/**
 * The Logic Lab
 */
package com.tll.client.admin.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * IAdminContextServiceAsync
 * @author jpk
 */
public interface IAdminContextServiceAsync {

	void getAdminContext(AsyncCallback<AdminContextPayload> callback);
}
