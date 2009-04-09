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
import com.tll.di.AcegiModule;

/**
 * SecurityContextBootstrapper - Sets the {@link SecurityContext} in the
 * {@link ServletContext}.
 * @author jpk
 */
public class SecurityContextBootstrapper implements IBootstrapHandler {

	private static final Log log = LogFactory.getLog(SecurityContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext, Config config) {
		AuthenticationManager authenticationManager = null;
		AccessDecisionManager httpRequesetAccessDecisionManager = null;

		authenticationManager = injector.getInstance(AuthenticationManager.class);
		httpRequesetAccessDecisionManager =
			injector.getInstance(Key.get(AccessDecisionManager.class, Names.named(AcegiModule.ADM_HTTP_REQUEST)));

		log.debug("Setting security context..");
		servletContext.setAttribute(SecurityContext.KEY, new SecurityContext(authenticationManager,
				httpRequesetAccessDecisionManager));
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}
}
