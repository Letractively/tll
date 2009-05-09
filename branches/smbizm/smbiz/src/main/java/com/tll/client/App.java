/**
 * The Logic Lab
 */
package com.tll.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.tll.client.ui.ImageBundle;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;

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
	private static final ImageBundle imageBundle = (ImageBundle) GWT.create(ImageBundle.class);

	/**
	 * App wide constants (based on Constants.properties file).
	 */
	private static final Constants constants = (Constants) GWT.create(Constants.class);

	/**
	 * App wide global msg panel.
	 */
	private static final GlobalMsgPanel gmp = new GlobalMsgPanel();

	/**
	 * @return the app scoped image bundle instance.
	 */
	public static ImageBundle imgs() {
		return imageBundle;
	}

	/**
	 * @return The app wide constants.
	 */
	public static Constants constants() {
		return constants;
	}

	/**
	 * @return The global message panel.
	 */
	public static GlobalMsgPanel getGlobalMsgPanel() {
		return gmp;
	}

	/**
	 * Performs initialization stuff that should be invoked immediately in
	 * onModuleLoad()
	 */
	public static void init() {
		Log.setUncaughtExceptionHandler();

		// setup history tracking by establishing an initial token name in the
		// history queue
		History.newItem(INITIAL_HISTORY_TOKEN);
	}

	/**
	 * Translates a generic {@link IEntityType} to the app specific enum
	 * equivalent.
	 * @param et the generic entity type
	 * @return the smbiz specific enum entity type equivalent
	 */
	public static SmbizEntityType smbizEntityType(IEntityType et) {
		return IEntityType.Util.toEnum(SmbizEntityType.class, et);
	}
}
