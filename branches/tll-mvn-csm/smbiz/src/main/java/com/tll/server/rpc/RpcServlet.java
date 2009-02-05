/**
 * The Logic Lab
 */
package com.tll.server.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tll.server.IAppContext;
import com.tll.server.ISecurityContext;
import com.tll.server.RequestContext;

/**
 * RpcServlet
 * @author jpk
 */
public abstract class RpcServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 5032508084607776181L;

	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Provides the current http request context for all rpc servlet
	 * implementations.
	 * @return The {@link RequestContext} for the current server request.
	 */
	protected final RequestContext getRequestContext() {
		return new RequestContext((IAppContext) getServletContext().getAttribute(IAppContext.SERVLET_CONTEXT_KEY),
				(ISecurityContext) getServletContext().getAttribute(ISecurityContext.SERVLET_CONTEXT_KEY),
				getThreadLocalRequest());
	}
}
