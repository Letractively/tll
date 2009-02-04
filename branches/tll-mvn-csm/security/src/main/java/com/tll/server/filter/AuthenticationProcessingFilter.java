/**
 * The Logic Lab
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.AuthenticationManager;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public abstract class AuthenticationProcessingFilter extends AbstractSecurityFilter {

	/**
	 * The Acegi
	 * {@link org.springframework.security.ui.webapp.AuthenticationProcessingFilter}
	 * .
	 */
	private org.springframework.security.ui.webapp.AuthenticationProcessingFilter wrapped;

	/**
	 * Called only when the security mode is acegi.
	 * @return The {@link AuthenticationManager}.
	 */
	protected abstract AuthenticationManager getAuthenticationManager();
	
	@Override
	protected void doInitAcegi(FilterConfig config) /*throws ServletException*/{
		wrapped.setAuthenticationManager(getAuthenticationManager());

		String afu = config.getInitParameter("authenticationFailureUrl");
		if(afu == null) {
			throw new Error("The init parameter 'authenticationFailureUrl' must be declared");
		}
		wrapped.setAuthenticationFailureUrl(afu);

		String dtu = config.getInitParameter("defaultTargetUrl");
		if(dtu == null) {
			throw new Error("The init parameter 'defaultTargetUrl' must be declared");
		}
		wrapped.setDefaultTargetUrl(dtu);

		// avoids Acegi's SavedRequest mechanism
		wrapped.setAlwaysUseDefaultTargetUrl(true);
	}

	@Override
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}

}
