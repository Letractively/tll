/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.ui.edit;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;


/**
 * IHasEditHandlers
 * @author jpk
 */
public interface IHasEditHandlers extends HasHandlers {

	/**
	 * Adds a handler.
	 * @param handler
	 * @return {@link HandlerRegistration}
	 */
	HandlerRegistration addEditHandler(IEditHandler handler);
}
