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
	public static final String SERVLET_CONTEXT_KEY = AppContext.class.getName();

	private final boolean debug;
	private final String environment;

	/**
	 * Constructor
	 * @param debug
	 * @param environment
	 */
	public AppContext(boolean debug, String environment) {
		super();
		this.debug = debug;
		this.environment = environment;
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
}
