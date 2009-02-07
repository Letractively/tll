/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * RequestContext - Provides servlets w/ the needed constructs to perform the
 * requested job including a ref to the http request.
 * <p>
 * In particular, this construct becomes necessary when the request is delegated
 * to a class that does NOT have access to the http request.
 * @author jpk
 */
public final class RequestContext {

	private final HttpServletRequest request;

	/**
	 * Constructor
	 * @param request The http servlet request.
	 */
	public RequestContext(HttpServletRequest request) {
		super();
		if(request == null) {
			throw new IllegalArgumentException("No http servlet request specified.");
		}
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return request.getSession(false);
	}
	
	public IAppContext getAppContext() {
		return (IAppContext) getSession().getServletContext().getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
	}

	public ISecurityContext getSecurityContext() {
		return (ISecurityContext) getSession().getServletContext().getAttribute(ISecurityContext.SERVLET_CONTEXT_KEY);
	}
}
