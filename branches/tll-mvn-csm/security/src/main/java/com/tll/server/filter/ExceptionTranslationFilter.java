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

import org.springframework.security.ui.AccessDeniedHandlerImpl;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

/**
 * ExceptionTranslationFilter
 * @author jpk
 */
public abstract class ExceptionTranslationFilter extends AbstractSecurityFilter {

	/**
	 * The wrapped
	 * {@link org.springframework.security.ui.ExceptionTranslationFilter}.
	 */
	private org.springframework.security.ui.ExceptionTranslationFilter wrapped;

	@Override
	public void doInitAcegi(FilterConfig config) {
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
			throw new Error("The init parameter 'loginFormUrl' must be declared");
		}
		AuthenticationProcessingFilterEntryPoint apfep = new AuthenticationProcessingFilterEntryPoint();
		apfep.setLoginFormUrl(lfu);
		wrapped.setAuthenticationEntryPoint(apfep);
	}

	@Override
	public void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}

}
