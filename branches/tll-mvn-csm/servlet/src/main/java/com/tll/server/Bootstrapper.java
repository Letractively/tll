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

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.util.EnumUtil;

/**
 * Bootstrapper - Bootstraps the app via dependency injection.
 * @author jpk
 */
public final class Bootstrapper implements ServletContextListener {

	/**
	 * The servlet context param name identifying the dependency injection modules
	 * to load.
	 */
	static final String DEPENDENCY_MODULE_CLASS_NAMES = "di.module.classnames";

	/**
	 * Factory method encapsulating the construction an {@link AppContext}
	 * instance from an {@link Injector}.
	 * @param servletContext The servlet context.
	 * @return Newly created {@link AppContext} instance.
	 */
	public static AppContext generateAppContext(ServletContext servletContext) {
		final boolean debug = Config.instance().getBoolean(ConfigKeys.DEBUG_PARAM.getKey());
		final String environment = Config.instance().getString(ConfigKeys.ENVIRONMENT_PARAM.getKey());
		final SecurityMode securityMode = EnumUtil.fromString(SecurityMode.class, Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));
		final DaoMode daoMode =
				EnumUtil.fromString(DaoMode.class, Config.instance().getString(ConfigKeys.DAO_MODE_PARAM.getKey()));

		// create the di injector
		Injector injector = createInjector(servletContext, debug ? Stage.DEVELOPMENT : Stage.PRODUCTION);
		
		final AuthenticationManager authenticationManager = injector.getInstance(AuthenticationManager.class);

		// @Named("httpRequestAccessDecisionManager")
		// TODO figure out how to extract this from the injector
		final AccessDecisionManager httpRequesetAccessDecisionManager = null;

		final RefData refdata = injector.getInstance(RefData.class);
		final MailManager mailManager = injector.getInstance(MailManager.class);
		final Marshaler marshaler = injector.getInstance(Marshaler.class);
		final EntityManagerFactory entityManagerFactory = injector.getInstance(EntityManagerFactory.class);
		final IEntityFactory entityFactory = injector.getInstance(IEntityFactory.class);
		final IEntityServiceFactory entityServiceFactory = injector.getInstance(IEntityServiceFactory.class);
		
		return
				new AppContext(debug, environment, securityMode, authenticationManager, httpRequesetAccessDecisionManager,
						refdata, mailManager, marshaler, daoMode, entityManagerFactory, entityFactory, entityServiceFactory);
	}

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

	private static final Log log = LogFactory.getLog(Bootstrapper.class);

	public void contextInitialized(ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();

		if(servletContext.getAttribute(IAppContext.SERVLET_CONTEXT_KEY) != null) {
			throw new Error("App already bootstrapped.");
		}

		servletContext.setAttribute(IAppContext.SERVLET_CONTEXT_KEY, generateAppContext(servletContext));
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		IAppContext ac =
				(IAppContext) servletContextEvent.getServletContext().getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
		if(ac != null) {
			try {
				final EntityManagerFactory emf = ac.getEntityManagerFactory();
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
				log.error(e.getMessage(), e);
			}
		}
	}
}
