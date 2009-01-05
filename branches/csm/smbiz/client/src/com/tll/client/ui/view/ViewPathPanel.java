/**
 * 
 */
package com.tll.client.ui.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
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

	private static final String CSS_VIEWPATH = "viewpath";
	private static final String CSS_SPACER = "spacer";
	private static final String CSS_SPACER_HTML = "&raquo;";

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
		container.addStyleName(Style.HNAV);
		container.addStyleName(CSS_VIEWPATH);
		container.add(ulPanel);
		initWidget(container);
	}

	public void onCurrentViewChanged(ViewChangedEvent event) {
		ulPanel.clear();
		IViewRef[] viewPath = ViewManager.instance().getViewPath();
		if(viewPath != null && viewPath.length > 0) {
			int count = viewPath.length;
			for(int i = 0; i < count; i++) {
				// add view link
				ulPanel.add(IViewRef.Tools.getViewLink(viewPath[i]));

				// add spacer
				P p = new P();
				p.setStyleName(CSS_SPACER);
				p.getElement().setInnerHTML(CSS_SPACER_HTML);
				ulPanel.add(p);
			}
		}
		else {
			P p = new P(" ");
			p.setStyleName(CSS_SPACER);
			ulPanel.add(p);
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
