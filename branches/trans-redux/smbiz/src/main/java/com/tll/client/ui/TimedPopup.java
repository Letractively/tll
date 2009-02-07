/**
 * The Logic Lab
 * @author jpk Nov 11, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * TimedPopup - {@link PopupPanel} extension that has logic to specify a length of time for the
 * popup to be shown.
 * <p>
 * <strong>NOTE: </strong>We must add a popup listener to monitor when the popup is auto-closed to
 * keep an internal flag in sync with the sub-classes' <code>showing</code> property.
 * @author jpk
 */
public abstract class TimedPopup extends PopupPanel implements PopupListener {

	/**
	 * The time in mili-seconds for the panel to be shown. <br>
	 * NOTE: <code>-1</code> means the panel is shown indefinitely.
	 */
	private final int duration;

	/**
	 * Re-dux of {@link PopupPanel#showing} property but it is private!
	 */
	private boolean tpshowing;

	private final Timer timer = new Timer() {

		@Override
		public void run() {
			hide();
		}

	};

	/**
	 * Constructor
	 * @param autoHide
	 * @param modal
	 * @param duration
	 */
	public TimedPopup(boolean autoHide, boolean modal, int duration) {
		super(autoHide, modal);
		this.duration = duration;
		addPopupListener(this);
	}

	public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
		if(sender == this) {
			tpshowing = false;
		}
	}

	protected void cancelTimer() {
		timer.cancel();
	}

	protected void startTimer() {
		timer.schedule(duration);
	}

	protected final void doShow() {
		super.show();
	}

	protected final void doHide() {
		super.hide();
	}

	@Override
	public void show() {
		if(tpshowing) {
			return;
		}
		tpshowing = true;
		doShow();
		if(duration > 0) {
			startTimer();
		}
	}

	@Override
	public void hide() {
		if(!tpshowing) {
			return;
		}
		tpshowing = false;
		cancelTimer();
		doHide();
	}

	public boolean isShowing() {
		return tpshowing;
	}
}
