/**
 * The Logic Lab
 * @author jpk
 * @since Mar 25, 2009
 */
package com.tll.client.mvc.view;

/**
 * IViewInitializer - The runtime compliment to instantiated {@link IView}s.
 * @author jpk
 */
public interface IViewInitializer {

	/**
	 * @return the view key.
	 */
	ViewKey getViewKey();
}
