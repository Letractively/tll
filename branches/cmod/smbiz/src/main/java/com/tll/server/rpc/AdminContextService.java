/**
 * The Logic Lab
 * @author jpk Aug 25, 2007
 */
package com.tll.server.rpc;

import com.tll.common.AdminContext;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.AdminContextPayload;
import com.tll.common.data.rpc.IAdminContextService;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.server.AppContext;
import com.tll.server.RequestContext;
import com.tll.server.filter.AuthenticationProcessingFilter;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.entity.MEntityContext;

/**
 * AdminContextService
 * @author jpk
 */
public class AdminContextService extends RpcServlet implements IAdminContextService {

	private static final long serialVersionUID = 7908647379731614097L;
	
	public AdminContextPayload getAdminContext() {
		final RequestContext rc = getRequestContext();
		final MEntityContext mec = (MEntityContext) rc.getServletContext().getAttribute(MEntityContext.SERVLET_CONTEXT_KEY);
		final AppContext ac = (AppContext) rc.getServletContext().getAttribute(AppContext.SERVLET_CONTEXT_KEY);
		
		final com.tll.server.AdminContext sac =
				(com.tll.server.AdminContext) rc.getSession().getAttribute(
						AuthenticationProcessingFilter.SA_ADMIN_CONTEXT);
		assert sac != null;

		final Marshaler entityMarshaller = mec.getMarshaler();
		assert entityMarshaller != null : "No marshaler present";

		final Model user = entityMarshaller.marshalEntity(sac.getUser(), new MarshalOptions(true, 1));
		// NOTE: we want a distinct copy of the account here so we separately
		// marshall the account as opposed to grabbing the nested account from the
		// just marshaled user
		final Model account = entityMarshaller.marshalEntity(sac.getAccount(), MarshalOptions.NON_RELATIONAL);

		final Status status = new Status();
		status.addMsg("Admin Context retrieved.", MsgLevel.INFO, MsgAttr.NODISPLAY.flag);
		return new AdminContextPayload(status, new AdminContext(ac.isDebug(), ac.getEnvironment(), user, account));
	}

}
