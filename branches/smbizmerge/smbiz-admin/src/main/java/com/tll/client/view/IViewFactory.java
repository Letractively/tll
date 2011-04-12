/**
 * The Logic Lab
 * @author jpk
 * @since Apr 12, 2011
 */
package com.tll.client.view;

/**
 * View factory definition.
 * @author jpk
 */
public interface IViewFactory {

	/**
	 * @return the default view.
	 */
	IView getDefaultView();

	/**
	 * Provides a view impl given the view def (class).
	 * @param def
	 * @return view impl instance
	 */
	<T extends IView> T getView(Class<T> def);
}
