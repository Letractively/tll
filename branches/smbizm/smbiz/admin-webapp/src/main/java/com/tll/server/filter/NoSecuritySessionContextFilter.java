/**
 * The Logic Lab
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.User;
import com.tll.server.AdminContext;
import com.tll.server.AppContext;
import com.tll.server.rpc.entity.MEntityContext;
import com.tll.service.entity.user.IUserService;

/**
 * NoSecuritySessionContextFilter - Creates {@link HttpSession}s populating it
 * with an {@link AdminContext}.
 * <p>
 * This is a replacement filter when the app is in "mock" mode and no security
 * is employed.
 * @author jpk
 */
public final class NoSecuritySessionContextFilter implements Filter {

	private static final Log log = LogFactory.getLog(NoSecuritySessionContextFilter.class);

	@Override
	public void destroy() {
		// no-op
	}

	@Override
	public void init(FilterConfig config) /*throws ServletException*/{
		// no-op
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		log.debug("Creating mock admin context from default user email specified in config..");
		try {
			final HttpServletRequest hsr = (HttpServletRequest) request;
			HttpSession session = hsr.getSession(false);
			if(session == null) {
				session = hsr.getSession(true);
				if(session == null) throw new ServletException("Unable to obtain a servlet session");
				log.info("Servlet session created: " + session.getId());
				final ServletContext sc = session.getServletContext();
				final AppContext appContext = (AppContext) sc.getAttribute(AppContext.KEY);
				if(appContext == null) {
					throw new ServletException("Unable to obtain the app context");
				}
				final MEntityContext mec = (MEntityContext) sc.getAttribute(MEntityContext.KEY);
				if(mec == null) {
					throw new ServletException("Unable to obtain the MEntity context");
				}
				final IUserService userService = mec.getEntityServiceFactory().instance(IUserService.class);
				final User user = (User) userService.loadUserByUsername(appContext.getDfltUserEmail());
				final AdminContext ac = new AdminContext();
				ac.setUser(user);
				ac.setAccount(user.getAccount());
				session.setAttribute(AdminContext.KEY, ac);
			}
		}
		finally {
			chain.doFilter(request, response);
		}
	}
}
