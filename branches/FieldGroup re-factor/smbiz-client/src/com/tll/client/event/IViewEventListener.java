/**
 * The Logic Lab
 * @author jpk
 * Sep 3, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.ViewChangedEvent;
import com.tll.client.mvc.view.AbstractView;

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
