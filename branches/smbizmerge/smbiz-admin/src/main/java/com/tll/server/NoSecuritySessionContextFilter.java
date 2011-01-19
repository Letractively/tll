/**
 * The Logic Lab
 */
package com.tll.server;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NoSecuritySessionContextFilter - Creates {@link HttpSession}s populating it
 * with an {@link AdminContext}.
 * <p>
 * This is a replacement filter when the app is in "mock" mode and no security
 * is employed.
 * @author jpk
 */
public final class NoSecuritySessionContextFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(NoSecuritySessionContextFilter.class);

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
// TODO fix
//				final PersistContext pc = (PersistContext) sc.getAttribute(PersistContext.KEY);
//				if(pc == null) {
//					throw new ServletException("Unable to obtain the persist context");
//				}
//				final IUserService userService = pc.getEntityServiceFactory().instance(IUserService.class);
//				final User user = (User) userService.loadUserByUsername(appContext.getDfltUserEmail());
//				log.debug("Creating mock admin context from default user email specified in config..");
//				final AdminContext ac = new AdminContext();
//				ac.setUser(user);
//				session.setAttribute(AdminContext.KEY, ac);
//				log.info("Server-side admin context created and stored in the servlet context");
			}
		}
		finally {
			chain.doFilter(request, response);
		}
	}
}
