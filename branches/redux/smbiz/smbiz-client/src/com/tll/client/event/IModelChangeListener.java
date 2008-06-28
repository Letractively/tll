/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.ModelChangeEvent;

/**
 * IModelChangeListener - Event listener for model change related events.
 * @author jpk
 */
public interface IModelChangeListener extends EventListener {

	/**
	 * Fired when a model change related event occurs.
	 * @param event The event
	 */
	void onModelChangeEvent(ModelChangeEvent event);
}
