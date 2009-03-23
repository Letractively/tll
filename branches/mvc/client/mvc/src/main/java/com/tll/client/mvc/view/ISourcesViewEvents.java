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
	void addViewEventListener(IViewChangeHandler listener);

	/**
	 * Removes a listener.
	 * @param listener
	 */
	void removeViewEventListener(IViewChangeHandler listener);

	/**
	 * ViewEventListenerCollection definition.
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	public static final class ViewEventListenerCollection extends ArrayList<IViewChangeHandler> {

		public void fireOnViewChanged(ViewChangedEvent event) {
			for(IViewChangeHandler listener : this) {
				listener.onCurrentViewChanged(event);
			}
		}
	}

}
