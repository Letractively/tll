/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.AddAccountRequest;
import com.tll.common.data.EntityPayload;

/**
 * ICrudService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/aa")
public interface IAddAccountService extends RemoteService {

	/**
	 * Adds an account.
	 * @param request The {@link AddAccountRequest}
	 * @return the added account in a new {@link EntityPayload} instance
	 */
	EntityPayload add(AddAccountRequest request);
}
