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

	public static final String DEFAULT_STAGE = "prod"; // production

	public static final String DEFAULT_ENVIRONMENT = "UNSPECIFIED";

	/**
	 * The key identifying the sole {@link AppContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = AppContext.class.getName();

	private final String stage;
	private final String environment;
	private final String dfltUserEmail;
	private final AddAccountService addAccountService;

	/**
	 * Constructor
	 * @param stage
	 * @param environment
	 * @param dfltUserEmail
	 * @param addAccountService
	 */
	public AppContext(String stage, String environment, String dfltUserEmail, AddAccountService addAccountService) {
		super();
		this.stage = stage;
		this.environment = environment;
		this.dfltUserEmail = dfltUserEmail;
		this.addAccountService = addAccountService;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return "dev".equals(stage);
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
