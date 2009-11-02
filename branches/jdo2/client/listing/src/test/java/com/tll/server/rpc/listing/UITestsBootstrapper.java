/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.server.rpc.listing;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectContainer;
import com.google.inject.Injector;
import com.tll.dao.IDbShell;
import com.tll.dao.db4o.Db4oDbShell;


/**
 * UITestsBootstrapper
 * @author jpk
 */
public class UITestsBootstrapper extends ListingContextBootstrapper {

	private static final Log log = LogFactory.getLog(UITestsBootstrapper.class);

	private static final String KEY = Integer.toString(UITestsBootstrapper.class.getName().hashCode());

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		super.startup(injector, servletContext);

		// retain the db4o's object container ref
		final Object oc = injector.getInstance(ObjectContainer.class);
		if(oc == null) throw new Error("Unable to obtain db4o's object container");
		servletContext.setAttribute(KEY, oc);

		// re-stub the db
		log.info("re-stubbing test db..");
		final ObjectContainer dbref = injector.getInstance(ObjectContainer.class);
		final IDbShell dbs = injector.getInstance(IDbShell.class);
		((Db4oDbShell)dbs).restub(dbref);
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		super.shutdown(servletContext);
		// kill the db
		final ObjectContainer oc = (ObjectContainer) servletContext.getAttribute(KEY);
		if(oc != null) {
			log.debug("Shutting down db4o..");
			oc.close();
		}

	}

}
