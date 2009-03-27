/**
 * The Logic Lab
 * @author jpk Feb 17, 2009
 */
package com.tll.client.ui.msg;

import com.google.gwt.event.dom.client.ScrollHandler;
import com.tll.client.ui.IDragHandler;
import com.tll.client.ui.Position;

/**
 * IMsgOperator - Provision for clients to manipulate one or more message
 * popups.
 * @author jpk
 */
public interface IMsgOperator extends IDragHandler, ScrollHandler {

	/**
	 * @return <code>true</code> of the message popup(s) are showing.
	 */
	boolean isShowing();

	/**
	 * Shows or hides the messages.
	 * @param show show or hide?
	 */
	void showMsgs(boolean show);

	/**
	 * Sets the msg popup display attributes then shows the popup. This is an all
	 * in one way to set display attributes and show the popup in one call.
	 * <p>
	 * By default they are: {@link Position#BOTTOM}, <code>-1</code>,
	 * <code>false</code>.
	 * @param position The relative positioning scheme relative to the targeted ui
	 *        element.
	 * @param milliDuration the show duration in milliseconds. If <code>-1</code>,
	 *        the message is shown indefinitely until the user clicks outside the
	 *        popup (i.e.: The <code>autoHide</code> popup property is set to
	 *        <code>true</code> in this case).
	 * @param showMsgLevelImages show message level images?
	 */
	void showMsgs(Position position, int milliDuration, boolean showMsgLevelImages);
	
	/**
	 * Removes all messages permanantly.
	 */
	void clearMsgs();
}