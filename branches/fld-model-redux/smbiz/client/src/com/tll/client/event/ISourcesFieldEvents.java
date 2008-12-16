/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;

import com.tll.client.event.type.FieldEvent;

/**
 * ISourcesFieldEvents
 * @author jpk
 */
public interface ISourcesFieldEvents {

	/**
	 * Adds a listener
	 * @param listener
	 */
	void addFieldListener(IFieldListener listener);

	/**
	 * Removes a listener
	 * @param listener
	 */
	void removeFieldListener(IFieldListener listener);

	/**
	 * FieldListenerCollection
	 * @author jpk
	 */
	final class FieldListenerCollection extends ArrayList<IFieldListener> {

		public void fire(FieldEvent event) {
			for(IFieldListener listener : this) {
				listener.onFieldEvent(event);
			}
		}
	}
}
