/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.AddAccountRequest;
import com.tll.common.data.ModelPayload;

/**
 * ICrudService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/aa")
public interface IAddAccountService extends RemoteService {

	/**
	 * Adds an account.
	 * @param request The {@link AddAccountRequest}
	 * @return the added account in a new {@link ModelPayload} instance
	 */
	ModelPayload add(AddAccountRequest request);
}
