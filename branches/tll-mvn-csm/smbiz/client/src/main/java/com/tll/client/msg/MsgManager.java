/**
 * The Logic Lab
 * @author jpk Sep 5, 2007
 */
package com.tll.client.msg;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.IDragListener;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.TimedPositionedPopup.Position;

/**
 * MsgManager - This construct is aware of all {@link MsgPanel}s present in the
 * DOM and manages their life-cycle.
 * @author jpk
 */
public final class MsgManager implements IDragListener, ScrollListener {

	/**
	 * the singleton instance
	 */
	private static final MsgManager instance = new MsgManager();

	public static final MsgManager instance() {
		return instance;
	}

	private enum PopupState {
		SHOWING,
		HIDING,
		EITHER;
	}

	/**
	 * List of ALL {@link MsgPanel}s referenced in the app (including
	 * {@link #globalMsgPanel}).
	 */
	private final List<MsgPanel> msgPanels = new ArrayList<MsgPanel>();

	/**
	 * List of showing {@link MsgPanel}s during a dragging op.
	 */
	private List<MsgPanel> draggingMsgPanels;

	/**
	 * Constructor
	 */
	public MsgManager() {
		super();
	}

	/**
	 * Posts a list of {@link Msg}s that will be bound to a given {@link Widget}.
	 * @param autoHide When <code>true</code> an outside click hides the message
	 *        panel.
	 * @param msgs The messages to post
	 * @param position How to center the message popup relative to the given
	 *        Widget
	 * @param refWidget The target Widget.
	 * @param duration How long in mili-seconds to show the message popup. If
	 *        <code>-1</code>, the duration is infinite.
	 * @param clearExisting Clear out existing messages bound to the given target
	 *        Widget first?
	 * @return The message panel bound to the given Widget. Usually, a call to
	 *         {@link MsgPanel#show()} will ensue.
	 * @throws IllegalArgumentException When no messages are specified or the
	 *         given reference is <code>null</code>.
	 */
	public MsgPanel post(boolean autoHide, List<Msg> msgs, Position position, Widget refWidget, int duration,
			boolean clearExisting) throws IllegalArgumentException {
		if(msgs == null || msgs.size() < 1) {
			throw new IllegalArgumentException("No messages specified.");
		}
		if(refWidget == null) {
			throw new IllegalArgumentException("A reference widget must be specified.");
		}
		MsgPanel mp = findMsgPanel(refWidget.getElement());
		if(mp == null) {
			mp = stubMsgPanel(autoHide, position, refWidget.getElement(), duration, false);
		}
		if(clearExisting) mp.removeMsgs();
		mp.addMsgs(msgs);
		return mp;
	}

	/**
	 * Posts a single {@link Msg} that will be bound to a given {@link Widget}.
	 * @param autoHide When <code>true</code> an outside click hides the message
	 *        panel.
	 * @param msg The message to post
	 * @param position How to center the message popup relative to the given
	 *        Widget
	 * @param refWidget The target Widget.
	 * @param duration How long in mili-seconds to show the message popup. If
	 *        <code>-1</code>, the duration is infinite.
	 * @param clearExisting Clear out existing messages bound to the given target
	 *        Widget first?
	 * @return The message panel bound to the given Widget. Usually, a call to
	 *         {@link MsgPanel#show()} will ensue.
	 * @throws IllegalArgumentException When either the msg or the reference
	 *         widget is not specified.
	 */
	public MsgPanel post(boolean autoHide, Msg msg, Position position, Widget refWidget, int duration,
			boolean clearExisting) {
		if(msg == null) {
			throw new IllegalArgumentException("No message specified.");
		}
		final List<Msg> msgs = new ArrayList<Msg>(1);
		msgs.add(msg);
		return post(autoHide, msgs, position, refWidget, duration, clearExisting);
	}

	/**
	 * Shows or hides the {@link MsgPanel} bound to the given Widget and if
	 * specified, all {@link MsgPanel}s bound to the child {@link Widget}s.
	 * @param w The widget for which to show the {@link MsgPanel}.
	 * @param show Show or hide?
	 * @param drillDown Apply to the child Widgets?
	 */
	public void show(Widget w, boolean show, boolean drillDown) {
		if(drillDown) {
			List<MsgPanel> list = findContainedMsgPanels(w, PopupState.EITHER);
			if(list == null) return;
			for(MsgPanel mp : list) {
				if(show) {
					mp.show();
				}
				else {
					mp.hide();
				}
			}
		}
		else {
			MsgPanel mp = findMsgPanel(w.getElement());
			if(mp == null) return;
			if(show)
				mp.show();
			else
				mp.hide();
		}
	}

