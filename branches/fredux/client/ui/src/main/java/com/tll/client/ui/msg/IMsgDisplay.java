/**
 * The Logic Lab
 * @author jpk
 * @since Mar 9, 2009
 */
package com.tll.client.ui.msg;

import com.tll.client.ui.IWidgetRef;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * IMsgDisplay - Generic definition for posting and removing messages in the ui
 * of varying type.
 * @author jpk
 */
public interface IMsgDisplay {

	/**
	 * Add multiple sourced messages.
	 * @param wref
	 * @param msgs
	 */
	void add(IWidgetRef wref, Iterable<Msg> msgs);

	/**
	 * Add a single sourced message.
	 * @param wref
	 * @param msg
	 */
	void add(IWidgetRef wref, Msg msg);

	/**
	 * Add multiple of un-sourced messages.
	 * @param msgs
	 */
	void add(Iterable<Msg> msgs);

	/**
	 * Add a single un-sourced message.
	 * @param msg
	 */
	void add(Msg msg);

	/**
	 * Remove all posted messages that source to the given widget.
	 * @param wref the widget reference
	 */
	void remove(IWidgetRef wref);

	/**
	 * Remove all posted un-sourced messages.
	 */
	void removeUnsourced();

	/**
	 * Clear all messages of the given level.
	 * @param level
	 */
	void clear(MsgLevel level);

	/**
	 * Remove <em>all</em> messages.
	 */
	void clear();

	/**
	 * @param level
	 * @return the number of posted messages of the given level.
	 */
	int size(MsgLevel level);

	/**
	 * @return the total number of posted messages.
	 */
	int size();

}