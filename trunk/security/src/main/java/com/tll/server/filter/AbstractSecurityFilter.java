/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tll.server.SecurityContext;

/**
 * AbstractSecurityFilter - Base class for all security related {@link Filter}s.
 * @author jpk
 */
public abstract class AbstractSecurityFilter implements Filter {

	/**
	 * Gets the non-<code>null</code> {@link ISecurityContext} instance.
	 * @param request The servlet request
	 * @return The non-<code>null</code> security context.
	 * @throws ServletException When the security context can't be obtained.
	 */
	protected final SecurityContext getSecurityContext(ServletRequest request) throws ServletException {
		assert request != null;
		final HttpSession session = ((HttpServletRequest) request).getSession(false);
		final SecurityContext sc =
				session == null ? null : (SecurityContext) session.getServletContext().getAttribute(
						SecurityContext.SERVLET_CONTEXT_KEY);
		if(sc == null) {
			throw new ServletException("Unable to obtain the security context from the request");
		}
		return sc;
	}

	/**
	 * Gets the non-<code>null</code> {@link ISecurityContext} instance.
	 * @param config The filter config
	 * @return The non-<code>null</code> security context.
	 * @throws ServletException When the security context can't be obtained.
	 */
	protected final SecurityContext getSecurityContext(FilterConfig config) throws ServletException {
		assert config != null;
		final SecurityContext sc =
				(SecurityContext) config.getServletContext().getAttribute(SecurityContext.SERVLET_CONTEXT_KEY);
		if(sc == null) {
			throw new ServletException("Unable to obtain the security context from the filter configuration.");
		}
		return sc;
	}

	@Override
	public void destroy() {
		// default impl no-op
	}
}