	/**
	 * Toggles the display of the {@link MsgPanel} bound to the given widget and
	 * all nested widgets. {@link Widget} if there is one.
	 * @param w The widget to be toggled.
	 * @param drillDown Apply toggle to the child Widgets?
	 */
	public void toggle(Widget w, boolean drillDown) {
		if(drillDown) {
			List<MsgPanel> list = findContainedMsgPanels(w, PopupState.EITHER);
			if(list == null) return;
			for(MsgPanel mp : list) {
				mp.toggle();
			}
		}
		else {
			MsgPanel mp = findMsgPanel(w.getElement());
			if(mp != null) mp.toggle();
		}
	}

	/**
	 * Clears all found {@link MsgPanel}s bound to the given widget and and all
	 * nested widgets.
	 * @param w The Widget
	 * @param drillDown
	 */
	public void clear(Widget w, boolean drillDown) {
		if(drillDown) {
			List<MsgPanel> list = findContainedMsgPanels(w, PopupState.EITHER);
			if(list == null) return;
			for(MsgPanel mp : list) {
				mp.hide();
				msgPanels.remove(mp);
			}
		}
		else {
			MsgPanel mp = findMsgPanel(w.getElement());
			if(mp != null) {
				mp.hide();
				msgPanels.remove(mp);
			}
		}
	}

	/**
	 * Clears all content from all referenced {@link MsgPanel}s.
	 */
	public void clear() {
		for(MsgPanel mp : msgPanels) {
			mp.hide();
		}
		msgPanels.clear();
	}

	/**
	 * Stubs a MsgPanel adding it to the internal list of referenced msg panels.
	 * @param autoHide
	 * @param position
	 * @param refElement
	 * @param duration mili-seconds
	 * @param showImage
	 * @return New MsgPanel instance
	 */
	private MsgPanel stubMsgPanel(boolean autoHide, Position position, Element refElement, int duration, boolean showImage) {
		MsgPanel mp = new MsgPanel(autoHide, position, refElement, duration, showImage);
		msgPanels.add(mp);
		return mp;
	}

	/**
	 * Finds the msg panel bound the given element returning <code>null</code> if
	 * no msg panel is bound.
	 * @param refElement The Element to check
	 * @return The bound MsgPanel or <code>null</code> if none bound.
	 */
	private MsgPanel findMsgPanel(Element refElement) {
		for(MsgPanel mp : msgPanels) {
			if(mp.getRefElement() == refElement) {
				return mp;
			}
		}
		return null;
	}

	/**
	 * Finds all {@link MsgPanel}s <em>contained in</em> the given Widget.
	 * @param w The widget If <code>null</code>, <code>null</code> is returned
	 * @param state The desired MsgPanel state which filters the results
	 * @return List of the contained {@link MsgPanel}s.
	 */
	private List<MsgPanel> findContainedMsgPanels(Widget w, PopupState state) {
		if(w == null) return null;
		List<MsgPanel> list = new ArrayList<MsgPanel>();
		for(MsgPanel mp : msgPanels) {
			if(w.getElement().isOrHasChild(mp.getRefElement())) {
				boolean add = false;
				switch(state) {
					case SHOWING:
						add = mp.isShowing();
						break;
					case HIDING:
						add = !mp.isShowing();
						break;
					case EITHER:
						add = true;
				}
				if(add) list.add(mp);
			}
		}
		return list;
	}

	public void onDragStart(DragEvent event) {
		assert draggingMsgPanels == null;
		// tell the contained msg panels we are dragging
		draggingMsgPanels = findContainedMsgPanels((Widget) event.getSource(), PopupState.SHOWING);
		if(draggingMsgPanels == null) return;
		for(MsgPanel mp : draggingMsgPanels) {
			mp.hide();
		}
	}

	public void onDragging(DragEvent event) {
	}

	public void onDragEnd(DragEvent event) {
		if(draggingMsgPanels == null) return;
		for(MsgPanel mp : draggingMsgPanels) {
			mp.show();
		}
		draggingMsgPanels = null;
	}

	public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
		// hide all showing msg panels contained in the widget
		show(widget, false, true);
	}
}
