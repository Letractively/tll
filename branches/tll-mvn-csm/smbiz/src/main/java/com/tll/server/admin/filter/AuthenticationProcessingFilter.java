/**
 * The Logic Lab
 */
package com.tll.server.admin.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.security.AuthenticationManager;

import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.model.User;
import com.tll.server.IAppContext;
import com.tll.server.SecurityMode;
import com.tll.server.admin.AdminContext;
import com.tll.service.entity.user.IUserService;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public final class AuthenticationProcessingFilter extends com.tll.server.filter.AuthenticationProcessingFilter {
	
	private SecurityMode securityMode;

	@Override
	protected AuthenticationManager getAuthenticationManager() {
		return null;
	}

	@Override
	protected SecurityMode getSecurityMode() {
		return securityMode;
	}

	/**
	 * The servlet session attribute key identifying the {@link AdminContext}.
	 */
	public static final String SA_ADMIN_CONTEXT = "ac";

	private void doFilterCommon(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			if(request instanceof HttpServletRequest) {
				HttpServletRequest hsr = (HttpServletRequest) request;
				HttpSession session = hsr.getSession(false);
				assert session != null;
				ServletContext sc = session.getServletContext();
				AdminContext ac = (AdminContext) session.getAttribute(SA_ADMIN_CONTEXT);
				if(ac == null) {
					IAppContext appContext = (IAppContext) sc.getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
					if(appContext == null) {
						throw new Error("Unable to obtain the app context");
					}
					IUserService userService = appContext.getEntityServiceFactory().instance(IUserService.class);
					String defaultUserEmail = Config.instance().getString(ConfigKeys.USER_DEFAULT_EMAIL_PARAM.getKey());
					assert defaultUserEmail != null : "No default user email defined in the app configuration!";
					User user = userService.findByUsername(defaultUserEmail);
					ac = new AdminContext();
					ac.setUser(user);
					ac.setAccount(user.getAccount());
					hsr.getSession(false).setAttribute(SA_ADMIN_CONTEXT, ac);
				}
			}
		}
		finally {
			chain.doFilter(request, response);
		}
	}

	// TODO fix
	// @Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		// super.onSuccessfulAuthentication(request, response, authResult);

		// create an AdminContext for this servlet session
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assert user != null;
		AdminContext ac = new AdminContext();
		ac.setUser(user);
		ac.setAccount(user.getAccount()); // default set the current account to the
		// user's owning account

		request.getSession(false).setAttribute(AuthenticationProcessingFilter.SA_ADMIN_CONTEXT, ac);
	}
}
