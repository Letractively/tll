/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2009
 */
package com.tll.client.ui.msg;

import java.util.HashSet;
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
 * responsible for clearing ultimately clearing them in the registry!</b>
 * @author jpk
 */
public final class MsgPopupRegistry {

	/**
	 * The managed cache of popups for this registry.
	 */
	private final Set<MsgPopup> popups = new HashSet<MsgPopup>();

	/**
	 * Factory type method providing an {@link IMsgOperator} for all message
	 * popups bound either to it or any dom-wise child widget.
	 * @param w The targeted widget
	 * @param drillDown if <code>true</code>, all dom-wise nested message popups
	 *        will be bound to the returned operator. if <code>false</code>, only
	 *        the message popup for the given widget is bound.
	 * @return Newly created collective message operator.
	 */
	public IMsgOperator getOperator(Widget w, boolean drillDown) {
		return new MsgOperatorFlyweight(find(w, drillDown));
	}

	/**
	 * Add a message popup to a target widget retaining its reference to manage
	 * its lifecycle by this registry.
	 * @param msg the message to add
	 * @param w the target widget
	 * @param clearExisting clear existing messages first?
	 */
	public void addMsg(Msg msg, Widget w, boolean clearExisting) {
		final MsgPopup mp = getMsgPopup(w);
		if(clearExisting) mp.clearMsgs();
		mp.addMsg(msg);
	}

	/**
	 * Add a collection of messages to a target widget retaining their references
	 * to manage their lifecycles by this registry.
	 * @param msgs the messages to add
	 * @param w the target widget
	 * @param clearExisting clear existing messages first?
	 */
	public void addMsgs(Iterable<Msg> msgs, Widget w, boolean clearExisting) {
		final MsgPopup mp = getMsgPopup(w);
		if(clearExisting) mp.clearMsgs();
		mp.addMsgs(msgs);
	}

	/**
	 * This method clears out all cached message popups from this registry.
	 */
	public void clear() {
		popups.clear();
	}

	/**
	 * Provides a never-<code>null</code> set of message popups whose ref widget
	 * either matches the given widget or is a dom-wise child of the given widget
	 * when the drill down flag is <code>true</code>.
	 * @param w
	 * @param drillDown
	 * @return Never-<code>null</code> set of message popups which may be empty
	 */
	private Set<MsgPopup> find(Widget w, boolean drillDown) {
		final HashSet<MsgPopup> set = new HashSet<MsgPopup>();
		for(final MsgPopup mp : popups) {
			if(mp.getRefWidget() == w || (drillDown && (DOM.isOrHasChild(w.getElement(), mp.getRefWidget().getElement())))) {
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
		for(final MsgPopup mp : popups) {
			if(mp.getRefWidget() == w) {
				return mp;
			}
		}
		final MsgPopup mp = new MsgPopup(w);
		popups.add(mp);
		return mp;
	}
}
