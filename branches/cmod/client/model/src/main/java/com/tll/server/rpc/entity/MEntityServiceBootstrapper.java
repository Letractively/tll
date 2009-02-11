/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.rpc.entity;

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
import com.tll.server.IBootstrapHandler;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.ExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.util.EnumUtil;

/**
 * MEntityServiceBootstrapper
 * @author jpk
 */
public class MEntityServiceBootstrapper implements IBootstrapHandler {

	/**
	 * ConfigKeys - Configuration property keys for the app context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DAO_MODE_PARAM("db.dao.mode"),
		MENTITY_SERVICE_IMPL_RESOLVER_CLASSNAME("mEntityServiceImplResolverClassName"),
		NAMED_QUERY_RESOLVER_CLASSNAME("namedQueryResolverClassName");

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
	
	private static final Log log = LogFactory.getLog(MEntityServiceBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		final DaoMode daoMode =
				EnumUtil.fromString(DaoMode.class, Config.instance().getString(ConfigKeys.DAO_MODE_PARAM.getKey()));

		final RefData refdata = injector.getInstance(RefData.class);
		final MailManager mailManager = injector.getInstance(MailManager.class);
		
		final Marshaler marshaler = injector.getInstance(Marshaler.class);
		final EntityManagerFactory entityManagerFactory =
				daoMode == DaoMode.ORM ? injector.getInstance(EntityManagerFactory.class) : null;
		final IEntityFactory entityFactory = injector.getInstance(IEntityFactory.class);
		final IEntityServiceFactory entityServiceFactory = injector.getInstance(IEntityServiceFactory.class);
		final ExceptionHandler exceptionHandler = injector.getInstance(ExceptionHandler.class);
		
		String cn;

		IMEntityServiceImplResolver sr;
		final INamedQueryResolver nqr;
		try {
			// instantiate the mentity service resolver
			cn = Config.instance().getString(ConfigKeys.MENTITY_SERVICE_IMPL_RESOLVER_CLASSNAME.getKey());
			sr = (IMEntityServiceImplResolver) Class.forName(cn).newInstance();

			// instantiate the named query resolver
			cn = Config.instance().getString(ConfigKeys.NAMED_QUERY_RESOLVER_CLASSNAME.getKey());
			nqr = (INamedQueryResolver) Class.forName(cn).newInstance();

		}
		catch(InstantiationException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(IllegalAccessException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(ClassNotFoundException e) {
			throw new Error(e.getMessage(), e);
		}

		servletContext.setAttribute(IMEntityServiceContext.SERVLET_CONTEXT_KEY, new MEntityServiceContext(refdata,
				mailManager, marshaler, entityManagerFactory, entityFactory, entityServiceFactory, sr, nqr, exceptionHandler));
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		IMEntityServiceContext c =
				(IMEntityServiceContext) servletContext.getAttribute(IMEntityServiceContext.SERVLET_CONTEXT_KEY);
		if(c != null) {
			final EntityManagerFactory emf = c.getEntityManagerFactory();
			if(emf != null) {
				log.debug("Closing EntityManagerFactory ...");
				emf.close();
			}
		}
	}

}
