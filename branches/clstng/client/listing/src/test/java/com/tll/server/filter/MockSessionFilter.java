/**
 * The Logic Lab
 * @author jpk
 * @since Mar 18, 2009
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

/**
 * MockSessionFilter - Stubs a session if there isn't one.
 * @author jpk
 */
public class MockSessionFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
			IOException {
		if(((HttpServletRequest) request).getSession(false) == null) {
			((HttpServletRequest) request).getSession(true);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

}
