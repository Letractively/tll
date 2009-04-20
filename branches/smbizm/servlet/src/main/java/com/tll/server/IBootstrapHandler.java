/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import com.google.inject.Injector;
import com.tll.config.Config;

/**
 * IBootstrapHandler - Invoked by the {@link Bootstrapper},
 * {@link IBootstrapHandler}s are responsible for initializing the servlet
 * context.
 * @author jpk
 */
public interface IBootstrapHandler {

	/**
	 * Event hook signaling app startup (the bootstrap phase).
	 * @param injector The assembled app scoped dependency injector that provides
	 *        all app dependencies.
	 * @param servletContext The servlet context used to store app scoped
	 *        artifacts.
	 * @param config The employed {@link Config} instance.
	 */
	void startup(Injector injector, ServletContext servletContext, Config config);

	/**
	 * Event hook signaling app shutdown.
	 * @param servletContext The servlet context.
	 */
	void shutdown(ServletContext servletContext);
}
