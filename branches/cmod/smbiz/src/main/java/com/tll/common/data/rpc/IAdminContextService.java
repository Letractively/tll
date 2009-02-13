/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.AdminContext;

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
