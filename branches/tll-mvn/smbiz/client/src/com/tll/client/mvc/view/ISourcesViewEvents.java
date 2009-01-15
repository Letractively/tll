/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2008
 */
package com.tll.client.mvc.view;

import java.util.ArrayList;


/**
 * ISourcesViewEvents
 * @author jpk
 */
public interface ISourcesViewEvents {

	/**
	 * Adds a listener.
	 * @param listener
	 */
	void addViewEventListener(IViewEventListener listener);

	/**
	 * Removes a listener.
	 * @param listener
	 */
	void removeViewEventListener(IViewEventListener listener);

	/**
	 * ViewEventListenerCollection definition.
	 * @author jpk
	 */
	public static final class ViewEventListenerCollection extends ArrayList<IViewEventListener> {

		public void fireOnViewChanged(ViewChangedEvent event) {
			for(IViewEventListener listener : this) {
				listener.onCurrentViewChanged(event);
			}
		}
	}

}
