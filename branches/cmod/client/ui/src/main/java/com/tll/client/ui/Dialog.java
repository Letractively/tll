/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Dialog
 * @author jpk
 */
public class Dialog extends DialogBox {

	private final HasFocus focusOnCloseWidget;

	private final boolean showOverlay;

	/**
	 * Constructor
	 */
	public Dialog() {
		this(null, false);
	}

	/**
	 * Constructor
	 * @param focusOnCloseWidget
	 * @param showOverlay
	 */
	public Dialog(HasFocus focusOnCloseWidget, boolean showOverlay) {
		super(false, true);
		this.focusOnCloseWidget = focusOnCloseWidget;
		this.showOverlay = showOverlay;
	}

	@Override
	public void show() {
		// we check for is attached (i.e. is showing) to avoid suprious call to
		// App.busy() which will invalidate the glass panel counting mechanism
		if(!isAttached()) {
			if(showOverlay) UI.busy();
			super.show();
		}
	}

	@Override
	public void hide() {
		// we check for is attached (i.e. is showing) to avoid suprious call to
		// App.unbusy() which will invalidate the glass panel counting mechanism
		if(isAttached()) {
			super.hide();
			if(showOverlay) UI.unbusy();
			if(focusOnCloseWidget != null) {
				DeferredCommand.addCommand(new FocusCommand(focusOnCloseWidget, true));
			}
		}
	}

	@Override
	public boolean onEventPreview(Event event) {
		final boolean rval = super.onEventPreview(event);
		if(rval) {
			if(event.getTypeInt() == Event.ONKEYDOWN) {
				if(event.getKeyCode() == KeyboardListener.KEY_ESCAPE) {
					hide();
				}

			}
		}
		return rval;
	}

}
