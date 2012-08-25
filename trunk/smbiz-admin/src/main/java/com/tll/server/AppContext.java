/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

/**
 * App scoped (servlet context) data object.
 * @author jpk
 */
public class AppContext {

	public static final String DEFAULT_STAGE = "dev"; 	
		// dev (development), prod (production)

	public static final String DEFAULT_ENVIRONMENT = "UNSPECIFIED";

	/**
	 * The key identifying the sole {@link AppContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = AppContext.class.getName();

	private final String stage;
	private final String environment;
	private final String dfltUserEmail;

	/**
	 * Constructor
	 * @param stage
	 * @param environment
	 * @param dfltUserEmail
	 */
	public AppContext(String stage, String environment, String dfltUserEmail) {
		super();
		this.stage = stage;
		this.environment = environment;
		this.dfltUserEmail = dfltUserEmail;
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
}
