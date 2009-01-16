/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.Payload;

/**
 * IForgotPasswordService
 * @author jpk
 */
public interface IForgotPasswordService extends RemoteService {

	/**
	 * @param emailAddress
	 * @return the status contained w/in the dataSet transport
	 */
	Payload requestPassword(String emailAddress);

}
