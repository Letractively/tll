/**
 * 
 */
package com.tll.client.ui.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IViewEventListener;
import com.tll.client.mvc.view.IViewRef;
import com.tll.client.mvc.view.ViewChangedEvent;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.P;

/**
 * ViewPathPanel - Renders the current view path.
 * @author jpk
 */
public class ViewPathPanel extends Composite implements IViewEventListener {

	/**
	 * Styles - (viewpath.css, hnav.css)
	 * @author jpk
	 */
	protected static class Styles {

		public static final String HNAV = "hnav";
		public static final String VIEWPATH = "viewpath";
		public static final String SPACER = "spacer";
	}

	/**
	 * Spacer HTML.
	 */
	private static final String SPACER_HTML = "&raquo;";

	/**
	 * The topmost (parent) ulPanel of this {@link Widget}.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * Panel containing the {@link ViewRequestLink}s.
	 */
	private final HtmlListPanel ulPanel = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public ViewPathPanel() {
		super();
		container.addStyleName(Styles.HNAV);
		container.addStyleName(Styles.VIEWPATH);
		container.add(ulPanel);
		initWidget(container);
	}

	public void onCurrentViewChanged(ViewChangedEvent event) {
		ulPanel.clear();
		final IViewRef[] viewPath = ViewManager.get().getViewPath();
		if(viewPath != null && viewPath.length > 0) {
			final int count = viewPath.length;
			for(int i = 0; i < count; i++) {
				// add view link
				ulPanel.append(IViewRef.Tools.getViewLink(viewPath[i]));

				// add spacer
				final P p = new P();
				p.setStyleName(Styles.SPACER);
				p.getElement().setInnerHTML(SPACER_HTML);
				ulPanel.append(p);
			}
		}
		else {
			final P p = new P(" ");
			p.setStyleName(Styles.SPACER);
			ulPanel.append(p);
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		ViewManager.get().addViewEventListener(this);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		ViewManager.get().removeViewEventListener(this);
	}
}