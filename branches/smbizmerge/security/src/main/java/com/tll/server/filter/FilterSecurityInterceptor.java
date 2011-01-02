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
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSourceEditor;

import com.tll.server.SecurityContext;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
// TODO fix deprecation
@SuppressWarnings("deprecation")
public class FilterSecurityInterceptor extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(FilterSecurityInterceptor.class);
	
	private final org.springframework.security.intercept.web.FilterSecurityInterceptor wrapped 
	 = new org.springframework.security.intercept.web.FilterSecurityInterceptor();

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing the FilterSecurityInterceptor..");
		final SecurityContext sc = getSecurityContext(config);
		wrapped.setAuthenticationManager(sc.getAuthenticationManager());
		wrapped.setAccessDecisionManager(sc.getHttpRequestAccessDecisionManager());

		final String ods = config.getInitParameter("objectDefinitionSource");
		if(ods == null) {
			throw new Error("The init parameter 'objectDefinitionSource' must be declared");
		}
		final FilterInvocationDefinitionSourceEditor editor = new FilterInvocationDefinitionSourceEditor();
		editor.setAsText(ods);
		final FilterInvocationDefinitionSource fids = (FilterInvocationDefinitionSource) editor.getValue();
		wrapped.setObjectDefinitionSource(fids);
		
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
		log.debug("FilterSecurityInterceptor filtering..");
		wrapped.doFilter(request, response, chain);
	}

}
