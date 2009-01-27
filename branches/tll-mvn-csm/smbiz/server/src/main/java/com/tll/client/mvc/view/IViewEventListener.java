/**
 * The Logic Lab
 * @author jpk
 * Sep 3, 2007
 */
package com.tll.client.mvc.view;

import java.util.EventListener;


/**
 * IViewEventListener - Listens for {@link AbstractView} related events.
 * @author jpk
 */
public interface IViewEventListener extends EventListener {

	/**
	 * Fired when a view related event occurrs.
	 * @param event The event
	 */
	void onCurrentViewChanged(ViewChangedEvent event);
}
