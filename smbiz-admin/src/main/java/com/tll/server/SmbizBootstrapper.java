/**
 * The Logic Lab
 * @author jpk
 * @since Dec 30, 2010
 */
package com.tll.server;


import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tll.config.Config;
import com.tll.config.ConfigRef;

/**
 * smbiz admin app bootstrapper.
 * @author jpk
 */
public class SmbizBootstrapper extends GuiceServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(SmbizBootstrapper.class);
	
	private transient Config config;

	@Override
	protected Injector getInjector() {
		// load *all* found config properties
		// NOTE: this is presumed to be the first contact point with the config
		// instance!
		if(config != null) throw new IllegalStateException();
		try {
			config = Config.load(new ConfigRef(true));
		}
		catch(final IllegalArgumentException e) {
			throw new Error("Unable to load config: " + e.getMessage(), e);
		}
		log.debug("App config instance loaded");

		log.debug("Creating servlet injector..");
		Injector injector =
				Guice.createInjector(new SmbizDb4oPersistModule(config), new SmbizWebModule(config), new SmbizServletModule(config));
		log.debug("Servlet injector created");
		return injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Initing smbiz app..");
		super.contextInitialized(sce);
		
		Injector injector = (Injector) sce.getServletContext().getAttribute(Injector.class.getName());
		if(injector == null) throw new Error("No di injector found in servlet context");
		
		// instantiate db4o
		log.info("Starting up db4o session..");
		Provider<PlatformTransactionManager> tm = injector.getProvider(PlatformTransactionManager.class);
		tm.get(); // this forces db4o to boot
		EmbeddedObjectContainer oc = injector.getInstance(EmbeddedObjectContainer.class);
		sce.getServletContext().setAttribute(EmbeddedObjectContainer.class.getName(), oc);
		log.info("Db4o session started");
		
		// create app and persist contexts
		AppContext ac = injector.getInstance(AppContext.class);
		assert ac != null;
		sce.getServletContext().setAttribute(AppContext.KEY, ac);
		PersistContext pc = injector.getInstance(PersistContext.class);
		assert pc != null;
		sce.getServletContext().setAttribute(PersistContext.KEY, pc);
		
		config = null;	// no longer need this ref!
		
		log.info("Smbiz app initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("shutting down smbiz app..");
		
		// db4o shutdown
		log.info("Shutting down db4o session..");
		EmbeddedObjectContainer oc = (EmbeddedObjectContainer) sce.getServletContext().getAttribute(EmbeddedObjectContainer.class.getName());
		if(oc != null) {
			oc.close();
			log.info("Db4o session closed");
		}
		else {
			log.info("No Db4o session found in servlet context");
		}
		
		super.contextDestroyed(sce);
		
		log.info("smbiz app shutdown");
	}
}
