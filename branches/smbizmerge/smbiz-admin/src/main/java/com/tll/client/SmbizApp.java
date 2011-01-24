/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author jpk
 */
public class SmbizApp {

	private static final Logger log = Logger.getLogger(SmbizShell.class.getName());
	
	private final SmbizShell shell;

	/**
	 * Constructor
	 */
	public SmbizApp(SmbizShell shell) {
		this.shell = shell;
	}

	public void run(HasWidgets root) {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException(Throwable e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		});

		IClientFactory clientFactory = GWT.create(IClientFactory.class);
		SmbizActivityMapper activityMapper = new SmbizActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, clientFactory.getEventBus());
		activityManager.setDisplay(shell.getPanel());
		
		root.add(shell);
		clientFactory.getPlaceHistoryController().handleCurrentHistory();
	}
}
