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

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.User;
import com.tll.server.IAppContext;
import com.tll.server.ISecurityContext;
import com.tll.server.admin.AdminContext;
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
	protected void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain,
			ISecurityContext securityContext) throws IOException,
			ServletException {
		doFilterCommon(request, response, chain);
		super.doFilterAcegi(request, response, chain, securityContext);
	}

	@Override
	protected void doFilterNoSecurity(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilterCommon(request, response, chain);
		super.doFilterNoSecurity(request, response, chain);
	}

	private void doFilterCommon(ServletRequest request, ServletResponse response, FilterChain chain)
			/*throws IOException, ServletException*/{
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
