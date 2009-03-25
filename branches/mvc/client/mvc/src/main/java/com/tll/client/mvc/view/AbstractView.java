/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.mvc.view;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * AbstractView - Base view class for all defined views in the app.
 * @author jpk
 * @param <R>
 */
public abstract class AbstractView<R extends IViewKeyProvider> extends Composite implements IView<R> {

	/**
	 * The view key uniquely indentifying the view at runtime.
	 */
	private IViewKey viewKey;

	/**
	 * The wrapped Widget
	 */
	private final FlowPanel pnl = new FlowPanel();

	/**
	 * Constructor
	 */
	public AbstractView() {
		super();
		pnl.setStylePrimaryName(Styles.VIEW);
		initWidget(pnl);
	}

	public final Widget getViewWidget() {
		return this;
	}

	/**
	 * @return The {@link ViewClass} of this AbstractView.
	 */
	protected abstract ViewClass getViewClass();

	public final ViewOptions getOptions() {
		return getViewClass().getViewOptions();
	}

	public final IViewKey getViewKey() {
		return viewKey;
	}

	public String getShortViewName() {
		return getLongViewName();
	}

	/**
	 * Adds a Widget to this view's UI layout.
	 * @param widget
	 */
	protected final void addWidget(Widget widget) {
		pnl.add(widget);
	}

	/**
	 * Override this method when the impl needs a css style callout added to the
	 * view Widget. The default is <code>null</code>.
	 * @return AbstractView impl specific style.
	 */
	protected String getViewStyle() {
		return null;
	}

	public final void initialize(R initializer) {
		if(initializer == null || initializer.getViewKey() == null)
			throw new IllegalArgumentException("Null or invalid view initializer.");
		viewKey = initializer.getViewKey();
		// add view specific style to the view's widget
		if(getViewStyle() != null) {
			addStyleName(getViewStyle());
		}

		// do impl specific initialization
		doInitialization(initializer);
	}

	/**
	 * Performs impl specific initialization just after the ViewKey has been set.
	 * @param viewRequest The non-<code>null</code> view request.
	 */
	protected abstract void doInitialization(R viewRequest);

	/**
	 * Life-cycle provision for view implementations to perform clean-up before
	 * this view looses reference-ability. This could mean, for example, to issue
	 * an RPC cache clean up type command.
	 */
	public final void onDestroy() {
		Log.debug("Destroying view " + toString());
		doDestroy();
	}

	/**
	 * AbstractView impls use this hook to perform any necessary clean up just
	 * before this view looses reference-ability
	 */
	protected abstract void doDestroy();

	@Override
	public final String toString() {
		return getViewClass() + " [" + (viewKey == null ? "-nokey-" : Integer.toString(viewKey.getViewId())) + "]";
	}
}
