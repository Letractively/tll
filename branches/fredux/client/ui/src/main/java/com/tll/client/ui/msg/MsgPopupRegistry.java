/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2009
 */
package com.tll.client.ui.msg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.tll.common.msg.Msg;

/**
 * MsgPopupRegistry - Cache of message popups each referencing a widget by which
 * it is positioned.
 * <p>
 * Since this type of registry caches these popups, thier life-cycle is
 * "extended" beyond hiding the popup. As such, <b>the registry client is
 * responsible for ultimately clearing them in this registry.</b>
 * @author jpk
 */
public final class MsgPopupRegistry {

	/**
	 * The managed cache of popups for this registry keyed by ref widget.
	 */
	private final Map<Widget, MsgPopup> cache = new HashMap<Widget, MsgPopup>();
	
	/**
	 * Factory type method providing an {@link IMsgOperator} for all message
	 * popups bound either to it or any dom-wise child widget.
	 * @param w The targeted widget
	 * @param drillDown if <code>true</code>, all dom-wise nested message popups
	 *        will be bound to the returned operator. if <code>false</code>, only
	 *        the message popup for the given widget is bound.
	 * @return a message operator.
	 */
	public IMsgOperator getOperator(Widget w, boolean drillDown) {
		return drillDown ? new MsgOperatorFlyweight(drill(w)) : getMsgPopup(w);
	}

	/**
	 * Add a message popup to a target widget retaining its reference to manage
	 * its lifecycle by this registry.
	 * @param msg the message to add
	 * @param w the target widget
	 * @param clearExisting clear existing messages first?
	 * @return the associated operator for the given widget
	 */
	public IMsgOperator addMsg(Msg msg, Widget w, boolean clearExisting) {
		final MsgPopup mp = getMsgPopup(w);
		if(clearExisting) mp.clearMsgs();
		mp.addMsg(msg);
		return mp;
	}

	/**
	 * Add a collection of messages to a target widget retaining their references
	 * to manage their lifecycles by this registry.
	 * @param msgs the messages to add
	 * @param w the target widget
	 * @param clearExisting clear existing messages first?
	 * @return the associated operator for the given widget
	 */
	public IMsgOperator addMsgs(Iterable<Msg> msgs, Widget w, boolean clearExisting) {
		final MsgPopup mp = getMsgPopup(w);
		if(clearExisting) mp.clearMsgs();
		mp.addMsgs(msgs);
		return mp;
	}

	/**
	 * This method clears out all cached message popups from this registry.
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * Provides a never-<code>null</code> set of message popups whose ref widget
	 * either matches the given widget or is a dom-wise child of the given widget.
	 * @param w
	 * @return Never-<code>null</code> set of message popups which may be empty
	 */
	private Set<MsgPopup> drill(Widget w) {
		final HashSet<MsgPopup> set = new HashSet<MsgPopup>();
		for(final MsgPopup mp : cache.values()) {
			if(mp.getRefWidget() == w || ((DOM.isOrHasChild(w.getElement(), mp.getRefWidget().getElement())))) {
				set.add(mp);
			}
		}
		return set;
	}
	
	/**
	 * Searches the held cache of popups for the one that targets the given
	 * widget. If one exists, it is returned otherwise one is created, added the
	 * cache then returned.
	 * @param w The target widget
	 * @return The never <code>null</code> bound message popup.
	 */
	private MsgPopup getMsgPopup(Widget w) {
		MsgPopup mp = cache.get(w);
		if(mp == null) {
			mp = new MsgPopup(w);
			cache.put(w, mp);
		}
		return mp;
	}
}
