/**
 * The Logic Lab
 * @author jpk
 * Nov 20, 2007
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.tll.config.Config;
import com.tll.di.SecurityModule.ConfigKeys;
import com.tll.server.SecurityMode;

/**
 * HttpSessionContextIntegrationFilter
 * @author jpk
 */
public class HttpSessionContextIntegrationFilter implements Filter {
	
	private boolean isAcegi;
	private org.springframework.security.context.HttpSessionContextIntegrationFilter wrapped;
	
	/**
	 * Constructor
	 * @throws ServletException
	 */
	public HttpSessionContextIntegrationFilter() throws ServletException {
		super();
	}

	@Override
	public void init(FilterConfig filterConfig) /*throws ServletException*/{
		isAcegi = SecurityMode.ACEGI.name().equals(Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(isAcegi) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			if(request instanceof HttpServletRequest) {
				((HttpServletRequest) request).getSession(true); // force session
				// creation
			}
			chain.doFilter(request, response);
		}
	}

}
