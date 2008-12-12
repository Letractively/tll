/**
 * The Logic Lab
 */
package com.tll.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
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
import com.tll.config.ConfigKeys;

/**
 * GuiceBootstrapper
 * @author jpk
 */
public final class GuiceBootstrapper implements ServletContextListener {

	private static final Log log = LogFactory.getLog(GuiceBootstrapper.class);
	private Injector injector;

	public void contextInitialized(ServletContextEvent event) {
		final ServletContext context = event.getServletContext();

		if(context.getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE) != null) {
			throw new IllegalStateException("Context already initialized");
		}

		// debug
		final boolean isDebug = Config.instance().getBoolean(ConfigKeys.DEBUG_PARAM.getKey());
		context.setAttribute(Constants.IS_DEBUG_CONTEXT_ATTRIBUTE, new Boolean(isDebug));

		// environment
		final String env = Config.instance().getString(ConfigKeys.ENVIRONMENT_PARAM.getKey());
		context.setAttribute(Constants.ENVIRONMENT_CONTEXT_ATTRIBUTE, env);

		// injector
		injector = createGuiceInjector(context, isDebug ? Stage.DEVELOPMENT : Stage.PRODUCTION);
		context.setAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE, injector);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		if(injector != null) {
			try {
				final EntityManagerFactory emf = injector.getInstance(EntityManagerFactory.class);
				if(emf != null) {
					log.debug("Closing EntityManagerFactory ...");
					emf.close();
				}
			}
			catch(final RuntimeException e) {
				// Guice will throw a ConfigurationException when there is no binding
				// for the class specified in Injector.getInstance()
				// so we keep it silent (NOTE: ConfigurationException is only package
				// visible so we have to resort to its superclass: RuntimeException)
			}

			log.debug("Nullifying Guice injector...");
			injector = null;
		}
	}

	private Injector createGuiceInjector(ServletContext context, Stage stage) {

		final String[] moduleClassNames = StringUtils.split(context.getInitParameter(Constants.GUICE_MODULE_CLASS_NAMES));
		if(moduleClassNames == null || moduleClassNames.length < 1) {
			throw new Error("No Guice module class names declared.");
		}

		final List<Module> modules = new ArrayList<Module>(moduleClassNames.length);
		for(final String mcn : moduleClassNames) {
			try {
				modules.add((Module) Class.forName(mcn, true, this.getClass().getClassLoader()).newInstance());
			}
			catch(final ClassNotFoundException e) {
				throw new Error("Guice module class: " + mcn + " not found.");
			}
			catch(final InstantiationException e) {
				throw new Error("Unable to instantiate Guice module class: " + mcn);
			}
			catch(final IllegalAccessException e) {
				throw new Error("Unable to access Guice module class: " + mcn);
			}
		}

		if(log.isDebugEnabled()) {
			log.debug("Creating " + stage.toString() + " Guice injector...");
		}
		return Guice.createInjector(stage, modules);
	}

}
