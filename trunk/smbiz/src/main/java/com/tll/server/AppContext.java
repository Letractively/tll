/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import com.tll.service.entity.account.AddAccountService;

/**
 * AppContext
 * @author jpk
 */
public class AppContext {

	/**
	 * The key identifying the sole {@link AppContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = AppContext.class.getName();

	private final boolean debug;
	private final String environment;
	private final String dfltUserEmail;
	private final AddAccountService addAccountService;

	/**
	 * Constructor
	 * @param debug
	 * @param environment
	 * @param dfltUserEmail
	 * @param addAccountService
	 */
	public AppContext(boolean debug, String environment, String dfltUserEmail, AddAccountService addAccountService) {
		super();
		this.debug = debug;
		this.environment = environment;
		this.dfltUserEmail = dfltUserEmail;
		this.addAccountService = addAccountService;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @return the dfltUserEmail
	 */
	public String getDfltUserEmail() {
		return dfltUserEmail;
	}

	/**
	 * @return the addAccountService
	 */
	public AddAccountService getAddAccountService() {
		return addAccountService;
	}
}
