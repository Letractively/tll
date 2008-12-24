/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.model;

import java.util.ArrayList;


/**
 * ISourcesCrudEvents
 * @author jpk
 */
public interface ISourcesCrudEvents {

	/**
	 * Adds a CRUD listener
	 * @param listener
	 */
	void addCrudListener(ICrudListener listener);

	/**
	 * Removes a CRUD listener
	 * @param listener
	 */
	void removeCrudListener(ICrudListener listener);

	/**
	 * CrudListenerCollection
	 * @author jpk
	 */
	final class CrudListenerCollection extends ArrayList<ICrudListener> {

		public void fireCrudEvent(CrudEvent event) {
			for(ICrudListener listener : this) {
				listener.onCrudEvent(event);
			}
		}
	}
}