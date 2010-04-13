/**
 * The Logic Lab
 * @author jpk Nov 20, 2007
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

/**
 * HttpSessionContextIntegrationFilter
 * @author jpk
 */
public final class HttpSessionContextIntegrationFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(HttpSessionContextIntegrationFilter.class);
	
	/**
	 * The wrapped
	 * {@link org.springframework.security.context.HttpSessionContextIntegrationFilter}
	 * .
	 */
	private final org.springframework.security.context.HttpSessionContextIntegrationFilter wrapped;

	/**
	 * Constructor
	 * @throws ServletException
	 */
	public HttpSessionContextIntegrationFilter() throws ServletException {
		super();
		wrapped = new org.springframework.security.context.HttpSessionContextIntegrationFilter();
	}

	@Override
	public void init(FilterConfig config) /*throws ServletException*/{
		// required
		try {
			wrapped.afterPropertiesSet();
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}
}
