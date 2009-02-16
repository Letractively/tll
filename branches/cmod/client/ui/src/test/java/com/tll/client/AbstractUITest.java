package com.tll.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
@SuppressWarnings("synthetic-access")
public abstract class AbstractUITest implements EntryPoint, ValueChangeHandler<String> {

	/**
	 * UITestCase - Defines a simple life-cycle for invoking a particular ui test.
	 * @author jpk
	 */
	public static abstract class UITestCase {
		
		/**
		 * @return A descriptive name for the test.
		 */
		public abstract String getName();

		/**
		 * @return An optional description for the test.
		 */
		public abstract String getDescription();

		/**
		 * @return The employed history token for this test. This is used to
		 *         navigate from test to test in the ui.
		 */
		public final String getHistoryToken() {
			return getClass().getName();
		}

		/**
		 * Runs the test.
		 * @param testPanel The {@link Panel} onto which any UI artifacts are to be
		 *        attached.
		 */
		public abstract void doTest(Panel testPanel);

		/**
		 * Called when leaving this particular test context in the browser history
		 * stack.
		 * @param testPanel The {@link Panel} from which any UI added artifacts as a
		 *        result of invoking {@link #doTest(Panel)} are to be removed.
		 */
		public abstract void cleanup(Panel testPanel);
	}

	/**
	 * The root history token the histroy sub-system is initialized with upon
	 * module load.
	 */
	static final String ROOT_HISTORY_TOKEN = "";

	private final Grid testList = new Grid(1, 3);
	private final Hyperlink backLink = new Hyperlink("Back", "Back");
	private final FlowPanel testPanel = new FlowPanel();
	private final UITestCase[] tests;
	private UITestCase current;

	/**
	 * Constructor
	 */
	public AbstractUITest() {
		super();
		this.tests = getTestCases();
		if(tests == null || tests.length < 1) {
			throw new IllegalStateException("At least one test case must be declared.");
		}
	}

	/**
	 * This is the entry point method.
	 */
	public final void onModuleLoad() {

		Log.setUncaughtExceptionHandler();
		History.newItem(ROOT_HISTORY_TOKEN);

		// try/catch is necessary here because GWT.setUncaughtExceptionHandler()
		// will work not until onModuleLoad() has returned
		try {
			History.addValueChangeHandler(this);
			stubPage();
		}
		catch(final RuntimeException e) {
			GWT.log("Error in 'onModuleLoad()' method", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The descriptive name for this UI test.
	 */
	protected abstract String getTestSubjectName();

	/**
	 * @return The {@link UITestCase}s to test.
	 */
	protected abstract UITestCase[] getTestCases();

	/**
	 * Stubs the main test page with test links.
	 */
	private void stubPage() {
		final Label l = new Label("UI Tests for " + getTestSubjectName());
		l.getElement().getStyle().setProperty("fontSize", "130%");
		l.getElement().getStyle().setProperty("fontWeight", "bold");
		l.getElement().getStyle().setProperty("padding", "1em");
		RootPanel.get().add(l);

		// stub the test links
		testList.getElement().getStyle().setProperty("padding", "1em");
		testList.setCellSpacing(5);
		testList.setBorderWidth(1);
		testList.setWidget(0, 0, new Label("#"));
		testList.setWidget(0, 1, new Label("Test"));
		testList.setWidget(0, 2, new Label("Description"));
		int testIndex = 1;
		testList.resizeRows(tests.length + 1);
		for(final UITestCase test : tests) {
			final Hyperlink tlink = new Hyperlink(test.getName(), test.getHistoryToken());
			tlink.setTitle(test.getDescription());
			testList.setWidget(testIndex, 0, new Label(Integer.toString(testIndex)));
			testList.setWidget(testIndex, 1, tlink);
			testList.setWidget(testIndex, 2, new Label(test.getDescription()));
			testIndex++;
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

	public final void onValueChange(final ValueChangeEvent<String> event) {
		final String historyToken = event.getValue();
		assert historyToken != null;
		
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				testPanel.clear();

				if(ROOT_HISTORY_TOKEN.equals(historyToken) || backLink.getTargetHistoryToken().equals(historyToken)) {
					if(current != null) {
						try {
							current.cleanup(testPanel);
						}
						finally {
							current = null;
						}
					}
					toggleViewState(false);
					return;
				}
				
				for(final UITestCase test : tests) {
					if(historyToken.equals(test.getHistoryToken())) {
						assert current == null;
						current = test;
						toggleViewState(true);
						test.doTest(testPanel);
						return;
					}
				}
				
				throw new IllegalStateException("Unhandled history state: " + historyToken);
			}

		});
	}
}
