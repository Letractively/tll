/**
 * The Logic Lab
 * @author jpk
 * @since Oct 17, 2009
 */
package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.impl.GlassPanelImpl;

/**
 * GlassPanel
 * @author jpk
 */
public class GlassPanel extends Composite implements NativePreviewHandler {

	/**
	 * A FocusPanel which automatically focuses itself when attached (in order to
	 * blur any currently focused widget) and then removes itself.
	 */
	private static class FocusPanelImpl extends FocusPanel {

		public FocusPanelImpl() {
			addBlurHandler(new BlurHandler() {

				@Override
				public void onBlur(BlurEvent event) {
					FocusPanelImpl.this.removeFromParent();
				}

			});
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			/**
			 * Removed DeferredCommand if/when GWT issue 1849 is implemented
			 * http://code.google.com/p/google-web-toolkit/issues/detail?id=1849
			 */
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					setFocus(true);
				}
			});
		}
	}

	static final GlassPanelImpl impl = GWT.create(GlassPanelImpl.class);

	private HandlerRegistration resizeBus, nativePreview;

	private final boolean autoHide;

	private final SimplePanel mySimplePanel;

	private final Timer timer = new Timer() {

		@Override
		public void run() {
			impl.matchDocumentSize(GlassPanel.this, false);
		}
	};

	/**
	 * Create a glass panel widget that can be attached to an AbsolutePanel via
	 * {@link AbsolutePanel#add(com.google.gwt.user.client.ui.Widget, int, int)
	 * absolutePanel.add(glassPanel, 0, 0)}.
	 * @param autoHide <code>true</code> if the glass panel should be automatically
	 *        hidden when the user clicks on it or presses <code>ESC</code>.
	 */
	public GlassPanel(boolean autoHide) {
		this.autoHide = autoHide;
		mySimplePanel = new SimplePanel();
		initWidget(mySimplePanel);
		final Style style = getElement().getStyle();
		style.setProperty("backgroundColor", "#000");
		style.setProperty("filter", "alpha(opacity=50)");
		style.setProperty("opacity", "0.5");
		setStyleName("gwt-GlassPanel");
	}

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {
		switch(event.getTypeInt()) {
		case Event.ONKEYPRESS: {
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				removeFromParent();
				event.cancel();
			}
		}
		case Event.ONCLICK: {
			if(DOM.isOrHasChild(getElement(), DOM.eventGetTarget(Event.as(event.getNativeEvent())))) {
				removeFromParent();
				event.cancel();
			}
		}
		}
	}

	@Override
	public void setWidget(Widget widget) {
		mySimplePanel.setWidget(widget);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		AbsolutePanel parent;
		try {
			parent = (AbsolutePanel) getParent();
		}
		catch(final RuntimeException e) {
			throw new IllegalStateException("Parent widget must be an instance of AbsolutePanel");
		}

		if(parent == RootPanel.get()) {
			impl.matchDocumentSize(this, false);
			timer.scheduleRepeating(100);
			resizeBus = Window.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {
					impl.matchDocumentSize(GlassPanel.this, true);
				}
			});
		}
		else {
			impl.matchParentSize(this, parent);
		}
		if(autoHide) {
			nativePreview = Event.addNativePreviewHandler(this);
		}

		RootPanel.get().add(new FocusPanelImpl(), Window.getScrollLeft(), Window.getScrollTop());
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		timer.cancel();
		if(resizeBus != null) {
			resizeBus.removeHandler();
			resizeBus = null;
		}
		if(nativePreview != null) {
			nativePreview.removeHandler();
		}
	}
}
