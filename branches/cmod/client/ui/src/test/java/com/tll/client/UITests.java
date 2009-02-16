package com.tll.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	/**
	 * MsgPanelTest - Tests {@link MsgPanel} layout.
	 * @author jpk
	 */
	static final class MsgPanelTest extends UITestCase {

		@Override
		public void cleanup(Panel testPanel) {
			MsgManager.instance().clear();
		}

		@Override
		public void doTest(Panel testPanel) {
			final List<Msg> msgs = new ArrayList<Msg>();
			msgs.add(new Msg("This is an error message.", MsgLevel.ERROR));
			msgs.add(new Msg("This is another error message.", MsgLevel.ERROR));
			msgs.add(new Msg("This is a warn message.", MsgLevel.WARN));
			msgs.add(new Msg("This is an info message.", MsgLevel.INFO));
			msgs.add(new Msg("This is another info message.", MsgLevel.INFO));
			msgs.add(new Msg("This is yet another info message.", MsgLevel.INFO));
			MsgManager.instance().post(false, msgs, Position.CENTER, testPanel, -1, true).show();
		}

		@Override
		public String getDescription() {
			return "Tests the layout/CSS of MsgPanels";
		}

		@Override
		public String getName() {
			return "MsgPanel test";
		}
	}

	@Override
	protected String getTestSubjectName() {
		return "client-ui module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new MsgPanelTest() };
	}
}
