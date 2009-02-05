/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.tll.server.ISecurityContext;

/**
 * AbstractSecurityFilter - Base class for all security related {@link Filter}s.
 * @author jpk
 */
public abstract class AbstractSecurityFilter implements Filter {

	/**
	 * Called before any other filter initialization routines are performed.
	 * @param config
	 * @throws ServletException
	 */
	protected void doPreInit(FilterConfig config) throws ServletException {
		// base impl no-op
	}

	/**
	 * Performs filter init when there the security mode is acegi.
	 * @param config
	 * @param securityContext
	 * @throws ServletException
	 */
	protected void doInitAcegi(FilterConfig config, ISecurityContext securityContext) throws ServletException {
		// base impl no-op
	}

	/**
	 * Performs filter init when there is no security mode.
	 * @param config
	 * @throws ServletException
	 */
	protected void doInitNoSecurity(FilterConfig config) throws ServletException {
		// base impl no-op
	}

	/**
	 * Filters when security mode is acegi.
	 * @param request
	 * @param response
	 * @param chain
	 * @param securityContext
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain,
			ISecurityContext securityContext) throws IOException,
			ServletException {
		// base impl no-op
	}

	/**
	 * Filters when there is no security mode.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doFilterNoSecurity(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// pass through
		chain.doFilter(request, response);
	}

	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(request instanceof HttpServletRequest) {
			final ISecurityContext sc =
					(ISecurityContext) ((HttpServletRequest) request).getSession(false).getServletContext().getAttribute(
							ISecurityContext.SERVLET_CONTEXT_KEY);
			switch(sc.getSecurityMode()) {
				case ACEGI:
					doFilterAcegi(request, response, chain, sc);
					break;
				case NONE:
					doFilterNoSecurity(request, response, chain);
					break;
				default:
					throw new Error("Unhandled security mode: " + sc.getSecurityMode());
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public final void init(FilterConfig config) throws ServletException {
		// set the security mode
		doPreInit(config);
		final ISecurityContext sc =
				(ISecurityContext) config.getServletContext().getAttribute(ISecurityContext.SERVLET_CONTEXT_KEY);
		if(sc == null) {
			throw new Error("No security context found in the servlet context.");
		}
		switch(sc.getSecurityMode()) {
			case ACEGI:
				doInitAcegi(config, sc);
				break;
			case NONE:
				doInitNoSecurity(config);
				break;
			default:
				throw new Error("Unhandled security mode: " + sc.getSecurityMode());
		}
	}

	@Override
	public void destroy() {
		// default no-op
	}

}
