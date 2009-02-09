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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;

import com.tll.server.ISecurityContext;
import com.tll.server.SecurityMode;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public abstract class AuthenticationProcessingFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(AuthenticationProcessingFilter.class);

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
	 * Does non-Acegi filtering.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	protected abstract void doFilterNonAcegi(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	/**
	 * Invoked upon successful authentication.
	 * @throws IOException
	 */
	protected abstract void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException;

	/**
	 * Invoked upon failed authentication.
	 * @throws IOException
	 */
	protected abstract void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException;

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing the AuthenticationProcessingFilter..");
		ISecurityContext sc = getSecurityContext(config);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			wrapped.setAuthenticationManager(sc.getAuthenticationManager());

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
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		log.debug("AuthenticationProcessingFilter filtering..");
		if(getSecurityContext(request).getSecurityMode() == SecurityMode.ACEGI) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			doFilterNonAcegi(request, response, chain);
		}
	}

}
