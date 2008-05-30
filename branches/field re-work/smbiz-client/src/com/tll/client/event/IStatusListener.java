/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.StatusEvent;

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
