/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.rpc;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.tll.mail.MailManager;
import com.tll.server.IBootstrapHandler;
import com.tll.server.IExceptionHandler;
import com.tll.service.IForgotPasswordHandler;

/**
 * ForgotPasswordServiceBootstrapper
 * @author jpk
 */
public class ForgotPasswordServiceBootstrapper implements IBootstrapHandler {

	private static final Logger log = LoggerFactory.getLogger(ForgotPasswordServiceBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		log.debug("Creating the ForgotPasswordServiceContext...");
		final MailManager mailManager = injector.getInstance(MailManager.class);
		final IExceptionHandler exceptionHandler = injector.getInstance(IExceptionHandler.class);
		final IForgotPasswordHandler handler = injector.getInstance(IForgotPasswordHandler.class);
		servletContext.setAttribute(ForgotPasswordServiceContext.KEY, new ForgotPasswordServiceContext(
				handler, mailManager, exceptionHandler));
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}

}
