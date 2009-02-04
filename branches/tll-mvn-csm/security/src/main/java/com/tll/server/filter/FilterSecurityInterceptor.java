/**
 * The Logic Lab
 */
package com.tll.server.filter;

import javax.servlet.FilterConfig;

import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSourceEditor;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
@SuppressWarnings("deprecation")
public abstract class FilterSecurityInterceptor extends AbstractSecurityFilter {

	private org.springframework.security.intercept.web.FilterSecurityInterceptor wrapped;

	/**
	 * @return the {@link AuthenticationManager}.
	 */
	public abstract AuthenticationManager getAuthenticationManager();

	/**
	 * @return the http request related {@link AccessDecisionManager}.
	 */
	public abstract AccessDecisionManager getHttpRequestAccessDecisionManager();
	
	@Override
	public void doInitAcegi(FilterConfig config) /*throws ServletException*/{
		wrapped.setAuthenticationManager(getAuthenticationManager());
		wrapped.setAccessDecisionManager(getHttpRequestAccessDecisionManager());

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
