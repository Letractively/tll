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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.ui.AccessDeniedHandlerImpl;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

import com.tll.server.ISecurityContext;
import com.tll.server.SecurityMode;

/**
 * ExceptionTranslationFilter
 * @author jpk
 */
public class ExceptionTranslationFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(ExceptionTranslationFilter.class);
	
	/**
	 * The wrapped
	 * {@link org.springframework.security.ui.ExceptionTranslationFilter}.
	 */
	private org.springframework.security.ui.ExceptionTranslationFilter wrapped;

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing the ExceptionTranslationFilter..");
		ISecurityContext sc = getSecurityContext(config);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			// access denied handler
			String ep = config.getInitParameter("errorPage");
			if(ep == null) {
				throw new Error("The init parameter 'errorPage' must be declared");
			}
			AccessDeniedHandlerImpl impl = new AccessDeniedHandlerImpl();
			impl.setErrorPage(ep);
			wrapped.setAccessDeniedHandler(impl);

			// authentication entry point
			String lfu = config.getInitParameter("loginFormUrl");
			if(lfu == null) {
				throw new ServletException("The init parameter 'loginFormUrl' must be declared");
			}
			AuthenticationProcessingFilterEntryPoint apfep = new AuthenticationProcessingFilterEntryPoint();
			apfep.setLoginFormUrl(lfu);
			wrapped.setAuthenticationEntryPoint(apfep);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		log.debug("ExceptionTranslationFilter filtering..");
		ISecurityContext sc = getSecurityContext(request);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
