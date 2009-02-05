/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.util.EnumUtil;

/**
 * AppContextBootstrapper
 * @author jpk
 */
public class AppContextHandler implements IBootstrapHandler {
	
	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		ENVIRONMENT_PARAM("environment"),
		DEBUG_PARAM("debug"),
		DAO_MODE_PARAM("db.dao.mode");

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
	
	private static final Log log = LogFactory.getLog(AppContextHandler.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		final boolean debug = Config.instance().getBoolean(ConfigKeys.DEBUG_PARAM.getKey());
		final String environment = Config.instance().getString(ConfigKeys.ENVIRONMENT_PARAM.getKey());

		final DaoMode daoMode =
				EnumUtil.fromString(DaoMode.class, Config.instance().getString(ConfigKeys.DAO_MODE_PARAM.getKey()));

		final RefData refdata = injector.getInstance(RefData.class);
		final MailManager mailManager = injector.getInstance(MailManager.class);
		final Marshaler marshaler = injector.getInstance(Marshaler.class);
		final EntityManagerFactory entityManagerFactory = injector.getInstance(EntityManagerFactory.class);
		final IEntityFactory entityFactory = injector.getInstance(IEntityFactory.class);
		final IEntityServiceFactory entityServiceFactory = injector.getInstance(IEntityServiceFactory.class);

		AppContext c =
				new AppContext(debug, environment, refdata, mailManager, marshaler, daoMode, entityManagerFactory,
						entityFactory, entityServiceFactory);

		servletContext.setAttribute(IAppContext.SERVLET_CONTEXT_KEY, c);
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		IAppContext ac =
				(IAppContext) servletContext.getAttribute(IAppContext.SERVLET_CONTEXT_KEY);
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
