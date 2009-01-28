/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.model;

import java.util.EventListener;


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
