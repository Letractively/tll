/**
 * The Logic Lab
 * @author jpk May 10, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * IView - Runtime view definition defining a view's life-cycle.
 * @author jpk
 * @param <I> the view initializer type
 */
public interface IView<I extends IViewInitializer> {

	/**
	 * Styles - (view.css)
	 * @author jpk
	 */
	static final class Styles {

		/**
		 * Style applied to IView implementing widgets.
		 */
		static final String VIEW = "view";
	}

	/**
	 * @return The short view name.
	 */
	String getShortViewName();

	/**
	 * @return The long view name.
	 */
	String getLongViewName();

	/**
	 * @return The Widget used in the UI that represents this view.
	 */
	Widget getViewWidget();

	/**
	 * Initializes the view enabling it to be uniquely identifiable at runtime.
	 * @param initializer The view key provider responsible for providing the view
	 *        the ability to provide a {@link ViewKey} which is essential for
	 *        uniquely identifying views at runtime.
	 */
	void initialize(I initializer);

	/**
	 * Refreshes the contents of the view. This method also serves to populate the
	 * UI. A call to {@link #initialize(IViewInitializer)} is required before this
	 * method may be called.
	 */
	void refresh();

	/**
	 * Life-cycle provision for view implementations to perform clean-up before
	 * this view looses reference-ability. This could mean, for example, to issue
	 * an RPC cache clean up type command.
	 */
	void onDestroy();
}