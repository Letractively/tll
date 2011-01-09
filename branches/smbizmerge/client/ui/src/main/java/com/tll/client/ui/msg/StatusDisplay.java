/**
 * The Logic Lab
 * @author jpk Sep 1, 2007
 */
package com.tll.client.ui.msg;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Status;

/**
 * Console like window that displays messages contained w/in a Status object.
 * @author jpk
 */
public class StatusDisplay extends Composite {

	interface MyUiBinder extends UiBinder<Widget, StatusDisplay> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * StatusMsgDisplay
	 * @author jpk
	 */
	public static final class StatusMsgDisplay extends Composite {

		private final Label msg;

		public StatusMsgDisplay(Msg statusMsg) {
			msg = new Label(statusMsg.getMsg());
			msg.setStyleName(MsgStyles.getMsgLevelStyle(statusMsg.getLevel()));
			initWidget(msg);
		}
	}

	@UiField ScrollPanel sp;
	@UiField VerticalPanel vp;
	private int attribs;

	/**
	 * Constructor
	 */
	public StatusDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		sp.setTitle("Status History");
	}

	/**
	 * @return the attribs filter
	 */
	public int getAttribs() {
		return attribs;
	}

	/**
	 * Set the attribs filter for subsequent message processing.
	 * @param attribs
	 */
	public void setAttribs(int attribs) {
		this.attribs = attribs;
	}

	/**
	 * Adds status messages to the display filtering them according to the current
	 * state of the attribs data member.
	 * @param status
	 */
	public void addStatus(Status status) {
		final List<Msg> msgs = attribs == 0 ? status.getMsgs() : status.getMsgs(attribs);
		if(msgs != null) {
			for(final Msg msg : msgs) {
				vp.insert(new StatusMsgDisplay(msg), 0);
			}
		}
	}
}
