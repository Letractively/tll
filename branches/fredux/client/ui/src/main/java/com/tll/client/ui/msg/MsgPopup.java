/**
 * The Logic Lab
 * @author jpk Sep 12, 2007
 */
package com.tll.client.ui.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.DOMExt;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.ImageContainer;
import com.tll.client.ui.P;
import com.tll.client.ui.PopupHideTimer;
import com.tll.client.ui.Position;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * MsgPopup - UI widget designed to display one or more {@link Msg}s.
 * @see Msg
 * @author jpk
 */
final class MsgPopup extends PopupPanel implements IMsgOperator {

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
	private static final MsgLevelImageBundle msgLevelImgBundle =
			(MsgLevelImageBundle) GWT.create(MsgLevelImageBundle.class);

	/**
	 * The DOM element property signifying the associated msg level. This property
	 * is set for all created sub-panels of {@link #container}.
	 */
	private static final String ELEM_PROP_MSG_LEVEL = "_ml";
	
	private static final Position DEFAULT_POSITION = Position.BOTTOM;

	/**
	 * Contains sub-panels categorized my {@link MsgLevel}.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * The non-<code>null</code> reference ui widget.
	 */
	private Widget refWidget;

	private int duration = -1;

	private Position position = DEFAULT_POSITION;

	/**
	 * Used to schedule hiding of the popup when a duration is specified for
	 * showing the popup.
	 */
	private PopupHideTimer hideTimer;

	/**
	 * Constructor
	 */
	public MsgPopup() {
		super(false, false);
		container.setStyleName(Styles.MSG);
		setWidget(container);
	}

	/**
	 * Constructor
	 * @param refWidget
	 */
	public MsgPopup(Widget refWidget) {
		this();
		setRefWidget(refWidget);
	}

	/**
	 * @return the reference element
	 */
	public Widget getRefWidget() {
		return refWidget;
	}

	/**
	 * Sets the reference element.
	 * @param refWidget the reference element. Can't be <code>null</code>.
	 */
	public void setRefWidget(Widget refWidget) {
		if(refWidget == null) throw new IllegalArgumentException();
		this.refWidget = refWidget;
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

	@Override
	public void showMsgs(Position position, int milliDuration, boolean showMsgLevelImages) {
		setPosition(position);
		setDuration(milliDuration);
		setShowMsgLevelImages(showMsgLevelImages);
		showMsgs(true);
	}

	void setDuration(int milliseconds) {
		this.duration = milliseconds;
	}

	void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public void showMsgs(boolean show) {
		if(show) {
			// make sure we aren't currently cloaked!
			if(!DOMExt.isCloaked(refWidget.getElement())) {
				setAutoHideEnabled(duration <= 0);
				assert refWidget != null;
				setPopupPositionAndShow(new PositionCallback() {

					@SuppressWarnings("synthetic-access")
					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = 0, top = 0;
						final int rel = refWidget.getAbsoluteLeft();
						final int ret = refWidget.getAbsoluteTop();
						switch(position) {
							default:
							case BOTTOM:
								// position the msg panel left-aligned and directly beneath the ref
								// widget
								left = rel;
								top = ret + refWidget.getOffsetHeight();
								break;
							case CENTER: {
								left = rel + (refWidget.getOffsetWidth() / 2) - (offsetWidth / 2);
								top = ret + (refWidget.getOffsetHeight() / 2) - (offsetHeight / 2);
								break;
							}
						}
						setPopupPosition(Math.max(0, left), Math.max(0, top));
					}
				});
				if(duration > 0) {
					if(hideTimer == null) {
						hideTimer = new PopupHideTimer(this);
					}
					hideTimer.schedule(duration);
				}
			}
			else {
				hide();
			}
		}
	}

	public void clearMsgs() {
		hide();
		container.clear();
	}

	void setShowMsgLevelImages(boolean show) {
		FlowPanel mlp;
		for(final Object o : container) {
			mlp = (FlowPanel) o;
			if(show && mlp.getWidgetCount() == 1) {
				// no image so create it
				final MsgLevel level = MsgLevel.values()[mlp.getElement().getPropertyInt(ELEM_PROP_MSG_LEVEL)];
				final Image img = getMsgLevelImage(level);
				// NOTE: since this is a clipped image, the width/height should be known
				final ImageContainer ic = new ImageContainer(img, img.getWidth(), img.getHeight());
				mlp.insert(ic, 0);
			}
			mlp.getWidget(0).setVisible(show);
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

	@Override
	public void onDrag(DragEvent event) {
		switch(event.dragMode) {
			case DRAGGING:
				break;
			case START:
				hide();
				break;
			case END:
				show();
				break;
		}
	}

	@Override
	public void onScroll(ScrollEvent event) {
		hide();
	}
}
