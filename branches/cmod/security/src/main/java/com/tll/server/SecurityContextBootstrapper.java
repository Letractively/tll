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
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tll.config.Config;
import com.tll.di.SecurityModule.ConfigKeys;
import com.tll.util.EnumUtil;

/**
 * SecurityContextHandler - Sets the {@link ISecurityContext} in the
 * {@link ServletContext}.
 * @author jpk
 */
public class SecurityContextBootstrapper implements IBootstrapHandler {
	
	private static final Log log = LogFactory.getLog(SecurityContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		final SecurityMode securityMode =
				EnumUtil.fromString(SecurityMode.class, Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));

		AuthenticationManager authenticationManager = null;
		AccessDecisionManager httpRequesetAccessDecisionManager = null;

		if(securityMode == SecurityMode.ACEGI) {
			authenticationManager = injector.getInstance(AuthenticationManager.class);
			httpRequesetAccessDecisionManager =
					injector.getInstance(Key.get(AccessDecisionManager.class, Names.named("httpRequestAccessDecisionManager")));
		}

		log.info("Setting security context (SecurityMode: " + securityMode + ")..");
		servletContext.setAttribute(ISecurityContext.SERVLET_CONTEXT_KEY, new SecurityContext(securityMode,
				authenticationManager, httpRequesetAccessDecisionManager));
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}
}
