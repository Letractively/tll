/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.common.data.Payload;

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
