/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.ViewRequestEvent;

/**
 * AbstractView - Base view class for all defined views in the app.
 * @author jpk
 */
public abstract class AbstractView extends Composite implements IViewRef {

	static final String CSS_VIEW = "view";

	/**
	 * The view key uniquely indentifying the view at runtime.
	 */
	private ViewKey viewKey;

	/**
	 * The wrapped Widget
	 */
	private final FlowPanel pnl = new FlowPanel();

	/**
	 * Constructor
	 */
	public AbstractView() {
		super();
		pnl.setStylePrimaryName(CSS_VIEW);
		initWidget(pnl);
	}

	/**
	 * @return The {@link ViewClass} of this AbstractView.
	 */
	protected abstract ViewClass getViewClass();

	/**
	 * Factory method employed by {@link #getViewRequest()}.
	 * @return New ViewRequest instance specific to the implementation.
	 */
	protected abstract ShowViewRequest newViewRequest();

	/**
	 * Provision for generating a {@link ViewRequestEvent} that "points" to this
	 * particular AbstractView implementation.
	 * <p>
	 * The purponse for this method, among others, is to have the ability to
	 * "re-constitute" any particular AbstractView at any time during the app's
	 * loaded life-cycle.
	 * @return New and configured {@link ViewRequestEvent} instance.
	 */
	public final ShowViewRequest getViewRequest() {
		ShowViewRequest r = newViewRequest();
		r.setLongViewName(getLongViewName());
		r.setShortViewName(getShortViewName());
		return r;
	}

	/**
	 * The default impl for the short view name. Override as desired.
	 */
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
	 * The view options that define how the view appears.
	 */
	public final ViewOptions getOptions() {
		return getViewClass().getViewOptions();
	}

	/**
	 * @return the ViewKey which is runtime dependant
	 */
	public final ViewKey getViewKey() {
		return viewKey;
	}

	/**
	 * Override this method when the impl needs a css style callout added to the
	 * view Widget. The default is <code>null</code>.
	 * @return AbstractView impl specific style.
	 */
	protected String getViewStyle() {
		return null;
	}

	/**
	 * Initializes the view with the runtime dependant {@link ViewRequestEvent}.
	 * @param viewRequest The view request responsible for the instantiation of
	 *        this view. May NOT be <code>null</code>.
	 */
	public final void initialize(ViewRequestEvent viewRequest) {
		assert viewRequest != null;
		// set the view key
		this.viewKey = viewRequest.getViewKey();
		assert viewKey != null;
		// add view specific style to the view's widget
		if(getViewStyle() != null) {
			addStyleName(getViewStyle());
		}

		// do impl specific initialization
		doInitialization(viewRequest);
	}

	/**
	 * Performs impl specific initialization just after the ViewKey has been set.
	 * @param viewRequest The non-<code>null</code> view request.
	 */
	protected abstract void doInitialization(ViewRequestEvent viewRequest);

	/**
	 * Refreshes the contents of the view.
	 */
	public abstract void refresh();

	/**
	 * Life-cycle provision for view implementations to perform clean-up before
	 * this view looses reference-ability. This could mean, for example, to issue
	 * an RPC cache clean up type command.
	 */
	public final void onDestroy() {
		doDestroy();
	}

	/**
	 * AbstractView impls use this hook to perform any necessary clean up just
	 * before this view looses reference-ability
	 */
	protected abstract void doDestroy();

	@Override
	public final String toString() {
		return "class[" + getViewClass().toString() + "] key[" + (viewKey == null ? "UNSET" : viewKey.toString()) + "]";
	}
}
