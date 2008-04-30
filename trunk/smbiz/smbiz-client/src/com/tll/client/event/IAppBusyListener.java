/**
 * The Logic Lab
 * @author jpk
 * Dec 15, 2007
 */
package com.tll.client.event;

import java.util.EventListener;


/**
 * IAppBusyListener
 * @author jpk
 */
public interface IAppBusyListener extends EventListener {

	/**
	 * Fired when the app begins busy state.
	 */
	void onAppBusy();
	
	/**
	 * Fired when the app leaves busy state.
	 */
	void onAppUnbusy();
}
