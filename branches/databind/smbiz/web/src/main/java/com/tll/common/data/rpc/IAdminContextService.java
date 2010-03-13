/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.model.ModelKey;

/**
 * IAdminContextService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/adminContext")
public interface IAdminContextService extends RemoteService {

	/**
	 * This is the init routine that serves to populate the client-side admin
	 * context.
	 * @return the admin context payload by which the client-side admin context is
	 *         populated.
	 */
	AdminContextPayload getAdminContext();

	/**
	 * Changes the current account in the server-side admin context and sends back
	 * the marshaled version of the changed current account.
	 * @param accountRef the desired account to set as the current account in the
	 *        admin context
	 * @return the corres. payload
	 */
	AdminContextPayload changeCurrentAccount(ModelKey accountRef);

}
