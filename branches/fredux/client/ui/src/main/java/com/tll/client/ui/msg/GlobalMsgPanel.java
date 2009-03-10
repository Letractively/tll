/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.ui.msg;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.IWidgetRef;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * GlobalMsgPanel - Displayes sourced and un-sourced messages that are
 * removable.
 * @author jpk
 */
public class GlobalMsgPanel extends Composite implements IMsgDisplay {

	/**
	 * Styles - (msg.css)
	 * @author jpk
	 */
	static class Styles {

		/**
		 * The root message style
		 */
		public static final String MSG = "msg";
		
		/**
		 * Style for the container widget.
		 */
		public static final String GLOBAL = "gmsg";
	}
	
	private static final MsgLevel[] order = new MsgLevel[] {
		MsgLevel.FATAL, MsgLevel.ERROR, MsgLevel.WARN, MsgLevel.INFO };
	
	private static final int index(MsgLevel level) {
		for(int i = 0; i < order.length; i++) {
			if(order[i] == level) {
				return i;
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * Container for the child msg level panels.
	 */
	private final FlowPanel container;

	/**
	 * Constructor
	 */
	public GlobalMsgPanel() {
		container = new FlowPanel();
		container.addStyleName(Styles.MSG);
		container.addStyleName(Styles.GLOBAL);
		MutableMsgLevelPanel p;
		for(final MsgLevel l : order) {
			p = new MutableMsgLevelPanel(l);
			p.setVisible(false);
			container.add(p);
		}
		initWidget(container);
	}
	
	private MutableMsgLevelPanel getMsgLevelPanel(MsgLevel level) {
		return (MutableMsgLevelPanel) container.getWidget(index(level));
	}

	/**
	 * Add multiple sourced messages.
	 * @param wref
	 * @param msgs
	 */
	public void add(IWidgetRef wref, Iterable<Msg> msgs) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(wref, msgs);
			if(p.size() > 0) p.setVisible(true);
		}
	}
	
	/**
	 * Add a single sourced message.
	 * @param wref
	 * @param msg
	 */
	public void add(IWidgetRef wref, Msg msg) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(wref, msg);
			if(p.size() > 0) p.setVisible(true);
		}
	}

	/**
	 * Add multiple of un-sourced messages.
	 * @param msgs
	 */
	public void add(Iterable<Msg> msgs) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(msgs);
			if(p.size() > 0) p.setVisible(true);
		}
	}
	
	/**
	 * Add a single un-sourced message.
	 * @param msg
	 */
	public void add(Msg msg) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(msg);
			if(p.size() > 0) p.setVisible(true);
		}
	}
	
	/**
	 * Remove all posted messages that source to the given widget.
	 * @param wref the widget reference
	 */
	public void remove(IWidgetRef wref) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.remove(wref);
			if(p.size() == 0) p.setVisible(false);
		}
	}
	
	/**
	 * Remove all posted un-sourced messages.
	 */
	public void removeUnsourced() {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.removeUnsourced();
			if(p.size() == 0) p.setVisible(false);
		}
	}
	
	/**
	 * Clear all messages of the given level.
	 * @param level
	 */
	public void clear(MsgLevel level) {
		final MutableMsgLevelPanel p = getMsgLevelPanel(level);
		p.clear();
		p.setVisible(false);
	}

	/**
	 * Remove <em>all</em> messages.
	 */
	public void clear() {
		for(final MsgLevel level : order) {
			clear(level);
		}
	}

	/**
	 * @param level
	 * @return the number of posted messages of the given level.
	 */
	public int size(MsgLevel level) {
		return getMsgLevelPanel(level).size();
	}

	/**
	 * @return the total number of posted messages.
	 */
	public int size() {
		int c = 0;
		for(final MsgLevel l : order) {
			c += size(l);
		}
		return c;
	}
}
