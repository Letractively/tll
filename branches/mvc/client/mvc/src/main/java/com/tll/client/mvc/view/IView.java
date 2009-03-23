/**
 * The Logic Lab
 * @author jpk
 * May 10, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * IView - Runtime view definition defining a view's life-cycle.
 * @author jpk
 */
public interface IView extends IViewRef {

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
	 * @return The Widget used in the UI that represents this view.
	 */
	Widget getViewWidget();

	/**
	 * Provision for generating a {@link IViewRequest} that "points" to this
	 * particular AbstractView implementation.
	 * <p>
	 * The purponse for this method, among others, is to have the ability to
	 * "re-constitute" any particular AbstractView at any time during the app's
	 * loaded life-cycle.
	 * @return New and configured {@link IViewRequest} instance.
	 */
	ShowViewRequest getViewRequest();

	/**
	 * The view options that define how the view appears.
	 * @return view options
	 */
	ViewOptions getOptions();

	/**
	 * Initializes the view with the runtime dependant {@link IViewRequest}.
	 * @param viewRequest The view request responsible for the instantiation of
	 *        this view. May NOT be <code>null</code>.
	 */
	void initialize(IViewRequest viewRequest);

	/**
	 * Refreshes the contents of the view. This method also serves to populate the
	 * UI. A call to {@link #initialize(IViewRequest)} is required before this
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