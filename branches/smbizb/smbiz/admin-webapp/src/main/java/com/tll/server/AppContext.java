/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

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

	/**
	 * Constructor
	 * @param debug
	 * @param environment
	 * @param dfltUserEmail
	 */
	public AppContext(boolean debug, String environment, String dfltUserEmail) {
		super();
		this.debug = debug;
		this.environment = environment;
		this.dfltUserEmail = dfltUserEmail;
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
}
