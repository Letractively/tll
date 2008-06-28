/**
 * The Logic Lab
 */
package com.tll.client.admin.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.admin.AdminContext;

/**
 * IAdminContextService
 * @author jpk
 */
public interface IAdminContextService extends RemoteService {

  /**
   * @return the {@link AdminContext} contained inside the payload.
   */
	AdminContextPayload getAdminContext();

}
