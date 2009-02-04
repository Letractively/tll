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

import com.tll.server.SecurityMode;

/**
 * AbstractSecurityFilter - Base class for all security related {@link Filter}s.
 * @author jpk
 */
public abstract class AbstractSecurityFilter implements Filter {
	
	/**
	 * @return The operating {@link SecurityMode}.
	 */
	protected abstract SecurityMode getSecurityMode();
	
	/**
	 * Performs filter init when there the security mode is acegi.
	 * @param config
	 * @throws ServletException
	 */
	protected void doInitAcegi(FilterConfig config) throws ServletException {
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
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
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
		switch(getSecurityMode()) {
			case ACEGI:
				doFilterAcegi(request, response, chain);
				break;
			case NONE:
				doFilterNoSecurity(request, response, chain);
				break;
			default:
				throw new Error("Unhandled security mode: " + getSecurityMode());
		}
	}

	@Override
	public final void init(FilterConfig config) throws ServletException {
		switch(getSecurityMode()) {
			case ACEGI:
				doInitAcegi(config);
				break;
			case NONE:
				doInitNoSecurity(config);
				break;
			default:
				throw new Error("Unhandled security mode: " + getSecurityMode());
		}
	}

	@Override
	public void destroy() {
		// default no-op
	}

}
