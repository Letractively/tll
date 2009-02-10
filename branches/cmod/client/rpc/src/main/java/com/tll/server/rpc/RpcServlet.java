/**
 * The Logic Lab
 */
package com.tll.server.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tll.server.RequestContext;

/**
 * RpcServlet
 * @author jpk
 */
public abstract class RpcServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 5032508084607776181L;

	protected final Log log = LogFactory.getLog(this.getClass());

	public RequestContext getRequestContext() {
		return new RequestContext(getThreadLocalRequest());
	}
}
