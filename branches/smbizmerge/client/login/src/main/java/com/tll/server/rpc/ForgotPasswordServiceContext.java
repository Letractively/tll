/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server.rpc;

import com.google.inject.Inject;
import com.tll.mail.MailManager;
import com.tll.server.IExceptionHandler;
import com.tll.service.IForgotPasswordHandler;

/**
 * Encapsulates needed objects for handling forgot password requests.
 * @author jpk
 */
public class ForgotPasswordServiceContext {
	
	public static final String KEY = ForgotPasswordServiceContext.class.getName();

	private final IForgotPasswordHandler handler;
	private final MailManager mailManager;
	private final IExceptionHandler exceptionHandler;

	/**
	 * Constructor
	 * @param handler
	 * @param mailManager
	 * @param exceptionHandler
	 */
	@Inject
	public ForgotPasswordServiceContext(IForgotPasswordHandler handler, MailManager mailManager,
			IExceptionHandler exceptionHandler) {
		super();
		this.handler = handler;
		this.mailManager = mailManager;
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * @return the userService
	 */
	public IForgotPasswordHandler getForgotPasswordHandler() {
		return handler;
	}

	/**
	 * @return the mailManager
	 */
	public MailManager getMailManager() {
		return mailManager;
	}

	/**
	 * @return the exceptionHandler
	 */
	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}
