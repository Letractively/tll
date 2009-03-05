/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.ui.edit;

import com.google.gwt.event.shared.EventHandler;


/**
 * IEditHandler - Listens to edit events.
 * @author jpk
 */
public interface IEditHandler extends EventHandler {

	/**
	 * Fired when an edit event occurs.
	 * @param event The event
	 */
	void onEdit(EditEvent event);
}
