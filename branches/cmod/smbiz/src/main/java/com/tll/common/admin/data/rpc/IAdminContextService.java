/**
 * The Logic Lab
 */
package com.tll.common.admin.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.admin.AdminContext;

/**
 * IAdminContextService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/adminContext")
public interface IAdminContextService extends RemoteService {

  /**
   * @return the {@link AdminContext} contained inside the payload.
   */
	AdminContextPayload getAdminContext();

}
