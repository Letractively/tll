/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.data.rpc;

import java.util.EventListener;


/**
 * IStatusListener
 * @author jpk
 */
public interface IStatusListener extends EventListener {

	/**
	 * Fired when a status related event occurrs.
	 * @param event the status event
	 */
	void onStatusEvent(StatusEvent event);
}
