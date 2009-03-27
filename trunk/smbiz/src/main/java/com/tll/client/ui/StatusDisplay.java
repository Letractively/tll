/**
 * The Logic Lab
 * @author jpk Sep 1, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.tll.client.data.rpc.IStatusHandler;
import com.tll.client.data.rpc.StatusEvent;
import com.tll.client.data.rpc.StatusEventDispatcher;
import com.tll.common.data.Status;
import com.tll.common.msg.Msg;

/**
 * StatusDisplay - Console like window that displays messages contained w/in a
 * Status object.
 * @author jpk
 */
public class StatusDisplay extends Composite implements IStatusHandler {

	/**
	 * Styles - (status.css)
	 * @author jpk
	 */
	protected static class Styles {

		public static final String STATUS_DISPLAY = "statusDisplay";
	}

	private static StatusDisplay firstStatusDisplay = null;

	/**
	 * Client side logging provision
	 * @param msg The msg to log to the {@link StatusDisplay}
	 */
	public static void log(Msg msg) {
		if(firstStatusDisplay != null) {
			firstStatusDisplay.handleMsg(msg);
		}
	}

	private final ScrollPanel sp = new ScrollPanel();
	private final VerticalPanel vp = new VerticalPanel();

	/**
	 * Constructor
	 */
	public StatusDisplay() {
		super();
		sp.setStylePrimaryName(Styles.STATUS_DISPLAY);
		sp.setTitle("Status History");
		sp.add(vp);
		initWidget(sp);

		if(firstStatusDisplay == null) {
			firstStatusDisplay = this;
		}
	}

	private void handleMsg(Msg msg) {
		vp.insert(new StatusMsgDisplay(msg), 0);
	}

	private void handleStatus(Status status) {
		if(status.getNumTotalMsgs() > 0) {
			for(Msg msg : status.getAllMsgs()) {
				handleMsg(msg);
			}
		}
	}

	public void onStatusEvent(StatusEvent event) {
		Status status = event.getStatus();
		if(status != null) {
			handleStatus(status);
		}
	}

	private static final class StatusMsgDisplay extends Composite {

		private final Label msg;

		public StatusMsgDisplay(Msg statusMsg) {
			msg = new Label(statusMsg.getMsg());
			msg.setStylePrimaryName(statusMsg.getLevel().getName().toLowerCase());
			initWidget(msg);
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		StatusEventDispatcher.get().addStatusHandler(this);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		StatusEventDispatcher.get().removeStatusHandler(this);
	}

}
