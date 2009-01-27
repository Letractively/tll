/**
 * The Logic Lab
 * @author jpk Jan 27, 2008
 */
package com.tll.client.model;

import java.util.ArrayList;

/**
 * ISourcesModelChangeEvents
 * @author jpk
 */
public interface ISourcesModelChangeEvents {

	/**
	 * Adds a listener interface to receive option related events.
	 * @param listener the listener interface to add
	 */
	void addModelChangeListener(IModelChangeListener listener);

	/**
	 * Removes a previously added listener interface.
	 * @param listener the listener interface to remove
	 */
	void removeModelChangeListener(IModelChangeListener listener);

	/**
	 * ModelChangeListenerCollection
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	public static class ModelChangeListenerCollection extends ArrayList<IModelChangeListener> {

		public void fireOnModelChange(ModelChangeEvent event) {
			for(IModelChangeListener listener : this) {
				listener.onModelChangeEvent(event);
			}
		}

	}
}