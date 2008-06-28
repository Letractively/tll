/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;

import com.tll.client.event.type.EditEvent;

/**
 * ISourcesEditEvents
 * @author jpk
 */
public interface ISourcesEditEvents {

	/**
	 * Adds an edit listener
	 * @param listener
	 */
	void addEditListener(IEditListener listener);

	/**
	 * Removes an edit listener
	 * @param listener
	 */
	void removeEditListener(IEditListener listener);

	/**
	 * EditListenerCollection
	 * @author jpk
	 */
	final class EditListenerCollection extends ArrayList<IEditListener> {

		public void fireEditEvent(EditEvent event) {
			for(IEditListener listener : this) {
				listener.onEditEvent(event);
			}
		}
	}
}
