/**
 * The Logic Lab
 * @author jpk Aug 25, 2007
 */
package com.tll.server.admin.rpc;

import com.tll.common.admin.AdminContext;
import com.tll.common.admin.data.rpc.AdminContextPayload;
import com.tll.common.admin.data.rpc.IAdminContextService;
import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.server.Constants;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.RpcServlet;

/**
 * AdminContextService
 * @author jpk
 */
public class AdminContextService extends RpcServlet implements IAdminContextService {

	private static final long serialVersionUID = 7908647379731614097L;

	public AdminContextPayload getAdminContext() {
		final RequestContext rc = getRequestContext();
		final com.tll.server.admin.AdminContext sac =
				(com.tll.server.admin.AdminContext) rc.getSession().getAttribute(Constants.SA_ADMIN_CONTEXT);
		assert sac != null;

		final Marshaler entityMarshaller = rc.getMarshaler();
		assert entityMarshaller != null : "No marshaler present";

		final Model user = entityMarshaller.marshalEntity(sac.getUser(), new MarshalOptions(true, 1));
		// NOTE: we want a distinct copy of the account here so we separately
		// marshall the account as opposed to grabbing the nested account from the
		// just marshaled user
		final Model account = entityMarshaller.marshalEntity(sac.getAccount(), MarshalOptions.NON_RELATIONAL);

		final AdminContext ac = new AdminContext(rc.isDebug(), rc.getEnvironment(), user, account);

		final Status status = new Status();
		status.addMsg("Admin Context retrieved.", MsgLevel.INFO, MsgAttr.NODISPLAY.flag);
		return new AdminContextPayload(status, ac);
	}

}
