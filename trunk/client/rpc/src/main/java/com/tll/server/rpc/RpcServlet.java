/**
 * The Logic Lab
 */
package com.tll.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tll.common.msg.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.server.RequestContext;

/**
 * RpcServlet
 * @author jpk
 */
public abstract class RpcServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 5032508084607776181L;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Appends ui-friendly message(s) to the given {@link Status} instance from
	 * the given exception.
	 * @param t the exception
	 * @param status the status instance
	 */
	public static final void exceptionToStatus(Throwable t, Status status) {
		String emsg = t.getMessage();
		if(emsg == null) {
			emsg = t.getClass().getSimpleName();
		}
		if(t instanceof RuntimeException) {
			status.addMsg(emsg, MsgLevel.FATAL, MsgAttr.EXCEPTION.flag);
		}
		else {
			status.addMsg(emsg, MsgLevel.ERROR, MsgAttr.EXCEPTION.flag);
		}
	}

	/**
	 * @return The request context for the current request.
	 */
	protected final RequestContext getRequestContext() {
		return new RequestContext(getThreadLocalRequest(), getThreadLocalResponse());
	}
}
