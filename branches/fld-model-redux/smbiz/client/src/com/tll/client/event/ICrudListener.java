/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.CrudEvent;



/**
 * ICrudListener - Event listener for data store CRUD related events.
 * @author jpk
 */
public interface ICrudListener extends EventListener {

	/**
	 * Fired when a CRUD related event occurs.
	 * @param event The event 
	 */
	void onCrudEvent(CrudEvent event);
}
