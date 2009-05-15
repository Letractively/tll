/**
 * The Logic Lab
 * @author jpk Aug 25, 2007
 */
package com.tll.server.rpc;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.AdminContextPayload;
import com.tll.common.data.rpc.IAdminContextService;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.server.AppContext;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.entity.MEntityContext;
import com.tll.server.rpc.entity.MEntityServiceDelegate;

/**
 * AdminContextService
 * @author jpk
 */
public class AdminContextService extends RpcServlet implements IAdminContextService {

	private static final long serialVersionUID = 7908647379731614097L;

	@Override
	public AdminContextPayload getAdminContext() {
		final RequestContext rc = getRequestContext();
		final MEntityContext mec = (MEntityContext) rc.getServletContext().getAttribute(MEntityContext.KEY);
		final AppContext ac = (AppContext) rc.getServletContext().getAttribute(AppContext.KEY);

		final Status status = new Status();

		final com.tll.server.AdminContext sac =
			(com.tll.server.AdminContext) rc.getSession().getAttribute(
					com.tll.server.AdminContext.KEY);
		if(sac == null) {
			// presume not logged in yet
			status.addMsg("Admin Context not found.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			return new AdminContextPayload(status, ac.isDebug(), ac.getEnvironment(), null, null);
		}

		final Marshaler entityMarshaller = mec.getMarshaler();
		assert entityMarshaller != null : "No marshaler present";

		final Model user = entityMarshaller.marshalEntity(sac.getUser(), new MarshalOptions(true, 1));
		// NOTE: we want a distinct copy of the account here so we separately
		// marshall the account as opposed to grabbing the nested account from the
		// just marshaled user
		final Model account = entityMarshaller.marshalEntity(sac.getUser().getAccount(), MarshalOptions.NON_RELATIONAL);

		status.addMsg("Admin Context retrieved.", MsgLevel.INFO, MsgAttr.STATUS.flag);
		return new AdminContextPayload(status, ac.isDebug(), ac.getEnvironment(), user, account);
	}

	@Override
	public AdminContextPayload changeCurrentAccount(ModelKey accountRef) {
		final RequestContext rc = getRequestContext();
		final MEntityServiceDelegate delegate = (MEntityServiceDelegate) rc.getServletContext().getAttribute(MEntityServiceDelegate.KEY);
		final MEntityContext mec = (MEntityContext) rc.getServletContext().getAttribute(MEntityContext.KEY);
		final com.tll.server.AdminContext sac =
			(com.tll.server.AdminContext) rc.getSession().getAttribute(com.tll.server.AdminContext.KEY);
		final Marshaler em = mec.getMarshaler();
		assert delegate != null && em != null && sac != null;

		final ModelPayload ep = delegate.load(new EntityLoadRequest(accountRef));
		final Status status = ep.getStatus();

		status.addMsg("Admin Context current account retrieved for " + accountRef.descriptor(), MsgLevel.INFO,
				MsgAttr.STATUS.flag);
		return new AdminContextPayload(status, ep.getEntity());
	}
}
