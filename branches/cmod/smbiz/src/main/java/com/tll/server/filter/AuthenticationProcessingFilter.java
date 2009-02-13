/**
 * The Logic Lab
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.context.SecurityContextHolder;

import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.User;
import com.tll.server.AdminContext;
import com.tll.server.AppContext;
import com.tll.server.rpc.entity.MEntityContext;
import com.tll.service.entity.user.IUserService;

/**
 * AuthenticationProcessingFilter
 * @author jpk
 */
public final class AuthenticationProcessingFilter extends com.tll.server.filter.AuthenticationProcessingFilter {

	/**
	 * ConfigKeys - Configuration property key names used by the app.
	 * @author jpk
	 */
	public enum ConfigKeys implements IConfigKey {

		USER_DEFAULT_EMAIL_PARAM("mail.dflt_user_email");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	/**
	 * The servlet session attribute key identifying the {@link AdminContext}.
	 */
	public static final String SA_ADMIN_CONTEXT = "ac";

	@Override
	protected void doFilterNonAcegi(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Creating mock admin context from default user email specified in config..");
		try {
			final HttpServletRequest hsr = (HttpServletRequest) request;
			final HttpSession session = hsr.getSession(false);
			assert session != null;
			final ServletContext sc = session.getServletContext();
			AdminContext ac = (AdminContext) session.getAttribute(SA_ADMIN_CONTEXT);
			if(ac == null) {
				final String defaultUserEmail = Config.instance().getString(ConfigKeys.USER_DEFAULT_EMAIL_PARAM.getKey());
				assert defaultUserEmail != null : "No default user email defined in the app configuration!";
				final AppContext appContext = (AppContext) sc.getAttribute(AppContext.SERVLET_CONTEXT_KEY);
				if(appContext == null) {
					throw new Error("Unable to obtain the app context");
				}
				final MEntityContext mec = (MEntityContext) sc.getAttribute(MEntityContext.SERVLET_CONTEXT_KEY);
				final IUserService userService = mec.getEntityServiceFactory().instance(IUserService.class);
				final User user = (User) userService.loadUserByUsername(defaultUserEmail);
				ac = new AdminContext();
				ac.setUser(user);
				ac.setAccount(user.getAccount());
				session.setAttribute(SA_ADMIN_CONTEXT, ac);
			}
		}
		finally {
			chain.doFilter(request, response);
		}
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) /*throws IOException*/{

		// create an AdminContext for this servlet session
		log.debug("Creating admin context from acegi security context..");
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assert user != null;
		final AdminContext ac = new AdminContext();
		ac.setUser(user);
		ac.setAccount(user.getAccount()); // default set the current account to the
		// user's owning account

		request.getSession(false).setAttribute(AuthenticationProcessingFilter.SA_ADMIN_CONTEXT, ac);
	}

	@Override
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) /*throws IOException*/{
		log.debug("User authentication failed");
		// no-op
	}
}
