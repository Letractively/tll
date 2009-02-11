/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.rpc;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;
import com.tll.config.IConfigKey;
import com.tll.mail.MailManager;
import com.tll.server.IBootstrapHandler;
import com.tll.service.IForgotPasswordHandler;

/**
 * MEntityServiceBootstrapper
 * @author jpk
 */
public class ForgotPasswordServiceBootstrapper implements IBootstrapHandler {

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
	
	private static final Log log = LogFactory.getLog(ForgotPasswordServiceBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		log.debug("Creating the ForgotPasswordServiceContext...");
		final MailManager mailManager = injector.getInstance(MailManager.class);
		final ExceptionHandler exceptionHandler = injector.getInstance(ExceptionHandler.class);
		final IForgotPasswordHandler handler = injector.getInstance(IForgotPasswordHandler.class);
		servletContext.setAttribute(ForgotPasswordServiceContext.SERVLET_CONTEXT_KEY, new ForgotPasswordServiceContext(
				handler, mailManager, exceptionHandler));
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}

}
