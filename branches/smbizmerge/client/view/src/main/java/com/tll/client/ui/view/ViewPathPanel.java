/**
 * 
 */
package com.tll.client.ui.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.P;
import com.tll.client.view.IViewChangeHandler;
import com.tll.client.view.ViewChangeEvent;
import com.tll.client.view.ViewManager;
import com.tll.client.view.ViewRef;

/**
 * ViewPathPanel - Renders the current view path.
 * @author jpk
 */
public class ViewPathPanel extends Composite implements IViewChangeHandler {

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
	 * Default spacer html token.
	 */
	private static final String DEFAULT_SPACER_HTML = "&raquo;";

	/**
	 * The spacer html token
	 */
	private final String spacerHtml;

	private final int capacity;

	/**
	 * The topmost (parent) ulPanel of this {@link Widget}.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * Panel containing the {@link ViewLink}s.
	 */
	private final HtmlListPanel ulPanel = new HtmlListPanel(false);

	/**
	 * Constructor
	 * @param capacity the max number of view links to display
	 */
	public ViewPathPanel(final int capacity) {
		this(DEFAULT_SPACER_HTML, capacity);
	}

	/**
	 * Constructor
	 * @param spacerHtml The html text to use for the spacer token
	 * @param capacity the max number of view links to display
	 */
	public ViewPathPanel(final String spacerHtml, final int capacity) {
		this.spacerHtml = spacerHtml;
		this.capacity = capacity;
		container.addStyleName(Styles.HNAV);
		container.addStyleName(Styles.VIEWPATH);
		container.add(ulPanel);
		initWidget(container);
	}

	@Override
	public void onViewChange(final ViewChangeEvent event) {
		ulPanel.clear();
		final ViewRef[] viewPath = ViewManager.get().getViewRefs(capacity, false);
		if(viewPath != null && viewPath.length > 0) {
			final int count = viewPath.length;
			for(int i = count - 1; i >= 0; i--) {
				// add view link
				ulPanel.append(new ViewLink(viewPath[i]));

				// add spacer
				final P p = new P();
				p.setStyleName(Styles.SPACER);
				p.getElement().setInnerHTML(spacerHtml);
				ulPanel.append(p);
			}
		}
		else {
			final P p = new P(" ");
			p.setStyleName(Styles.SPACER);
			ulPanel.append(p);
		}
	}

	private HandlerRegistration vch;

	@Override
	protected void onLoad() {
		super.onLoad();
		if(vch != null) throw new IllegalStateException();
		vch = ViewManager.get().addViewChangeHandler(this);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		if(vch != null) {
			vch.removeHandler();
			vch = null;
		}
	}
}
