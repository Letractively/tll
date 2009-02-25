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
import com.tll.server.SecurityMode;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
// TODO fix deprecation warning
@SuppressWarnings("deprecation")
public class FilterSecurityInterceptor extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(FilterSecurityInterceptor.class);
	
	private org.springframework.security.intercept.web.FilterSecurityInterceptor wrapped;

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing the FilterSecurityInterceptor..");
		final SecurityContext sc = getSecurityContext(config);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
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
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		log.debug("FilterSecurityInterceptor filtering..");
		final SecurityContext sc = getSecurityContext(request);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
