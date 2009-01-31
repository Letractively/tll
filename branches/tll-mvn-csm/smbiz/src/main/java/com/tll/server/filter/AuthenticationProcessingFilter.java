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

import com.google.inject.Inject;
import com.tll.server.IAppContext;
import com.tll.server.SecurityMode;
import com.tll.service.entity.user.IUserService;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public abstract class AuthenticationProcessingFilter extends org.acegisecurity.ui.webapp.AuthenticationProcessingFilter {

	protected boolean isAcegi;

	@Inject
	protected IUserService userService;

	@Override
	public final void init(FilterConfig config) throws ServletException {
		super.init(config);
		
		IAppContext ac = (IAppContext) config.getServletContext().getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
		if(ac == null) {
			throw new Error("No app context found.");
		}

		if(ac.getSecurityMode() == SecurityMode.ACEGI) {
			setAuthenticationManager(ac.getAuthenticationManager());
			assert getAuthenticationManager() != null;
			String afu = config.getInitParameter("authenticationFailureUrl");
			if(afu == null) {
				throw new Error("The init parameter 'authenticationFailureUrl' must be declared");
			}
			setAuthenticationFailureUrl(afu);

			String dtu = config.getInitParameter("defaultTargetUrl");
			if(dtu == null) {
				throw new Error("The init parameter 'defaultTargetUrl' must be declared");
			}
			setDefaultTargetUrl(dtu);
			setAlwaysUseDefaultTargetUrl(true); // avoids Acegi's SavedRequest
			// mechanism
		}
		else {
			userService = ac.getEntityServiceFactory().instance(IUserService.class);
			if(userService == null) {
				throw new Error("Unable to obtain reference to the user service.");
			}
		}
	}

	/**
	 * Sub-classes extend this method.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	protected abstract void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(isAcegi) {
			super.doFilter(request, response, chain);
		}
		else {
			doFilterInternal(request, response, chain);
		}
	}

}
