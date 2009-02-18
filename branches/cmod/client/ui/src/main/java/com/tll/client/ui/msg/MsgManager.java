/**
 * The Logic Lab
 * @author jpk Sep 5, 2007
 */
package com.tll.client.ui.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.IDragHandler;
import com.tll.common.msg.Msg;

/**
 * MsgManager - This construct is aware of all {@link IMsgOperator}s present in
 * the DOM and manages their life-cycle.
 * @author jpk
 */
public final class MsgManager implements IDragHandler, ScrollHandler {
	
	/**
	 * PopupState - Used to filter {@link IMsgOperator}s when finding them under a
	 * given ref widget.
	 * @author jpk
	 */
	public enum PopupState {
		SHOWING,
		HIDING,
		EITHER;
	}

	/**
	 * the singleton instance
	 */
	private static final MsgManager instance = new MsgManager();

	public static final MsgManager get() {
		return instance;
	}

	/**
	 * Cache of all managed {@link MsgPanel}s keyed by ui element to which
	 * {@link MsgPanel}s are bound.
	 */
	private final Map<Element, MsgPanel> msgPanels = new HashMap<Element, MsgPanel>();

	/**
	 * List of showing {@link MsgPanel}s during a dragging op.
	 */
	private transient List<MsgPanel> draggingMsgPanels;

	/**
	 * Constructor
	 */
	public MsgManager() {
		super();
	}

	/**
	 * Posts a list of {@link Msg}s that will be bound to a given {@link Widget}.
	 * @param msgs The messages to post
	 * @param refWidget The target Widget.
	 * @param clearExisting Clear out existing messages bound to the given target
	 *        Widget first?
	 * @return Never <code>null</code> message operator.
	 * @throws IllegalArgumentException When no messages are specified or the
	 *         given reference is <code>null</code>.
	 */
	public IMsgOperator post(Iterable<Msg> msgs, Widget refWidget,
			boolean clearExisting) throws IllegalArgumentException {
		if(msgs == null) {
			throw new IllegalArgumentException("No messages specified.");
		}
		if(refWidget == null) {
			throw new IllegalArgumentException("A reference widget must be specified.");
		}
		MsgPanel mp = msgPanels.get(refWidget.getElement());
		if(mp == null) {
			mp = new MsgPanel(refWidget.getElement());
			msgPanels.put(refWidget.getElement(), mp);
		}
		if(clearExisting) mp.clearMsgs();
		mp.addMsgs(msgs);
		return mp;
	}

	/**
	 * Posts a single {@link Msg} that will be bound to a given {@link Widget}.
	 * @param msg The message to post
	 * @param refWidget The target Widget.
	 * @param clearExisting Clear out existing messages bound to the given target
	 *        Widget first?
	 * @return Never <code>null</code> message operator.
	 * @throws IllegalArgumentException When either the msg or the reference
	 *         widget is not specified.
	 */
	public IMsgOperator post(Msg msg, Widget refWidget, boolean clearExisting) {
		if(msg == null) {
			throw new IllegalArgumentException("No message specified.");
		}
		final List<Msg> msgs = new ArrayList<Msg>(1);
		msgs.add(msg);
		return post(msgs, refWidget, clearExisting);
	}

	/**
	 * Finds the msg panel bound the given widget returning <code>null</code> if
	 * no msg panel is bound.
	 * @param refWidget The ref widget
	 * @return The bound operator or <code>null</code> if no operators are bound.
	 */
	public IMsgOperator findMsgOperator(Widget refWidget) {
		return msgPanels.get(refWidget.getElement());
	}

	/**
	 * Finds all {@link IMsgOperator}s <em>contained in</em> the given Widget
	 * filtered by the given popup state.
	 * @param w The widget If <code>null</code>, <code>null</code> is returned
	 * @param drillDown Search child elements possibly held under the given
	 *        widget?
	 * @param state The desired MsgPanel state which filters the results
	 * @return Never <code>null</code> list of bound {@link IMsgOperator}s. If
	 *         emtpty, no operators were found bound.
	 */
	public IMsgOperator findMsgOperators(Widget w, boolean drillDown, PopupState state) {
		return new MsgOperatorAggregate(findMsgPanels(w, drillDown, state));
	}

	/**
	 * Clears all found message panels bound to the given widget and, optionally,
	 * all nested widgets.
	 * @param w The Widget
	 * @param drillDown clear out message panels in nested widgets as well?
	 */
	public void clear(Widget w, boolean drillDown) {
		if(drillDown) {
			final List<MsgPanel> list = findMsgPanels(w, drillDown, PopupState.EITHER);
			if(list != null) {
				for(final MsgPanel mp : list) {
					mp.hide();
					msgPanels.remove(mp);
				}
			}
		}
		else {
			final MsgPanel mp = msgPanels.get(w.getElement());
			if(mp != null) {
				mp.hide();
				msgPanels.remove(mp);
			}
		}
	}

	/**
	 * Clears all content from all referenced {@link IMsgOperator}s.
	 */
	public void clear() {
		for(final MsgPanel mp : msgPanels.values()) {
			mp.hide();
		}
		msgPanels.clear();
	}

	/**
	 * Finds all {@link MsgPanel}s <em>contained in</em> the given Widget.
	 * @param w The widget to check
	 * @param drillDown Search child elements under the given widget element?
	 * @param state The desired MsgPanel state which filters the results
	 * @return Never <code>null</code> list of bound {@link IMsgOperator}s or
	 *         <code>null</code> which may be empy indicating no operators are
	 *         bound.
	 */
	private List<MsgPanel> findMsgPanels(Widget w, boolean drillDown, PopupState state) {
		final List<MsgPanel> list = new ArrayList<MsgPanel>();
		
		final Element re = w.getElement();
		for(final Element key : msgPanels.keySet()) {
			final MsgPanel mp = msgPanels.get(key);
			assert mp != null;
			if(key == re || (drillDown && re.isOrHasChild(key))) {
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

	public void onDrag(DragEvent event) {
		switch(event.dragMode) {
			case DRAGGING:
				break;
			case START:
				assert draggingMsgPanels == null;
				// tell the contained msg panels we are dragging
				draggingMsgPanels = findMsgPanels((Widget) event.getSource(), true, PopupState.SHOWING);
				for(final IMsgOperator mp : draggingMsgPanels) {
					mp.hide();
				}
				break;
			case END:
				if(draggingMsgPanels != null) {
					for(final IMsgOperator mp : draggingMsgPanels) {
						mp.show();
					}
					draggingMsgPanels = null;
				}
				break;
		}
	}

	public void onScroll(ScrollEvent event) {
		// hide all showing msg panels contained in the widget
		final List<MsgPanel> list = findMsgPanels((Widget) event.getSource(), true, PopupState.EITHER);
		if(list != null) {
			for(final IMsgOperator mp : list) {
				mp.hide();
			}
		}
	}
}
