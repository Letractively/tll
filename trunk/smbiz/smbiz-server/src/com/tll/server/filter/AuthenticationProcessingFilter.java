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

import org.acegisecurity.AuthenticationManager;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.server.Constants;
import com.tll.server.SecurityMode;
import com.tll.service.entity.impl.user.IUserService;

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

		isAcegi = SecurityMode.ACEGI.name().equals(Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM));
		Injector injector = (Injector) config.getServletContext().getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE);

		if(isAcegi) {
			setAuthenticationManager(injector.getInstance(AuthenticationManager.class));
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
			userService = injector.getInstance(IUserService.class);
			assert userService != null;
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
