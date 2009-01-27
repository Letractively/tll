/**
 * The Logic Lab
 */
package com.tll.server;

/**
 * ValidationUtils
 * @author jpk
 */
public abstract class Constants {

	// servlet context param names
	public static final String LOG4J_CONFIG_LOCATION = "log4jConfigLocation";
	public static final String GUICE_MODULE_CLASS_NAMES = "guice.module.classnames";

	// servlet context attribute names
	public static final String GUICE_INJECTOR_CONTEXT_ATTRIBUTE = "guice.injector";
	public static final String IS_DEBUG_CONTEXT_ATTRIBUTE = "debug";
	public static final String ENVIRONMENT_CONTEXT_ATTRIBUTE = "environment";

	// session attribute names (SA_)
	public static final String SA_CACHE_MANAGER = "cm";
	public static final String SA_ADMIN_CONTEXT = "ac";
}
