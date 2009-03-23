/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.event.shared.HasHandlers;


/**
 * IHasViewChangeHandlers
 * @author jpk
 */
public interface IHasViewChangeHandlers extends HasHandlers {

	/**
	 * Adds a handler.
	 * @param handler
	 */
	void addViewChangeHandler(IViewChangeHandler handler);

	/**
	 * Removes a handler.
	 * @param handler
	 */
	void removeViewChangeHandler(IViewChangeHandler handler);
}
