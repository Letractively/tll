/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.Payload;

/**
 * IForgotPasswordService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "forgotPassword")
public interface IForgotPasswordService extends RemoteService {

	/**
	 * @param emailAddress
	 * @return the status contained w/in the dataSet transport
	 */
	Payload requestPassword(String emailAddress);

}