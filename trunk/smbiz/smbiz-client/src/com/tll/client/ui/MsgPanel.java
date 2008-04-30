/**
 * The Logic Lab
 * @author jpk Sep 12, 2007
 */
package com.tll.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.msg.Msg;
import com.tll.client.msg.Msg.MsgLevel;

/**
 * MsgPanel - UI widget designed to display one or more {@link Msg}s.
 * @see Msg
 * @author jpk
 */
public final class MsgPanel extends TimedPositionedPopup {

	/*
	 * Reference: msg.css
	 */
	private static final String CSS_MSG = "msg";

	/**
	 * The DOM element property signifying the associated msg level. This property
	 * is set for all created sub-panels of {@link #container}.
	 */
	private static final String ELEM_PROP_MSG_LEVEL = "_ml";

	private final FlowPanel container = new FlowPanel();
	private final boolean showImage;

	/**
	 * Constructor
	 * @param autoHide
	 * @param position
	 * @param refElement
	 * @param duration
	 */
	public MsgPanel(boolean autoHide, Position position, Element refElement, int duration, boolean showImage) {
		super(autoHide, false, duration, refElement, position);
		this.showImage = showImage;
		container.setStyleName(CSS_MSG);
		setWidget(container);
	}

	/**
	 * Gets the msg sub-panel associated with the given msg level. If not present,
	 * it is created.
	 * @return
	 */
	private FlowPanel getOrCreateMsgLevelPanel(MsgLevel level) {
		for(Object element2 : container) {
			FlowPanel mlsp = (FlowPanel) element2;
			int i = mlsp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL);
			if(level.ordinal() == i) return mlsp;
		}
		// stub the msg level panel
		// (child widget FORMAT: [{msg level img}]{ul html list of msg texts})
		FlowPanel msgLevelPanel = new FlowPanel();
		msgLevelPanel.addStyleName(CSS_MSG);
		msgLevelPanel.addStyleName(level.getName().toLowerCase());
		msgLevelPanel.getElement().setPropertyInt(ELEM_PROP_MSG_LEVEL, level.ordinal());
		if(showImage) {
			Image img = getMsgLevelImage(level);
			// NOTE: since this is a clipped image, the width/height should be known
			ImageContainer ic = new ImageContainer(img, img.getWidth(), img.getHeight());
			ic.addStyleName(CSS.IMAGE_CONTAINER);
			msgLevelPanel.add(ic);
		}
		msgLevelPanel.add(new HtmlListPanel(false));
		container.add(msgLevelPanel);
		return msgLevelPanel;
	}

	/**
	 * Toggles this panel's visibility via calling either {@link #show()} or
	 * {@link #hide()} depending on the return value of {@link #isShowing()}.
	 */
	public void toggle() {
		if(isShowing())
			hide();
		else
			show();
	}

	/**
	 * Provides a new {@link Image} containing the associated msg level icon.
	 * @param level The message level
	 * @return Image
	 */
	private Image getMsgLevelImage(MsgLevel level) {
		Image img = new Image();
		switch(level) {
			case WARN:
				App.imgs().warn().applyTo(img);
				break;
			case ERROR:
				App.imgs().error().applyTo(img);
				break;
			case FATAL:
				App.imgs().fatal().applyTo(img);
				break;
			default:
			case INFO:
				App.imgs().info().applyTo(img);
				break;
		}
		return img;
	}

	/**
	 * Adds a single {@link Msg} to this panel.
	 * @param msg
	 */
	public void addMsg(Msg msg) {
		FlowPanel msgLevelPanel = getOrCreateMsgLevelPanel(msg.getLevel());
		HtmlListPanel hlp = (HtmlListPanel) (showImage ? msgLevelPanel.getWidget(1) : msgLevelPanel.getWidget(0));
		hlp.add(new P(msg.getMsg()));
	}

	/**
	 * Adds multiple {@link Msg}s to this panel.
	 * <p>
	 * NOTE: {@link Msg}s are added in the order based on the order of the
	 * provided list.
	 * @param msgs
	 */
	public void addMsgs(List<Msg> msgs) {
		if(msgs == null) return;
		for(Msg msg : msgs) {
			addMsg(msg);
		}
	}

	/**
	 * Removes all {@link Msg}s from this panel.
	 */
	public void removeMsgs() {
		for(Object element2 : container) {
			container.remove((Widget) element2);
		}
	}
}
