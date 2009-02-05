/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;

import com.google.inject.Injector;
import com.tll.config.Config;
import com.tll.di.SecurityModule.ConfigKeys;
import com.tll.util.EnumUtil;

/**
 * AppContextBootstrapper
 * @author jpk
 */
public class SecurityContextHandler implements IBootstrapHandler {
	
	private static final Log log = LogFactory.getLog(SecurityContextHandler.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		final SecurityMode securityMode =
				EnumUtil.fromString(SecurityMode.class, Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));

		final AuthenticationManager authenticationManager = injector.getInstance(AuthenticationManager.class);

		// @Named("httpRequestAccessDecisionManager")
		// TODO figure out how to extract this from the injector
		final AccessDecisionManager httpRequesetAccessDecisionManager = null;

		ISecurityContext c = new SecurityContext(securityMode, authenticationManager, httpRequesetAccessDecisionManager);

		log.info("Setting security context");
		servletContext.setAttribute(ISecurityContext.SERVLET_CONTEXT_KEY, c);
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}
}
