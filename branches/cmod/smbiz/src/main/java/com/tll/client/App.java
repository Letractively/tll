/**
 * The Logic Lab
 */
package com.tll.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.tll.client.ui.ImageBundle;
import com.tll.client.ui.MsgImageBundle;

/**
 * App - General app wide utility methods and constants.
 * @author jpk
 */
public abstract class App {

	/**
	 * The app wide image bundle.
	 */
	private static final ImageBundle imageBundle = (ImageBundle) GWT.create(ImageBundle.class);

	/**
	 * @return the app scoped {@link MsgImageBundle} instance.
	 */
	public static ImageBundle imgs() {
		return imageBundle;
	}

	/**
	 * Use this token to initialize GWT history tracking.
	 */
	public static final String INITIAL_HISTORY_TOKEN = "";

	/**
	 * App wide constants (based on Constants.properties file).
	 */
	public static final Constants constants = (Constants) GWT.create(Constants.class);

	/**
	 * Performs initialization stuff that should be invoked immediately in
	 * onModuleLoad()
	 */
	public static void init() {

		Log.setUncaughtExceptionHandler();

		// set the uncaught exception handler
		/*
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

			public void onUncaughtException(final Throwable tracepoint) {
				App.resetBusy();
				performDefaultErrorHandling(tracepoint);
			}
		});
		*/

		// setup history tracking by establishing an initial token name in the
		// history queue
		History.newItem(INITIAL_HISTORY_TOKEN);
	}

	/*
	private void performDefaultErrorHandling(final Throwable caught) {
		if(caught != null) {
			final String stacktrace = getStacktraceAsString(caught);
			Window.alert(stacktrace);
			if(!GWT.isScript()) GWT.log("Error", caught);
		}
		else {
			final String message = "An Error occurred, but we have no further information about the cause.";
			Window.alert(message);
		}
	}

	private static String getStacktraceAsString(final Throwable tracepoint) {
		final StackTraceElement[] trace = tracepoint.getStackTrace();
		final StringBuilder sbuf = new StringBuilder(2048);
		sbuf.append(tracepoint.toString());
		sbuf.append(": at\n");
		// I cut the stacktrace at depth 7
		final int length = Math.min(7, trace.length);
		for(int i = 0; i <= length; i++) {
			sbuf.append(trace[i].toString());
			sbuf.append("\n");
		}
		if(trace.length > 7) {
			sbuf.append("...");
		}
		final String stacktrace = sbuf.toString();
		return stacktrace;
	}
	*/
}
