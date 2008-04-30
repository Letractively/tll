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

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSourceEditor;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.server.Constants;
import com.tll.server.SecurityMode;

/**
 * FilterSecurityInterceptor
 * @author jpk
 */
public final class FilterSecurityInterceptor extends org.acegisecurity.intercept.web.FilterSecurityInterceptor {

	private boolean isAcegi;

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

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

		isAcegi = SecurityMode.ACEGI.name().equals(Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM));
		if(isAcegi) {
			Injector injector =
					(Injector) config.getServletContext().getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE);
			// setAuthenticationManager(injector.getInstance(AuthenticationManager.class));
			// setAccessDecisionManager(injector.getInstance(Key.get(AccessDecisionManager.class,
			// Named.class)));
			injector.injectMembers(this);

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
		if(isAcegi) {
			super.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
