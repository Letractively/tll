/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.model;

import java.util.EventListener;




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
