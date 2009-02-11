/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import javax.servlet.ServletContext;

import com.tll.server.rpc.RpcServlet;

/**
 * AbstractEntityServiceContextRpcServlet - RPC servlet providing the
 * {@link IMEntityServiceContext}.
 * @author jpk
 */
public abstract class AbstractEntityServiceContextRpcServlet extends RpcServlet {
	private static final long serialVersionUID = -1771012201634873706L;

	/**
	 * Obtains the {@link IMEntityServiceContext} from the {@link ServletContext}.
	 * @return The {@link IMEntityServiceContext} instance.
	 */
	protected final IMEntityServiceContext getMEntityServiceContext() {
		return (IMEntityServiceContext) getThreadLocalRequest().getSession(false).getServletContext().getAttribute(
				IMEntityServiceContext.SERVLET_CONTEXT_KEY);
	}
}
