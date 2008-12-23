/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.ui;

import java.util.EventListener;


/**
 * IEditListener - Listens to edit events.
 * @author jpk
 */
public interface IEditListener extends EventListener {

	/**
	 * Fired when an edit event occurs.
	 * @param event The event
	 */
	void onEditEvent(EditEvent event);
}
