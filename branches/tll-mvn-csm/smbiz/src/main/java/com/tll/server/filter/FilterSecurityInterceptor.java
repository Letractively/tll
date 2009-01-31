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

import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSourceEditor;

import com.tll.server.IAppContext;
import com.tll.server.SecurityMode;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
public final class FilterSecurityInterceptor extends org.acegisecurity.intercept.web.FilterSecurityInterceptor {

	private SecurityMode securityMode;

	/*
	@Override
	@Inject
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	@Inject
	public void setAccessDecisionManager(@Named("httpRequestAccessDecisionManager")
	AccessDecisionManager accessDecisionManager) {
		super.setAccessDecisionManager(accessDecisionManager);
	}
	*/
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		IAppContext appContext = (IAppContext) config.getServletContext().getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
		if(appContext.getSecurityMode() == SecurityMode.ACEGI) {
			setAuthenticationManager(appContext.getAuthenticationManager());
			// setAccessDecisionManager(injector.getInstance(Key.get(AccessDecisionManager.class,
			// Named.class)));
			setAccessDecisionManager(appContext.getHttpRequestAccessDecisionManager());

			String ods = config.getInitParameter("objectDefinitionSource");
			if(ods == null) {
				throw new Error("The init parameter 'objectDefinitionSource' must be declared");
			}
			FilterInvocationDefinitionSourceEditor editor = new FilterInvocationDefinitionSourceEditor();
			editor.setAsText(ods);
			FilterInvocationDefinitionSource fids = (FilterInvocationDefinitionSource) editor.getValue();
			setObjectDefinitionSource(fids);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(securityMode == SecurityMode.ACEGI) {
			super.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
