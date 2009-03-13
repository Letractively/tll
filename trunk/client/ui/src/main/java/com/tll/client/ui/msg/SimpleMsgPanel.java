/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.ui.msg;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.ImageContainer;
import com.tll.client.ui.P;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;


/**
 * SimpleMsgPanel
 * @author jpk
 */
public class SimpleMsgPanel extends Composite {

	/**
	 * Styles - (msg.css)
	 * @author jpk
	 */
	static class Styles {

		/**
		 * Style applied to to widgets containing messages.
		 */
		public static final String MSG = "msg";

	}

	/**
	 * The DOM element property signifying the associated msg level. This property
	 * is set for all created sub-panels of {@link #container}.
	 */
	private static final String ELEM_PROP_MSG_LEVEL = "_ml";

	/**
	 * Contains sub-panels categorized my {@link MsgLevel}.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * Constructor
	 */
	public SimpleMsgPanel() {
		super();
		container.setStyleName(Styles.MSG);
		initWidget(container);
	}
	
	public void clear() {
		container.clear();
	}
	
	private HtmlListPanel extract(FlowPanel msgLevelPanel) {
		return (HtmlListPanel) ((msgLevelPanel.getWidgetCount() == 2) ? msgLevelPanel.getWidget(1) : msgLevelPanel
				.getWidget(0));
	}

	/**
	 * Adds a single {@link Msg} to this panel.
	 * @param msg
	 */
	public void addMsg(Msg msg) {
		extract(getMsgLevelPanel(msg.getLevel(), true)).append(new P(msg.getMsg()));
	}
	
	/**
	 * Adds multiple {@link Msg}s to this panel.
	 * <p>
	 * NOTE: {@link Msg}s are added in the order based on the order of the
	 * provided iterable.
	 * @param msgs
	 */
	public void addMsgs(Iterable<Msg> msgs) {
		if(msgs != null) {
			for(final Msg msg : msgs) {
				addMsg(msg);
			}
		}
	}

	public void setShowMsgLevelImages(boolean show) {
		FlowPanel mlp;
		for(final Object o : container) {
			mlp = (FlowPanel) o;
			if(show && mlp.getWidgetCount() == 1) {
				// no image so create it
				final MsgLevel level = MsgLevel.values()[mlp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL)];
				// NOTE: since this is a clipped image, the width/height should be known
				mlp.insert(new ImageContainer(Util.getMsgLevelImage(level)), 0);
			}
			mlp.getWidget(0).setVisible(show);
		}
	}

	/**
	 * Gets the msg sub-panel associated with the given msg level. If not present,
	 * it is created.
	 * @param level
	 * @param createIfAbsent
	 * @return the associated message panel or <code>null<code>
	 */
	private FlowPanel getMsgLevelPanel(MsgLevel level, boolean createIfAbsent) {
		FlowPanel mlp;
		for(final Object o : container) {
			mlp = (FlowPanel) o;
			final int i = mlp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL);
			if(level.ordinal() == i) return mlp;
		}
		if(!createIfAbsent) return null;
		// stub the msg level panel
		// (child widget FORMAT: [{msg level img}]{ul html list of msg texts})
		mlp = new FlowPanel();
		mlp.addStyleName(Styles.MSG);
		mlp.addStyleName(level.getName().toLowerCase());
		mlp.getElement().setPropertyInt(ELEM_PROP_MSG_LEVEL, level.ordinal());
		mlp.add(new HtmlListPanel(false));
		container.add(mlp);
		return mlp;
	}
}