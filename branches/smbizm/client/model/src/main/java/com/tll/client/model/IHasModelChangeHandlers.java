/**
 * The Logic Lab
 * @author jpk
 * @since Apr 24, 2009
 */
package com.tll.client.model;

import com.google.gwt.event.shared.HasHandlers;


/**
 * IHasModelChangeHandlers
 * @author jpk
 */
interface IHasModelChangeHandlers extends HasHandlers {

	/**
	 * Adds a model change handler.
	 * @param handler
	 */
	void addModelChangeHandler(IModelChangeHandler handler);

	/**
	 * Removes a model change handler.
	 * @param handler
	 */
	void removeModelChangeHandler(IModelChangeHandler handler);
}
