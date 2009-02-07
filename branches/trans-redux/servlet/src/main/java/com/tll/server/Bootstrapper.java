/**
 * The Logic Lab
 */
package com.tll.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * Bootstrapper - Bootstraps the app via dependency injection.
 * @author jpk
 */
public final class Bootstrapper implements ServletContextListener {

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DEBUG_PARAM("debug");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	private static final Log log = LogFactory.getLog(Bootstrapper.class);

	/**
	 * The servlet context param name identifying the dependency injection modules
	 * to load.
	 */
	static final String DEPENDENCY_MODULE_CLASS_NAMES = "di.modules";

	/**
	 * The servlet context param name identifying the dependency injection
	 * handlers.
	 */
	static final String DEPENDENCY_HANDLER_CLASS_NAMES = "do.handlers";

	/**
	 * Creates a dependency injector from the {@link ServletContext}'s init
	 * params.
	 * @param context The servlet context.
	 * @return new dependency injector instance
	 */
	private static Injector createInjector(ServletContext context, Stage stage) {
		final String[] moduleClassNames = StringUtils.split(context.getInitParameter(DEPENDENCY_MODULE_CLASS_NAMES));
		if(moduleClassNames == null || moduleClassNames.length < 1) {
			throw new Error("No bootstrap module class names declared.");
		}

		final List<Module> modules = new ArrayList<Module>(moduleClassNames.length);
		for(final String mcn : moduleClassNames) {
			try {
				modules.add((Module) Class.forName(mcn, true, Bootstrapper.class.getClassLoader()).newInstance());
			}
			catch(final ClassNotFoundException e) {
				throw new Error("Module class: " + mcn + " not found.");
			}
			catch(final InstantiationException e) {
				throw new Error("Unable to instantiate module class: " + mcn);
			}
			catch(final IllegalAccessException e) {
				throw new Error("Unable to access module class: " + mcn);
			}
		}

		if(log.isDebugEnabled()) {
			log.debug("Creating " + stage.toString() + " bootstrap di injector...");
		}
		return Guice.createInjector(stage, modules);
	}

	/**
	 * Creates a dependency injector from the {@link ServletContext}'s init
	 * params.
	 * @param context The servlet context.
	 */
	private void loadDependencyHandlers(ServletContext context) {
		final String[] handlerClassNames = StringUtils.split(context.getInitParameter(DEPENDENCY_HANDLER_CLASS_NAMES));
		if(handlerClassNames == null || handlerClassNames.length < 1) {
			throw new Error("No bootstrap handlers declared.");
		}
		handlers = new ArrayList<IBootstrapHandler>(handlerClassNames.length);
		for(final String hcn : handlerClassNames) {
			try {
				handlers.add((IBootstrapHandler) Class.forName(hcn).newInstance());
			}
			catch(final ClassNotFoundException e) {
				throw new Error("Handler class: " + hcn + " not found.");
			}
			catch(final InstantiationException e) {
				throw new Error("Unable to instantiate handler class: " + hcn);
			}
			catch(final IllegalAccessException e) {
				throw new Error("Unable to access handler class: " + hcn);
			}
		}
	}

	private List<IBootstrapHandler> handlers;

	public void contextInitialized(ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();

		final boolean debug = Config.instance().getBoolean(ConfigKeys.DEBUG_PARAM.getKey());

		// create the dependency injector
		final Injector injector = createInjector(servletContext, debug ? Stage.DEVELOPMENT : Stage.PRODUCTION);

		// load the dependency handler definitions
		loadDependencyHandlers(servletContext);

		for(IBootstrapHandler handler : handlers) {
			handler.startup(injector, servletContext);
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		for(IBootstrapHandler handler : handlers) {
			handler.shutdown(servletContextEvent.getServletContext());
		}
	}
}
