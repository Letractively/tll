package com.tll.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
		 * Loads the UI test artifacts in the ui.
		 */
		public abstract void load();

		/**
		 * Unloads the UI test artifacts from the ui.
		 */
		public abstract void unload();
	}
	
	/**
	 * Stubs 2 panels horizontally where the first panel contains test buttons and
	 * the second panel contains the test subject.
	 */
	protected static final class DefaultTestLayout extends Composite {

		final HorizontalPanel layout = new HorizontalPanel();

		final VerticalPanel buttonPanel = new VerticalPanel();

		Widget context;

		/**
		 * Constructor
		 */
		public DefaultTestLayout() {
			super();
			layout.getElement().getStyle().setProperty("margin", "1em");
			layout.setSpacing(5);
			layout.setBorderWidth(1);
			layout.add(buttonPanel);
			initWidget(layout);
		}

		/**
		 * @param context the context to set replacing the existing one if it
		 *        exists.
		 * @return The old context if there was one
		 */
		public Widget setContext(Widget context) {
			final Widget old = this.context;
			if(this.context != null) {
				// remove old context
				this.context.removeFromParent();
			}
			layout.add(context);
			this.context = context;
			return old;
		}

		public void addTestAction(Button action) {
			buttonPanel.add(action);
		}
	} // DefaultTestLayout

	/**
	 * DefaultUITestCase - Employs {@link DefaultTestLayout} as the test layout
	 * making it easy to add test actions.
	 * @author jpk
	 */
	static abstract class DefaultUITestCase extends UITestCase {
		
		final String name, desc;
		DefaultTestLayout layout;

		/**
		 * Constructor
		 * @param name the test name
		 * @param desc the test description
		 */
		public DefaultUITestCase(String name, String desc) {
			super();
			this.name = name;
			this.desc = desc;
		}

		@Override
		public final String getName() {
			return name;
		}

		@Override
		public final String getDescription() {
			return desc;
		}
		
		/**
		 * Provides the testing subject (context).
		 * @return test context
		 */
		protected abstract Widget getContext();

		/**
		 * @return Array of {@link Button}s that when clicked, performs a test
		 *         action on the context.
		 */
		protected abstract Button[] getTestActions();
		
		/**
		 * Override to do specific test context initialization operations.
		 */
		protected void init() {
			// base impl no-op
		}

		/**
		 * Override to do specific test context teardown operations.
		 */
		protected void teardown() {
			// base impl no-op
		}

		@Override
		public final void load() {
			layout = new DefaultTestLayout();
			layout.setContext(getContext());
			RootPanel.get().add(layout);
			final Button[] actions = getTestActions();
			if(actions != null) {
				for(final Button action : actions) {
					layout.addTestAction(action);
				}
			}
			init();
		}

		@Override
		public final void unload() {
			teardown();
			layout.removeFromParent();
			layout = null;
		}
		
	} // DefaultUITestCase

	/**
	 * The root history token the histroy sub-system is initialized with upon
	 * module load.
	 */
	static final String ROOT_HISTORY_TOKEN = "";

	private final Grid testList = new Grid(1, 3);
	private final Hyperlink backLink = new Hyperlink("Back", "Back");
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
		testList.getElement().getStyle().setProperty("margin", "1em");
		testList.setCellSpacing(5);
		testList.setBorderWidth(1);
		testList.getRowFormatter().setStyleName(0, "bold");
		testList.getColumnFormatter().setStyleName(0, "italic");
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
		
		backLink.getElement().getStyle().setProperty("padding", "1em");
		backLink.setVisible(false);
		RootPanel.get().add(backLink);
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
				if(ROOT_HISTORY_TOKEN.equals(historyToken) || backLink.getTargetHistoryToken().equals(historyToken)) {
					if(current != null) {
						try {
							current.unload();
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
						test.load();
						return;
					}
				}
				
				throw new IllegalStateException("Unhandled history state: " + historyToken);
			}

		});
	}
}
