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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;

import com.tll.server.ISecurityContext;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public class AuthenticationProcessingFilter extends AbstractSecurityFilter {

	/**
	 * Wrapped - Thin wrapper around Acegi's
	 * {@link org.springframework.security.ui.webapp.AuthenticationProcessingFilter}
	 * .
	 * @author jpk
	 */
	final class Wrapped extends org.springframework.security.ui.webapp.AuthenticationProcessingFilter {

		@Override
		protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
				Authentication authResult) throws IOException {
			super.onSuccessfulAuthentication(request, response, authResult);
			AuthenticationProcessingFilter.this.onSuccessfulAuthentication(request, response, authResult);
		}

		@Override
		protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException failed) throws IOException {
			super.onUnsuccessfulAuthentication(request, response, failed);
			AuthenticationProcessingFilter.this.onUnsuccessfulAuthentication(request, response, failed);
		}
	}

	private final Wrapped wrapped = new Wrapped();

	/**
	 * Invoked upon successful authentication.
	 * @throws IOException
	 */
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		// base impl no-op
	}

	/**
	 * Invoked upon failed authentication.
	 * @throws IOException
	 */
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		// base impl no-op
	}

	@Override
	protected void doInitAcegi(FilterConfig config, ISecurityContext securityContext) /*throws ServletException*/{
		wrapped.setAuthenticationManager(securityContext.getAuthenticationManager());

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
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain,
			ISecurityContext securityContext) throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}

}
