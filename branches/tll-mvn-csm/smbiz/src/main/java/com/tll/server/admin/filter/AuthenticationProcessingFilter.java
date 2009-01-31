/**
 * The Logic Lab
 */
package com.tll.server.admin.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.model.User;
import com.tll.server.admin.AdminContext;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public final class AuthenticationProcessingFilter extends com.tll.server.filter.AuthenticationProcessingFilter {

	/**
	 * The servlet session attribute key identifying the {@link AdminContext}.
	 */
	public static final String SA_ADMIN_CONTEXT = "ac";

	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			if(request instanceof HttpServletRequest) {
				HttpServletRequest hsr = (HttpServletRequest) request;
				assert hsr.getSession(false) != null;
				AdminContext ac = (AdminContext) hsr.getSession(false).getAttribute(SA_ADMIN_CONTEXT);
				if(ac == null) {
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

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		super.onSuccessfulAuthentication(request, response, authResult);

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
