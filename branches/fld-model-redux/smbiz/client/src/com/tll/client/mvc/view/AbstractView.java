/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.ModelChangeEvent;

/**
 * AbstractView - Base view class for all defined views in the app.
 * @author jpk
 */
public abstract class AbstractView extends Composite implements IView {

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

	public final ViewKey getViewKey() {
		return viewKey;
	}

	/**
	 * Factory method employed by {@link #getViewRequest()}.
	 * @return New ViewRequest instance specific to the implementation.
	 */
	protected abstract ShowViewRequest newViewRequest();

	public final ShowViewRequest getViewRequest() {
		ShowViewRequest r = newViewRequest();
		r.setLongViewName(getLongViewName());
		r.setShortViewName(getShortViewName());
		return r;
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

	/**
	 * Handles model change errors. Sub-classes should override as necessary.
	 * @param event The {@link ModelChangeEvent}
	 */
	protected void handleModelChangeError(ModelChangeEvent event) {
		// base impl no-op
	}

	/**
	 * Handles successful model changes. Sub-classes should override as necessary.
	 * @param event The {@link ModelChangeEvent}
	 */
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		// base impl no-op
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		// is this our model change?
		if(event.getWidget() != this || !event.getWidget().getElement().isOrHasChild(this.getElement())) return;

		// errors?
		if(event.getStatus().hasErrors()) {
			handleModelChangeError(event);
			return;
		}

		// ahh success
		handleModelChangeSuccess(event);
	}

	@Override
	public final String toString() {
		return "class[" + getViewClass().toString() + "] key[" + (viewKey == null ? "UNSET" : viewKey.toString()) + "]";
	}
}
