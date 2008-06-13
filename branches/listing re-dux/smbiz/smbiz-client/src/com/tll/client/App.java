/**
 * The Logic Lab
 */
package com.tll.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.ui.GlassPanel;
import com.tll.client.ui.ImageBundle;
import com.tll.client.ui.ThrobbingPanel;

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
	 * The single glass panel that is shared among the client widgets.
	 */
	private static final GlassPanel theGlassPanel = new GlassPanel(false, null, 20, new ThrobbingPanel());

	/**
	 * The global counter used to decide on whether to show/hide the glass panel.
	 */
	private static int glassPanelCounter = 0;

	/**
	 * @return the app scoped {@link ImageBundle} instance.
	 */
	public static ImageBundle imgs() {
		return imageBundle;
	}

	public static void busy() {
		if(glassPanelCounter++ == 0) {
			RootPanel.get().add(theGlassPanel, 0, 0);
		}
	}

	public static void unbusy() {
		if(--glassPanelCounter == 0) {
			theGlassPanel.removeFromParent();
		}
	}

	public static void resetBusy() {
		glassPanelCounter = 0;
		theGlassPanel.removeFromParent();
	}

	public static void darkenGlassPanel() {
		theGlassPanel.setOpacity(60);
	}

	public static void lightenGlassPanel() {
		theGlassPanel.setOpacity(20);
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
	 * Overrides the {@link GWT#getModuleBaseURL()} behavior so RPC service entry points URL
	 * calculations may remain agnostic to script/host mode. <br>
	 * GWT.getModuleBaseURL() doesn't cut it when in host mode.
	 * @return The base URL of the app
	 */
	public static String getBaseUrl() {
		if(GWT.isScript()) {
			return GWT.getModuleBaseURL();
		}
		return "/";
	}

	/**
	 * Performs initialization stuff that should be invoked immediately in onModuleLoad()
	 */
	public static void init() {

		// set the uncaught exception handler
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

			public void onUncaughtException(final Throwable tracepoint) {
				App.resetBusy();
				performDefaultErrorHandling(tracepoint);
			}
		});

		// setup history tracking by establishing an initial token name in the
		// history queue
		History.newItem(INITIAL_HISTORY_TOKEN);
	}

	public static void performDefaultErrorHandling(final Throwable caught) {
		if(caught != null) {
			final String stacktrace = getStacktraceAsString(caught);
			Window.alert(stacktrace);
		}
		else {
			final String message = "An Error occurred, but we have no further information about the cause.";
			Window.alert(message);
		}
	}

	public static String getStacktraceAsString(final Throwable tracepoint) {
		final StackTraceElement[] trace = tracepoint.getStackTrace();
		final StringBuffer sbuf = new StringBuffer(2048);
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

}
