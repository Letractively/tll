package com.tll.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
@SuppressWarnings("synthetic-access")
public final class UITests implements EntryPoint, ValueChangeHandler<String> {

	/**
	 * The test names.
	 */
	static final String TEST_MSG_PANEL = "TEST_MSG_PANEL";
	static final String TEST_TOOLBAR = "TEST_TOOLBAR";

	static String[] tests = new String[] {
		TEST_MSG_PANEL, TEST_TOOLBAR };

	final HtmlListPanel testList = new HtmlListPanel(true);
	final Hyperlink backLink = new Hyperlink("Back", "Back");
	final FlowPanel testPanel = new FlowPanel();

	/**
	 * Constructor
	 */
	public UITests() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Log.setUncaughtExceptionHandler();
		History.newItem("");

		// try/catch is necessary here because GWT.setUncaughtExceptionHandler()
		// will work not until onModuleLoad() has returned
		try {
			History.addValueChangeHandler(this);
			stubTestLinks();
		}
		catch(final RuntimeException e) {
			GWT.log("Error in 'onModuleLoad()' method", e);
			e.printStackTrace();
		}
	}

	/**
	 * Setup links to invoke the desired tests.
	 */
	private void stubTestLinks() {
		for(final String element : tests) {
			final Hyperlink link = new Hyperlink(element, element);
			testList.add(link);
		}
		RootPanel.get().add(testList);
		backLink.setVisible(false);
		RootPanel.get().add(backLink);
		RootPanel.get().add(testPanel);
		testPanel.setWidth("700px");
	}

	private void toggleViewState(boolean gotoTest) {
		testList.setVisible(!gotoTest);
		backLink.setVisible(gotoTest);
	}

	public void onValueChange(final ValueChangeEvent<String> event) {
		final String historyToken = event.getValue();
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				testPanel.clear();
				MsgManager.instance().clear();

				boolean gotoTest = true;
				if(TEST_MSG_PANEL.equals(historyToken)) {
					testMsgPanel();
				}
				if(TEST_TOOLBAR.equals(historyToken)) {
					testToolbar();
				}
				else if("Back".equals(historyToken)) {
					gotoTest = false;
				}
				toggleViewState(gotoTest);
			}

		});
	}

	/**
	 * <p>
	 * Test: TEST_MSG_PANEL
	 * <p>
	 * Purpose: Renders a populated {@link MsgPanel} to verify its DOM/Style.
	 */
	void testMsgPanel() {

		// create msgs
		final List<Msg> msgs = new ArrayList<Msg>();
		// msgs.add(new Msg("This is a fatal message.", MsgLevel.FATAL));
		msgs.add(new Msg("This is an error message.", MsgLevel.ERROR));
		msgs.add(new Msg("This is another error message.", MsgLevel.ERROR));
		msgs.add(new Msg("This is a warn message.", MsgLevel.WARN));
		msgs.add(new Msg("This is an info message.", MsgLevel.INFO));
		msgs.add(new Msg("This is another info message.", MsgLevel.INFO));
		msgs.add(new Msg("This is yet another info message.", MsgLevel.INFO));
		// msgs.add(new Msg("This is a success message.", MsgLevel.SUCCESS));

		MsgManager.instance().post(false, msgs, Position.CENTER, RootPanel.get(), -1, true).show();
	}

	/**
	 * <p>
	 * Test: TEST_TOOLBAR
	 * <p>
	 * Purpose: Renders a {@link Toolbar} to verify its DOM/Style.
	 */
	void testToolbar() {
		Log.debug("testToolbar currently not implemented.");
	}
}
