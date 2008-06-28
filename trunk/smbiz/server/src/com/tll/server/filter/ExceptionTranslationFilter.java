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

import org.acegisecurity.ui.AccessDeniedHandlerImpl;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint;

import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.server.SecurityMode;

/**
 * ExceptionTranslationFilter
 * @author jpk
 */
public final class ExceptionTranslationFilter extends org.acegisecurity.ui.ExceptionTranslationFilter {

	private boolean isAcegi;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		isAcegi = SecurityMode.ACEGI.name().equals(Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));
		if(isAcegi) {
			// access denied handler
			String ep = config.getInitParameter("errorPage");
			if(ep == null) {
				throw new Error("The init parameter 'errorPage' must be declared");
			}
			AccessDeniedHandlerImpl impl = new AccessDeniedHandlerImpl();
			impl.setErrorPage(ep);
			super.setAccessDeniedHandler(impl);

			// authentication entry point
			String lfu = config.getInitParameter("loginFormUrl");
			if(lfu == null) {
				throw new Error("The init parameter 'loginFormUrl' must be declared");
			}
			AuthenticationProcessingFilterEntryPoint apfep = new AuthenticationProcessingFilterEntryPoint();
			apfep.setLoginFormUrl(lfu);
			setAuthenticationEntryPoint(apfep);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(isAcegi) {
			super.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
