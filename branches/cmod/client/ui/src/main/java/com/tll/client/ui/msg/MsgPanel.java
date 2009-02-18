/**
 * The Logic Lab
 * @author jpk Sep 12, 2007
 */
package com.tll.client.ui.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.ImageContainer;
import com.tll.client.ui.P;
import com.tll.client.ui.PopupHideTimer;
import com.tll.client.ui.PopupPositioner;
import com.tll.client.ui.Position;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * MsgPanel - UI widget designed to display one or more {@link Msg}s.
 * @see Msg
 * @author jpk
 */
final class MsgPanel extends PopupPanel implements IMsgOperator {

	/**
	 * MsgLevelImageBundle - Images for {@link Msg} related UI artifacts.
	 * @author jpk
	 */
	interface MsgLevelImageBundle extends ImageBundle {

		/**
		 * info
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/info.gif")
		AbstractImagePrototype info();

		/**
		 * warn
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/warn.gif")
		AbstractImagePrototype warn();

		/**
		 * error
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/error.gif")
		AbstractImagePrototype error();

		/**
		 * fatal
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/fatal.gif")
		AbstractImagePrototype fatal();
	}

	/**
	 * Styles - (msg.css)
	 * @author jpk
	 */
	private static class Styles {

		/**
		 * Style applied to to widgets containing messages.
		 */
		public static final String MSG = "msg";

	}

	/**
	 * The message level image bundle instance.
	 */
	private static final MsgLevelImageBundle msgLevelImgBundle = (MsgLevelImageBundle) GWT.create(MsgLevelImageBundle.class);

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
	 * The non-<code>null</code> reference ui element needed for positioning.
	 */
	private Element refElement;
	
	/**
	 * Used to schedule hiding of the popup when a duration is specified for
	 * showing the popup.
	 */
	private PopupHideTimer hideTimer;

	/**
	 * Constructor
	 * @param refElement
	 */
	public MsgPanel(Element refElement) {
		super(false, false);
		container.setStyleName(Styles.MSG);
		setWidget(container);
		setRefElement(refElement);
	}

	/**
	 * @return the reference element
	 */
	public Element getRefElement() {
		return refElement;
	}

	/**
	 * Sets the reference element.
	 * @param refElement the reference element. Can't be <code>null</code>.
	 */
	public void setRefElement(Element refElement) {
		if(refElement == null) throw new IllegalArgumentException();
		this.refElement = refElement;
	}

	/**
	 * Adds a single {@link Msg} to this panel.
	 * @param msg
	 */
	public void addMsg(Msg msg) {
		final FlowPanel msgLevelPanel = getOrCreateMsgLevelPanel(msg.getLevel());
		final HtmlListPanel hlp =
				(HtmlListPanel) ((msgLevelPanel.getWidgetCount() == 2) ? msgLevelPanel.getWidget(1) : msgLevelPanel
						.getWidget(0));
		hlp.add(new P(msg.getMsg()));
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
	
	public void clearMsgs() {
		container.clear();
	}
	
	@Override
	public void show() {
		show(PopupPositioner.DEFAULT_POSITIONING, -1);
	}

	@Override
	public void show(Position position, int duration) {
		PopupPositioner.setPopupPosition(this, refElement, position);
		super.show();
		if(duration > 0) {
			if(hideTimer == null) {
				hideTimer = new PopupHideTimer(this);
			}
			hideTimer.schedule(duration);
		}
	}

	public void toggle() {
		if(isShowing())
			hide();
		else
			show();
	}

	public void showMsgLevelImages(boolean show) {
		FlowPanel mlp;
		for(final Object o : container) {
			mlp = (FlowPanel) o;
			if(show) {
				if(mlp.getWidgetCount() == 1) {
					// no image so create it
					final MsgLevel level = MsgLevel.values()[mlp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL)];
					final Image img = getMsgLevelImage(level);
					// NOTE: since this is a clipped image, the width/height should be known
					final ImageContainer ic = new ImageContainer(img, img.getWidth(), img.getHeight());
					mlp.insert(ic, 0);
				}
				mlp.getWidget(0).setVisible(true);
			}
			else {
				if(mlp.getWidgetCount() == 2) {
					mlp.getWidget(0).setVisible(false);
				}
			}
		}
	}

	/**
	 * Gets the msg sub-panel associated with the given msg level. If not present,
	 * it is created.
	 * @return
	 */
	private FlowPanel getOrCreateMsgLevelPanel(MsgLevel level) {
		FlowPanel mlp;
		for(final Object o : container) {
			mlp = (FlowPanel) o;
			final int i = mlp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL);
			if(level.ordinal() == i) return mlp;
		}
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

	/**
	 * Provides a new {@link Image} containing the associated msg level icon.
	 * @param level The message level
	 * @return Image
	 */
	private Image getMsgLevelImage(MsgLevel level) {
		final Image img = new Image();
		switch(level) {
			case WARN:
				msgLevelImgBundle.warn().applyTo(img);
				break;
			case ERROR:
				msgLevelImgBundle.error().applyTo(img);
				break;
			case FATAL:
				msgLevelImgBundle.fatal().applyTo(img);
				break;
			default:
			case INFO:
				msgLevelImgBundle.info().applyTo(img);
				break;
		}
		return img;
	}
}
