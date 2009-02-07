/**
 * The Logic Lab
 * @author jpk
 * Nov 20, 2007
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.tll.server.ISecurityContext;

/**
 * HttpSessionContextIntegrationFilter
 * @author jpk
 */
public class HttpSessionContextIntegrationFilter extends AbstractSecurityFilter {
	
	/**
	 * The wrapped
	 * {@link org.springframework.security.context.HttpSessionContextIntegrationFilter}
	 * .
	 */
	private org.springframework.security.context.HttpSessionContextIntegrationFilter wrapped;
	
	/**
	 * Constructor
	 * @throws ServletException
	 */
	public HttpSessionContextIntegrationFilter() throws ServletException {
		super();
	}

	@Override
	protected void doFilterNoSecurity(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if(request instanceof HttpServletRequest) {
			// force session creation
			((HttpServletRequest) request).getSession(true);
		}
		super.doFilterNoSecurity(request, response, chain);
	}

	@Override
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain,
			ISecurityContext securityContext) throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}

}
