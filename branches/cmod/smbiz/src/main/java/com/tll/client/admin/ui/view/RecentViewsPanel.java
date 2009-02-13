/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.admin.ui.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IViewEventListener;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ViewChangedEvent;
import com.tll.client.ui.HtmlListPanel;

/**
 * RecentViewsPanel - Displays view links vertically that are currently in the
 * view cache that are NOT in the popped state.
 * @author jpk
 */
public final class RecentViewsPanel extends Composite implements IViewEventListener {

	/**
	 * Styles
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * Style applied to the widget containing the recent views listing.
		 */
		public static final String RECENT_VIEWS = "recentviews";
	}

	/**
	 * The topmost (parent) ulPanel of this {@link Widget}.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * AbstractView history links.
	 */
	private final HtmlListPanel ulPanel = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public RecentViewsPanel() {
		super();
		container.setStyleName(Styles.RECENT_VIEWS);
		container.add(ulPanel);
		initWidget(container);
	}

	public void onCurrentViewChanged(ViewChangedEvent event) {
		// NOTE: rebuild the ulPanel (it's MUCH easier than trying to remove/insert)
		ulPanel.clear();

		final IViewRef[] refs = ViewManager.instance().getRecentViews();
		final int count = refs.length;

		// re-build the recent view list
		// NOTE: ending at 1 before last element (skip the current view)
		for(int i = 0; i < count - 1; i++) {
			ulPanel.add(IViewRef.Tools.getViewLink(refs[i]));
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		ViewManager.instance().addViewEventListener(this);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		ViewManager.instance().removeViewEventListener(this);
	}

}