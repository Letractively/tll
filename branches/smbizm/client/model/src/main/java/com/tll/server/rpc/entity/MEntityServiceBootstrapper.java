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
import com.tll.mail.MailManager;
import com.tll.mail.NameEmail;
import com.tll.model.IEntityAssembler;
import com.tll.refdata.RefData;
import com.tll.server.IBootstrapHandler;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.ExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;

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

		MENTITY_SERVICE_IMPL_RESOLVER_CLASSNAME("server.mEntityServiceImplResolver.classname"),
		NAMED_QUERY_RESOLVER_CLASSNAME("server.namedQueryResolver.classname"),
		ONERROR_SEND_EMAIL("mail.onerror.ToAddress"),
		ONERROR_SEND_NAME("mail.onerror.ToName");

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
	public void startup(Injector injector, ServletContext servletContext, Config config) {
		log.info("Bootstrapping MEntity services..");
		final RefData refdata = injector.getInstance(RefData.class);

		// the mail manager is optional
		MailManager mailManager;
		try {
			mailManager = injector.getInstance(MailManager.class);
		}
		catch(final Exception e) {
			mailManager = null;
			log.warn("No mail manager will be employed.");
		}

		final Marshaler marshaler = injector.getInstance(Marshaler.class);

		// NOTE: we support the case where an em factory is not provided
		EntityManagerFactory entityManagerFactory;
		try {
			entityManagerFactory = injector.getInstance(EntityManagerFactory.class);
		}
		catch(final Exception e) {
			entityManagerFactory = null;
		}

		final String onErrorName = config.getString(ConfigKeys.ONERROR_SEND_NAME.getKey());
		final String onErrorEmail = config.getString(ConfigKeys.ONERROR_SEND_EMAIL.getKey());
		final NameEmail email = new NameEmail(onErrorName, onErrorEmail);
		final ExceptionHandler exceptionHandler = new ExceptionHandler(mailManager, email);
		final IEntityAssembler entityAssembler = injector.getInstance(IEntityAssembler.class);
		final IEntityServiceFactory entityServiceFactory = injector.getInstance(IEntityServiceFactory.class);

		String cn;

		IMEntityServiceImplResolver serviceResolver;
		final INamedQueryResolver namedQueryResolver;
		try {
			// instantiate the mentity service resolver
			cn = config.getString(ConfigKeys.MENTITY_SERVICE_IMPL_RESOLVER_CLASSNAME.getKey());
			serviceResolver = (IMEntityServiceImplResolver) Class.forName(cn).newInstance();

			// instantiate the named query resolver
			cn = config.getString(ConfigKeys.NAMED_QUERY_RESOLVER_CLASSNAME.getKey());
			namedQueryResolver = (INamedQueryResolver) Class.forName(cn).newInstance();

		}
		catch(final InstantiationException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(final IllegalAccessException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(final ClassNotFoundException e) {
			throw new Error(e.getMessage(), e);
		}

		// create and store the sole context
		final MEntityContext context =
			new MEntityContext(refdata, mailManager, marshaler, entityManagerFactory, entityAssembler,
						entityServiceFactory,
					namedQueryResolver, exceptionHandler);
		servletContext.setAttribute(MEntityContext.KEY, context);

		// create and store the sole delegate
		final MEntityServiceDelegate delegate = new MEntityServiceDelegate(context, serviceResolver);
		servletContext.setAttribute(MEntityServiceDelegate.KEY, delegate);

		log.info("MEntity services bootstrapped.");
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		final MEntityContext c =
			(MEntityContext) servletContext.getAttribute(MEntityContext.KEY);
		if(c != null) {
			final EntityManagerFactory emf = c.getEntityManagerFactory();
			if(emf != null) {
				log.debug("Closing EntityManagerFactory ...");
				emf.close();
			}
		}
	}

}
