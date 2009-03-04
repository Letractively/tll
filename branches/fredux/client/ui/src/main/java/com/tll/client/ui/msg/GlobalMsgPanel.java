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
 * GlobalMsgPanel - Displays messages associated with a particular referencable
 * widget.
 * @author jpk
 */
public class GlobalMsgPanel extends Composite {

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

	public void add(IWidgetRef wref, Iterable<Msg> msgs) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(wref, msgs);
			if(p.size() > 0) p.setVisible(true);
		}
	}
	
	public void add(Iterable<Msg> msgs) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.add(msgs);
			if(p.size() > 0) p.setVisible(true);
		}
	}
	
	public void remove(IWidgetRef wref) {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.remove(wref);
			if(p.size() == 0) p.setVisible(false);
		}
	}
	
	public void removeUnsourced() {
		MutableMsgLevelPanel p;
		for(final MsgLevel level : order) {
			p = getMsgLevelPanel(level);
			p.removeUnsourced();
			if(p.size() == 0) p.setVisible(false);
		}
	}
	
	public void clear(MsgLevel level) {
		final MutableMsgLevelPanel p = getMsgLevelPanel(level);
		p.clear();
		p.setVisible(false);
	}

	public void clear() {
		for(final MsgLevel level : order) {
			clear(level);
		}
	}

	public int size(MsgLevel level) {
		return getMsgLevelPanel(level).size();
	}

	public int size() {
		int c = 0;
		for(final MsgLevel l : order) {
			c += size(l);
		}
		return c;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}
}
