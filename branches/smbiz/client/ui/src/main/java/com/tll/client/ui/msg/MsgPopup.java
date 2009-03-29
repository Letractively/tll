/**
 * The Logic Lab
 * @author jpk Sep 12, 2007
 */
package com.tll.client.ui.msg;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.DOMExt;
import com.tll.client.ui.DragEvent;
import com.tll.client.ui.PopupHideTimer;
import com.tll.client.ui.Position;
import com.tll.common.msg.Msg;

/**
 * MsgPopup - UI widget designed to display one or more {@link Msg}s.
 * @see Msg
 * @author jpk
 */
final class MsgPopup extends PopupPanel implements IMsgOperator {

	private static final Position DEFAULT_POSITION = Position.BOTTOM;

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
	
	private final SimpleMsgPanel msgPanel = new SimpleMsgPanel();

	/**
	 * Constructor
	 */
	public MsgPopup() {
		super(false, false);
		setWidget(msgPanel);
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
		msgPanel.addMsg(msg);
	}

	/**
	 * Adds multiple {@link Msg}s to this panel.
	 * <p>
	 * NOTE: {@link Msg}s are added in the order based on the order of the
	 * provided iterable.
	 * @param msgs
	 */
	public void addMsgs(Iterable<Msg> msgs) {
		msgPanel.addMsgs(msgs);
	}

	@Override
	public void showMsgs(Position position, int milliDuration, boolean showMsgLevelImages) {
		setPosition(position);
		setDuration(milliDuration);
		msgPanel.setShowMsgLevelImages(showMsgLevelImages);
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
		}
		else {
			hide();
		}
	}

	public void clearMsgs() {
		hide();
		msgPanel.clear();
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