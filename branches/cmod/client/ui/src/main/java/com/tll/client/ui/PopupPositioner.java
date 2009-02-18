/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2009
 */
package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * PopupPositioner - Helper class that positions popups given a ui element
 * reference and simple positioning directive.
 * @author jpk
 */
public abstract class PopupPositioner {

	/**
	 * The default positioning.
	 */
	public static final Position DEFAULT_POSITIONING = Position.BOTTOM;

	/**
	 * Set the popup's position relative to a given ui element and positioning
	 * directive.
	 * @param popup The popup to position
	 * @param refElement The reference element
	 * @param position The positioning directive
	 */
	public static void setPopupPosition(PopupPanel popup, Element refElement, Position position) {
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
					popup.setVisible(false);
					popup.show();
					final int ow = popup.getOffsetWidth();
					final int oh = popup.getOffsetHeight();
					popup.hide();
					popup.setVisible(true);
					left = rel + (refElement.getPropertyInt("offsetWidth") / 2) - (ow / 2);
					top = ret + (refElement.getPropertyInt("offsetHeight") / 2) - (oh / 2);
					break;
				}
			}
			if(left < rel) left = rel;
			if(top < ret) top = ret;
		}
		popup.setPopupPosition(left, top);
	}
}
