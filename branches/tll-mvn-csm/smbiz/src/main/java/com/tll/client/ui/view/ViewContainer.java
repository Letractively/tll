/**
 * The Logic Lab
 * @author jpk
 * Mar 13, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.msg.MsgManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewState;
import com.tll.client.mvc.view.PinPopViewRequest;
import com.tll.client.mvc.view.UnloadViewRequest;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.IDragListener;
import com.tll.client.ui.ISourcesDragEvents;

/**
 * ViewContainer - UI container for {@link IView} implementations.
 * @author jpk
 */
public final class ViewContainer extends SimplePanel implements MouseListener, ISourcesDragEvents, ClickListener,
		EventPreview {

	/**
	 * Styles - (view.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * Primary style applied to the widget that is the view container.
		 */
		public static final String VIEW_CONTAINER = "viewContainer";
		/**
		 * Secondary style for view container's in the popped state.
		 */
		public static final String POPPED = "popped";
		/**
		 * Secondary style for view container's in the pinned state.
		 */
		public static final String PINNED = "pinned";

	} // Styles

	/**
	 * The wrapped IView
	 */
	private final IView view;

	private final ViewToolbar toolbar;

	private final MyVerticalPanel mainLayout = new MyVerticalPanel();

	private boolean mouseIsDown, dragging;
	private int dragStartX, dragStartY;
	// private DragInfo dragInfo;

	private final DragListenerCollection dragListeners = new DragListenerCollection();

	/**
	 * MyVerticalPanel - Simple extension of VerticalPanel to get at td and tr
	 * table elements for blyhme's sake!
	 * @author jpk
	 */
	private static final class MyVerticalPanel extends VerticalPanel {

		Element getTd(Widget w) {
			if(w.getParent() != this) {
				return null;
			}
			return w.getElement().getParentElement();
		}

		Element getWidgetTr(Widget w) {
			Element td = getTd(w);
			return td == null ? null : td.getParentElement();
		}
	}

	/**
	 * Constructor
	 * @param view The view to set
	 */
	public ViewContainer(IView view) {
		super();
		assert view != null;
		this.view = view;
		toolbar = new ViewToolbar(view.getLongViewName(), view.getOptions(), this);
		mainLayout.add(toolbar);
		mainLayout.add(view.getViewWidget());
		mainLayout.setStylePrimaryName(Styles.VIEW_CONTAINER);
		setWidget(mainLayout);
	}

	public boolean onEventPreview(Event event) {
		// NOTE: we should only be in the popped state for previewing events
		// assert isPopped() == true; (commented out for performance - but asserts
		// ok)

		final int type = event.getTypeInt();
		final Element target = event.getTarget();
		boolean eventTargetsPopup = getElement().isOrHasChild(target);

		switch(type) {
			case Event.ONMOUSEDOWN:
				// We need to preventDefault() on mouseDown events (outside of the
				// DialogBox content) to keep text from being selected when it
				// is dragged.
				if(toolbar.viewTitle.getElement().isOrHasChild(target)) {
					DOM.eventPreventDefault(event);
				}
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONCLICK:
			case Event.ONDBLCLICK: {
				// Don't eat events if event capture is enabled, as this can interfere
				// with dialog dragging, for example.
				if(DOM.getCaptureElement() != null) {
					return true;
				}
				break;
			}
		}

		// debug logging
		/*
		if(dragging) {
			String msg = "t:" + type + " tg?:" + eventTargetsPopup + " rvl:" + rval;
			StatusDisplay.log(new Msg(msg, Msg.LEVEL_INFO));
		}
		*/

		// NOTE: we dis-allow UI interaction with content NOT contained w/in this
		// view container!
		return eventTargetsPopup;
	}

	/**
	 * @return the view
	 */
	public IView getView() {
		return view;
	}

	public IViewState getViewState() {
		return new IViewState() {

			public boolean isPopped() {
				return ViewContainer.this.isPopped();
			}

			public boolean isMinimized() {
				return ViewContainer.this.isMinimized();
			}

		};
	}

	public void addDragListener(IDragListener listener) {
		if(dragListeners.size() == 0) toolbar.addMouseListener(this);
		dragListeners.add(listener);
	}

	public void removeDragListener(IDragListener listener) {
		dragListeners.remove(listener);
		if(dragListeners.size() == 0) toolbar.removeMouseListener(this);
	}

	public boolean isPopped() {
		return RootPanel.get().getElement() == getElement().getParentElement();
	}

	public boolean isMinimized() {
		return "none".equals(mainLayout.getWidgetTr(view.getViewWidget()).getStyle().getProperty("display"));
	}

	private void endDrag(Widget sender) {
		if(mouseIsDown) {
			DOM.releaseCapture(sender.getElement());
			mouseIsDown = dragging = false;
			dragStartX = dragStartY = -1;
		}
	}

	public void onMouseDown(Widget sender, int x, int y) {
		if(isPopped()) {
			endDrag(sender);
			DOM.setCapture(sender.getElement());
			mouseIsDown = true;
			assert x >= 0 && y >= 0;
			dragStartX = x;
			dragStartY = y;
		}
	}

	public void onMouseMove(Widget sender, int x, int y) {
		assert sender == toolbar.viewTitle;
		if(mouseIsDown) {
			if(!dragging) {
				dragging = true;
				dragListeners.fireDragStart(new DragEvent(this, dragStartX, dragStartY));
			}

			int absX = x + getAbsoluteLeft();
			int absY = y + getAbsoluteTop();

			int nx = absX - dragStartX;
			int ny = absY - dragStartY;

			// keep the drag handle within the viewable area!
			if(nx < 0) nx = 0;
			if(ny < 0) ny = 0;

			Element elm = getElement();
			elm.getStyle().setPropertyPx("left", nx);
			elm.getStyle().setPropertyPx("top", ny);

			dragListeners.fireDragging(new DragEvent(this, x - dragStartX, y - dragStartY));
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		if(mouseIsDown) {
			endDrag(sender);
			dragListeners.fireDragEnd(new DragEvent(this));
		}
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	/**
	 * Pops the view container out of the natural DOM layout making its
	 * positioning absolute and and adding it to the {@link RootPanel} using the
	 * panel's existing position.
	 * @param refWidget The reference Widget used to determine the popped
	 *        position.
	 */
	public void pop(Widget refWidget) {
		assert refWidget != null;
		pop(refWidget.getAbsoluteTop() + 15, refWidget.getAbsoluteLeft() + 15);
	}

	/**
	 * Pops the view container out of the natural DOM layout making its
	 * positioning absolute and and adding it to the {@link RootPanel}.
	 * @param top The top pixel position
	 * @param left The left pixel position
	 */
	private void pop(int top, int left) {
		if(!isPopped()) {
			assert top > 0 && left > 0;

			mainLayout.removeStyleDependentName(Styles.PINNED);
			mainLayout.addStyleDependentName(Styles.POPPED);

			int width = getWidget().getOffsetWidth();
			// int width = getWidget().getOffsetWidth();
			// int height = getWidget().getOffsetHeight();

			Element elm = getElement();
			elm.getStyle().setProperty("position", "absolute");
			elm.getStyle().setPropertyPx("left", left);
			elm.getStyle().setPropertyPx("top", top);
			elm.getStyle().setPropertyPx("width", width);
			elm.getStyle().setProperty("height", "");

			RootPanel.get().add(this);
			// DOM.setStyleAttribute(elm, "width", getWidget().getOffsetWidth() +
			// "px");

			if(toolbar.btnMinimize != null) {
				toolbar.btnMinimize.setDown(false);
				toolbar.show(toolbar.btnMinimize);
			}

			assert toolbar.btnPop != null;
			toolbar.btnPop.setDown(true);
			toolbar.btnPop.setTitle(ViewToolbar.TITLE_PIN);

			addDragListener(MsgManager.instance());

			DOM.addEventPreview(this);
		}
	}

	/**
	 * Pins the view container to the given parent panel
	 * @param parent The parent panel
	 */
	public void pin(Panel parent) {
		if(!isAttached() || isPopped()) {
			assert parent != null;

			DOM.removeEventPreview(this);

			removeDragListener(MsgManager.instance());

			Element elm = getElement();
			elm.getStyle().setProperty("position", "");
			elm.getStyle().setProperty("left", "");
			elm.getStyle().setProperty("top", "");
			elm.getStyle().setProperty("width", "");
			elm.getStyle().setProperty("height", "");

			mainLayout.removeStyleDependentName(Styles.POPPED);
			mainLayout.addStyleDependentName(Styles.PINNED);

			maximize();
			if(toolbar.btnMinimize != null) {
				toolbar.hide(toolbar.btnMinimize);
			}

			if(toolbar.btnPop != null) {
				toolbar.btnPop.setDown(false);
				toolbar.btnPop.setTitle(ViewToolbar.TITLE_POP);
			}

			parent.add(this);
		}
	}

	/**
	 * Minimizes the view
	 */
	public void minimize() {
		if(!isMinimized()) {
			mainLayout.getWidgetTr(view.getViewWidget()).getStyle().setProperty("display", "none");
			toolbar.btnMinimize.setTitle(ViewToolbar.TITLE_MAXIMIZE);
		}
	}

	/**
	 * Maximizes the view
	 */
	public void maximize() {
		if(isMinimized()) {
			mainLayout.getWidgetTr(view.getViewWidget()).getStyle().setProperty("display", "");
			toolbar.btnMinimize.setTitle(ViewToolbar.TITLE_MINIMIZE);
		}
	}

	/**
	 * Removes this instance from its parent and clears its state.
	 */
	public void close() {
		removeFromParent();
		DOM.removeEventPreview(this);
	}

	public void onClick(Widget sender) {

		// pop the view
		if(sender == toolbar.btnPop) {
			boolean popped = isPopped();
			ViewManager.instance().dispatch(new PinPopViewRequest(this, view.getViewKey(), !popped));
		}

		// close the view
		else if(sender == toolbar.btnClose) {
			ViewManager.instance().dispatch(new UnloadViewRequest(this, view.getViewKey(), true));
		}

		// minimize/mazimize the view
		else if(sender == toolbar.btnMinimize) {
			if(toolbar.btnMinimize.isDown()) {
				minimize();
			}
			else {
				maximize();
			}
		}

		// refresh the view
		else if(sender == toolbar.btnRefresh) {
			view.refresh();
		}
	}

	public void onDestroy() {
		view.onDestroy();
	}

	@Override
	public String toString() {
		return view.toString();
	}
}
