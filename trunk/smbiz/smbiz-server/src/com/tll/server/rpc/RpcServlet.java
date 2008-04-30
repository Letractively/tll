/**
 * The Logic Lab
 */
package com.tll.server.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Injector;
import com.tll.server.Constants;
import com.tll.server.RequestContext;

/**
 * RpcServlet
 * @author jpk
 */
public abstract class RpcServlet extends RemoteServiceServlet {

	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Provides the current http request context for all rpc servlet
	 * implementations.
	 * @return The {@link RequestContext} for the current server request.
	 */
	protected final RequestContext getRequestContext() {
		return new RequestContext(getThreadLocalRequest(), getServletContext(), (Injector) getServletContext()
				.getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE));
	}
}
