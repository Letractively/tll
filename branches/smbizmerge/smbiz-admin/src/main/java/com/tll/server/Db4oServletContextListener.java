package com.tll.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Manages the web-app based db4o session life-cycle.
 * @author jpk
 */
public class Db4oServletContextListener implements ServletContextListener {

	private static final Log log = LogFactory.getLog(Db4oServletContextListener.class);
	
	static final String KEY = Db4oServletContextListener.class.getName();

	@Inject
	Provider<PlatformTransactionManager> tm;

	@Inject
	EmbeddedObjectContainer oc;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Starting up db4o session..");
		// instantiate db4o (by forcing instantiation of the trans manager)
		tm.get();
		sce.getServletContext().setAttribute(KEY, oc);
		log.info("Db4o session started");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("Shutting down db4o session..");
		oc.close();
		log.info("Db4o session closed");
	}

}
