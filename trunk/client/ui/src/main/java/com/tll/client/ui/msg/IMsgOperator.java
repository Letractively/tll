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
	 * Sets the message positioning scheme relative to a targeted ui element.
	 * @param position The relative positioning scheme
	 */
	void setPosition(Position position);

	/**
	 * Sets the length of time in milliseconds that messages are shown.
	 * <p>
	 * If <code>-1</code>, the message is shown indefinitely until the user clicks
	 * outside the popup (i.e.: The <code>autoHide</code> popup property is set to
	 * <code>true</code> in this case).
	 * @param milliseconds the show duration in milliseconds
	 */
	void setDuration(int milliseconds);

	/**
	 * Sets the flag for showing the message level image.
	 * @param show true/false
	 */
	void setShowMsgLevelImages(boolean show);

	/**
	 * Shows the messages.
	 */
	void showMsgs();

	/**
	 * Hide the messages.
	 */
	void hideMsgs();

	/**
	 * Removes all messages permanantly.
	 */
	void clearMsgs();
}