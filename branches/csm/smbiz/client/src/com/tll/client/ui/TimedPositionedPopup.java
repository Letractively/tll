/**
 * The Logic Lab
 * @author jpk Nov 11, 2007
 */
package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;
import com.tll.client.DOMExt;

/**
 * TimedPositionedPopup - {@link PopupPanel} extension that has logic to
 * position itself in various ways as well as the provision to specify a length
 * of time for the popup to be shown.
 * @author jpk
 */
public abstract class TimedPositionedPopup extends TimedPopup {

	// TODO move for generalization purposes
	public static enum Position {
		/**
		 * {@link #CENTER}: Position the popup in the center of the associated
		 * Widget.
		 */
		CENTER,

		/**
		 * {@link #BOTTOM}: Position the popup beneath and left aligned of the
		 * associated Widget.
		 */
		BOTTOM;
	}

	/**
	 * How this popup is positioned in the client window.
	 */
	protected final Position position;

	/**
	 * The Reference {@link Element} used to determine this panel's position.
	 */
	protected final Element refElement;

	/**
	 * Constructor
	 * @param autoHide
	 * @param modal
	 * @param duration
	 * @param refElement
	 * @param position
	 */
	public TimedPositionedPopup(boolean autoHide, boolean modal, int duration, Element refElement, Position position) {
		super(autoHide, modal, duration);
		this.position = position;
		this.refElement = refElement;
	}

	public Element getRefElement() {
		return refElement;
	}

	public final void setPopupPosition() {
		int left = 0, top = 0;
		if(refElement != null) {
			final int rel = refElement.getAbsoluteLeft();
			final int ret = refElement.getAbsoluteTop();
			switch(position) {
				case BOTTOM:
					// position the msg panel left-aligned and directly beneath the ref
					// widget
					left = rel;
					top = ret + refElement.getPropertyInt("offsetHeight");
					break;
				default:
				case CENTER: {
					setVisible(false);
					doShow();
					int ow = getOffsetWidth();
					int oh = getOffsetHeight();
					doHide();
					setVisible(true);
					left = rel + (refElement.getPropertyInt("offsetWidth") / 2) - (ow / 2);
					top = ret + (refElement.getPropertyInt("offsetHeight") / 2) - (oh / 2);
					break;
				}
			}
			if(left < rel) left = rel;
			if(top < ret) top = ret;
		}
		setPopupPosition(left, top);
	}

	@Override
	public void show() {
		// we don't show the popup if it is being cloaked! otherwise, the calculated
		// positioning is erroneous
		if(refElement == null || DOMExt.isCloaked(refElement)) {
			return;
		}
		setPopupPosition();
		super.show();
	}

}
