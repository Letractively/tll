/**
 * The Logic Lab
 */
package com.tll.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.tll.client.ui.SmbizImageBundle;
import com.tll.client.ui.option.Option;

/**
 * App - General app wide utility methods and constants.
 * @author jpk
 */
public abstract class App {

	/**
	 * Use this token to initialize GWT history tracking.
	 */
	public static final String INITIAL_HISTORY_TOKEN = "";

	/**
	 * The app wide image bundle.
	 */
	private static final SmbizImageBundle imageBundle = (SmbizImageBundle) GWT.create(SmbizImageBundle.class);

	/**
	 * App wide constants (based on Constants.properties file).
	 */
	private static final SmbizConstants constants = (SmbizConstants) GWT.create(SmbizConstants.class);

	/**
	 * The global set current account option.
	 */
	public static final Option OPTION_SET_CURRENT = new Option("Set as Current");

	/**
	 * @return the app scoped image bundle instance.
	 */
	public static SmbizImageBundle imgs() {
		return imageBundle;
	}

	/**
	 * @return The app wide constants.
	 */
	public static SmbizConstants constants() {
		return constants;
	}

	/**
	 * Performs initialization stuff that should be invoked immediately in
	 * onModuleLoad()
	 */
	public static void init() {
	 Logger.setUncaughtExceptionHandler();

		// setup history tracking by establishing an initial token name in the
		// history queue
		History.newItem(INITIAL_HISTORY_TOKEN);
	}
}
