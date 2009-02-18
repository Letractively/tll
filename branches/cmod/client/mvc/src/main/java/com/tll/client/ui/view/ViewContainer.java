/**
 * The Logic Lab
 * @author jpk
 * Mar 13, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewState;
import com.tll.client.mvc.view.PinPopViewRequest;
import com.tll.client.mvc.view.UnloadViewRequest;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.IDragHandler;
import com.tll.client.ui.IHasDragEvents;
import com.tll.client.ui.DragEvent.DragMode;
import com.tll.client.ui.msg.MsgManager;

/**
 * ViewContainer - UI container for {@link IView} implementations.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class ViewContainer extends SimplePanel implements MouseDownHandler, MouseMoveHandler, MouseUpHandler,
		IHasDragEvents, ClickHandler, NativePreviewHandler {

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
			final Element td = getTd(w);
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

	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {
		// NOTE: we should only be in the popped state for previewing events
		// assert isPopped() == true; (commented out for performance - but asserts
		// ok)

		final int type = event.getTypeInt();
		final NativeEvent ne = event.getNativeEvent();
		final Element target = ne.getTarget();
		final boolean eventTargetsPopup = getElement().isOrHasChild(target);

		switch(type) {
			case Event.ONMOUSEDOWN:
				// We need to preventDefault() on mouseDown events (outside of the
				// DialogBox content) to keep text from being selected when it
				// is dragged.
				if(toolbar.viewTitle.getElement().isOrHasChild(target)) {
					//DOM.eventPreventDefault(event);
					ne.preventDefault();
				}
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONCLICK:
			case Event.ONDBLCLICK: {
				// Don't eat events if event capture is enabled, as this can interfere
				// with dialog dragging, for example.
				if(DOM.getCaptureElement() == null) {
					event.cancel();
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
		if(!eventTargetsPopup) {
			event.cancel();
		}
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
	
	private HandlerRegistration hrEventPreview, hrDrag, hrMsgManagerDragHandler;

	@Override
	public HandlerRegistration addDragHandler(IDragHandler handler) {
		if(hrDrag == null) {
			toolbar.addMouseDownHandler(this);
			toolbar.addMouseMoveHandler(this);
			toolbar.addMouseUpHandler(this);
		}
		return (hrDrag = addHandler(handler, DragEvent.TYPE));
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

	public void onMouseDown(MouseDownEvent event) {
		if(isPopped()) {
			endDrag((Widget) event.getSource());
			//DOM.setCapture(event.getNativeEvent().getTarget());
			event.stopPropagation();
			mouseIsDown = true;
			assert event.getClientX() >= 0 && event.getClientY() >= 0;
			dragStartX = event.getClientX();
			dragStartY = event.getClientY();
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		assert event.getSource() == toolbar.viewTitle;
		if(mouseIsDown) {
			if(!dragging) {
				dragging = true;
				fireEvent(new DragEvent(DragMode.START, dragStartX, dragStartY));
			}

			final int absX = event.getClientX() + getAbsoluteLeft();
			final int absY = event.getClientY() + getAbsoluteTop();

			int nx = absX - dragStartX;
			int ny = absY - dragStartY;

			// keep the drag handle within the viewable area!
			if(nx < 0) nx = 0;
			if(ny < 0) ny = 0;

			final Element elm = getElement();
			elm.getStyle().setPropertyPx("left", nx);
			elm.getStyle().setPropertyPx("top", ny);

			fireEvent(new DragEvent(DragMode.DRAGGING, event.getClientX() - dragStartX, event.getClientY() - dragStartY));
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if(mouseIsDown) {
			endDrag((Widget) event.getSource());
			fireEvent(new DragEvent(DragMode.END));
		}
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

			final int width = getWidget().getOffsetWidth();
			// int width = getWidget().getOffsetWidth();
			// int height = getWidget().getOffsetHeight();

			final Element elm = getElement();
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

			hrMsgManagerDragHandler = addDragHandler(MsgManager.get());

			assert hrEventPreview == null;
			hrEventPreview = Event.addNativePreviewHandler(this);
		}
	}

	/**
	 * Pins the view container to the given parent panel
	 * @param parent The parent panel
	 */
	public void pin(Panel parent) {
		if(!isAttached() || isPopped()) {
			assert parent != null;

			assert hrEventPreview != null;
			hrEventPreview.removeHandler();
			hrEventPreview = null;

			hrMsgManagerDragHandler.removeHandler();

			final Element elm = getElement();
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
		if(hrEventPreview != null) {
			hrEventPreview.removeHandler();
		}
		//DOM.removeEventPreview(this);
	}

	public void onClick(ClickEvent event) {
		final Object sender = event.getSource();
		
		// pop the view
		if(sender == toolbar.btnPop) {
			final boolean popped = isPopped();
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
