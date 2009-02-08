/**
 * The Logic Lab
 */
package com.tll.server.filter;

import javax.servlet.FilterConfig;

import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSourceEditor;

import com.tll.server.ISecurityContext;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
// TODO fix deprecation warning
@SuppressWarnings("deprecation")
public class FilterSecurityInterceptor extends AbstractSecurityFilter {

	private org.springframework.security.intercept.web.FilterSecurityInterceptor wrapped;

	@Override
	public void doInitAcegi(FilterConfig config, ISecurityContext securityContext) /*throws ServletException*/{
		wrapped.setAuthenticationManager(securityContext.getAuthenticationManager());
		wrapped.setAccessDecisionManager(securityContext.getHttpRequestAccessDecisionManager());

		String ods = config.getInitParameter("objectDefinitionSource");
		if(ods == null) {
			throw new Error("The init parameter 'objectDefinitionSource' must be declared");
		}
		FilterInvocationDefinitionSourceEditor editor = new FilterInvocationDefinitionSourceEditor();
		editor.setAsText(ods);
		FilterInvocationDefinitionSource fids = (FilterInvocationDefinitionSource) editor.getValue();
		wrapped.setObjectDefinitionSource(fids);
	}
}
