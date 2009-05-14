/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.AddAccountRequest;
import com.tll.common.data.EntityPayload;


/**
 * IAddAccountServiceAsync
 * @author jpk
 */
public interface IAddAccountServiceAsync {

	void add(AddAccountRequest request, AsyncCallback<EntityPayload> callback);
}
