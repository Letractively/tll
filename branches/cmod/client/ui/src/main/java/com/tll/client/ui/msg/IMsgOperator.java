/**
 * The Logic Lab
 * @author jpk Feb 17, 2009
 */
package com.tll.client.ui.msg;

import com.tll.client.ui.Position;
import com.tll.common.msg.Msg;

/**
 * IMsgOperator - Allows clients to access and manipulate UI messages whose
 * life-cycle is managed by the {@link MsgManager}.
 * @author jpk
 */
public interface IMsgOperator {

	/**
	 * Removes all contained {@link Msg}s bound to this operator.
	 */
	void clearMsgs();
	
	/**
	 * Show the messages bound to this operator.
	 */
	void show();

	/**
	 * Show the messages bound to this operator for a given length of time at the
	 * given relative position.
	 * @param position The relative position
	 * @param duration The time in mili-seconds to show the popup message
	 */
	void show(Position position, int duration);
	
	/**
	 * Hide the messages bound to this operator.
	 */
	void hide();

	/**
	 * Toggles message visibility.
	 */
	void toggle();

	/**
	 * Toggles the display of message level images for messages bound to this
	 * operator.
	 * @param show Show or hide the message level images?
	 */
	void showMsgLevelImages(boolean show);

}