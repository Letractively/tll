/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.Payload;

/**
 * IForgotPasswordServiceAsync
 * @author jpk
 */
public interface IForgotPasswordServiceAsync {

	void requestPassword(String emailAddress, AsyncCallback<Payload> callback);
}
