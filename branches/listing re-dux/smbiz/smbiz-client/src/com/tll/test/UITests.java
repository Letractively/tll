package com.tll.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.mvc.view.intf.InterfacesView;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.admin.ui.field.intf.MultiOptionInterfacePanel;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.model.BooleanPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedOneProperty;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.search.impl.AddressSearch;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.SimpleHyperLink;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.listing.ListingNavBar;
import com.tll.client.ui.view.ViewContainer;
import com.tll.client.ui.view.ViewToolbar;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/CSS of
 * compiled GWT code.
 */
public final class UITests implements EntryPoint, HistoryListener {

	/**
	 * The test names.
	 */
	static final String TEST_MSG_PANEL = "TEST_MSG_PANEL";
	static final String TEST_TOOLBAR = "TEST_TOOLBAR";
	static final String TEST_VIEW_CONTAINER = "TEST_VIEW_CONTAINER";
	static final String TEST_FIELDS = "TEST_FIELDS";
	static final String TEST_INTERFACE_PANEL = "TEST_INTERFACE_PANEL";

	static String[] tests = new String[] {
		TEST_MSG_PANEL,
		TEST_TOOLBAR,
		TEST_VIEW_CONTAINER,
		TEST_FIELDS,
		TEST_INTERFACE_PANEL };

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

		App.init();

		// try/catch is necessary here because GWT.setUncaughtExceptionHandler()
		// will work not until onModuleLoad() has returned
		try {
			History.addHistoryListener(this);
			stub();
		}
		catch(RuntimeException e) {
			GWT.log("Error in 'onModuleLoad()' method", e);
			e.printStackTrace();
		}
	}

	/**
	 * Setup links to invoke the desired tests.
	 */
	void stub() {
		for(String element : tests) {
			Hyperlink link = new Hyperlink(element, element);
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

	public void onHistoryChanged(final String historyToken) {
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				testPanel.clear();

				boolean gotoTest = true;
				if(TEST_MSG_PANEL.equals(historyToken)) {
					testMsgPanel();
				}
				else if(TEST_TOOLBAR.equals(historyToken)) {
					testToolbar();
				}
				else if(TEST_VIEW_CONTAINER.equals(historyToken)) {
					testViewContainer();
				}
				else if(TEST_FIELDS.equals(historyToken)) {
					testFields();
				}
				else if(TEST_INTERFACE_PANEL.equals(historyToken)) {
					testInterfacePanel();
				}
				else if("Back".equals(historyToken)) {
					gotoTest = false;
				}
				toggleViewState(gotoTest);
			}

		});
	}

	private static Model testModel;
	private static Model intf;

	/**
	 * TestFieldPanel - Used for the fields test.
	 * @author jpk
	 */
	private static final class TestFieldPanel extends FieldGroupPanel {

		private final AddressPanel ap;

		private final CheckboxField bf;
		private final CheckboxField bflabel;

		/**
		 * Constructor
		 */
		public TestFieldPanel() {
			super("Test Field Panel");
			ap = new AddressPanel();
			bf = fbool("bf", null);
			bflabel = fbool("bflabel", "Boolean with Label");
		}

		@Override
		public void populateFieldGroup() {
			addField(ap.getFields());
			addField(bflabel);
			addField(bf);
		}

		@Override
		protected Widget draw() {
			FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();
			canvas.addWidget(ap);
			canvas.addField(bflabel);
			canvas.addField(bf);
			return canvas.getCanvasWidget();
		}
	}

	private void setTestModel(Model address) {
		testModel = new Model();
		testModel.set(new RelatedOneProperty(EntityType.ADDRESS, "address", true, address));
		testModel.set(new BooleanPropertyValue("bflabel", true));
		testModel.set(new BooleanPropertyValue("bf", false));
	}

	/**
	 * <p>
	 * Test: TEST_FIELDS
	 * <p>
	 * Purpose: Renders a populated {@link InterfacesView} to verify its DOM/CSS.
	 */
	void testFields() {
		// use an address panel inside an edit panel as the test bed
		final TestFieldPanel fieldPanel = new TestFieldPanel();
		final EditPanel ep = new EditPanel(fieldPanel, true, true);
		testPanel.add(ep);

		if(testModel == null) {
			CrudCommand cc = new CrudCommand(testPanel);
			cc.addCrudListener(new ICrudListener() {

				public void onCrudEvent(CrudEvent event) {
					setTestModel(event.getPayload().getEntity());
					ep.setModel(testModel);
					ep.refresh();
				}
			});
			AddressSearch search = new AddressSearch("AddressKey");
			search.setAddress1("home address line 11");
			search.setPostalCode("94155");
			cc.loadByBusinessKey(search);
			cc.execute();
		}
		else {
			ep.setModel(testModel);
			ep.refresh();
		}

		// add button toggle read only/editable
		Button btnRO = new Button("Read Only", new ClickListener() {

			public void onClick(Widget sender) {
				Button b = (Button) sender;
				boolean readOnly = b.getText().equals("Read Only");
				fieldPanel.getFields().setReadOnly(readOnly);
				b.setText(readOnly ? "Editable" : "Read Only");
			}

		});
		testPanel.add(btnRO);

		// add button toggle enable/disable
		Button btnEnbl = new Button("Disable", new ClickListener() {

			public void onClick(Widget sender) {
				Button b = (Button) sender;
				boolean enable = b.getText().equals("Enable");
				fieldPanel.getFields().setEnabled(enable);
				b.setText(enable ? "Disable" : "Enable");
			}

		});
		testPanel.add(btnEnbl);

		// add button toggle show/hide
		Button btnVisible = new Button("Hide", new ClickListener() {

			public void onClick(Widget sender) {
				Button b = (Button) sender;
				boolean show = b.getText().equals("Show");
				fieldPanel.setVisible(show);
				b.setText(show ? "Hide" : "Show");
			}

		});
		testPanel.add(btnVisible);

	}

	/**
	 * <p>
	 * Test: TEST_INTERFACE_PANEL
	 * <p>
	 * Purpose: Renders an interface panel to verify its DOM/CSS.
	 */
	void testInterfacePanel() {
		final MultiOptionInterfacePanel intfPanel = new MultiOptionInterfacePanel();
		testPanel.add(intfPanel);
		if(intf == null) {
			// stub mock interface
			CrudCommand cc = new CrudCommand(testPanel);
			cc.addCrudListener(new ICrudListener() {

				public void onCrudEvent(CrudEvent event) {
					intf = event.getPayload().getEntity();
					intfPanel.populateFieldGroup();
				}
			});
			cc.loadByName(EntityType.INTERFACE_SINGLE, "Payment Processor");
			cc.execute();
		}
		else {
			intfPanel.populateFieldGroup();
		}
	}

	/**
	 * <p>
	 * Test: TEST_TOOLBAR
	 * <p>
	 * Purpose: Renders populated {@link Toolbar} impls to verify its DOM/CSS.
	 */
	void testToolbar() {
		ViewToolbar vt =
				new ViewToolbar("AbstractView Display Name", new ViewOptions(true, true, true, true, true),
						new ClickListener() {

							public void onClick(Widget sender) {
							}

						});
		testPanel.add(vt);

		ListingNavBar listingToolbar = new ListingNavBar(new IListingConfig<Model>() {

			public boolean isSortable() {
				return true;
			}

			public boolean isShowRefreshBtn() {
				return true;
			}

			public boolean isShowNavBar() {
				return true;
			}

			public boolean isShowAddBtn() {
				return true;
			}

			public boolean isPageable() {
				return true;
			}

			public ITableCellRenderer<Model> getCellRenderer() {
				return null;
			}

			public int getPageSize() {
				return 0;
			}

			public String getListingName() {
				return "Test Listing";
			}

			public String getListingElementName() {
				return "Test Element";
			}

			public Sorting getDefaultSorting() {
				return null;
			}

			public Column[] getColumns() {
				return null;
			}

			public String getCaption() {
				return "Test elements";
			}

		});
		testPanel.add(listingToolbar);
	}

	private static final class TestViewClass extends ViewClass {

		public TestViewClass() {
			super("testViewClass");
		}

		@Override
		public IView newView() {
			return new TestView();
		}

	}

	private static final class TestView extends AbstractView {

		private static final TestViewClass klas = new TestViewClass();

		public TestView() {
			super();
			addWidget(new Label("label 1"));
			addWidget(new Label("label 2"));
			addWidget(new Label("label 3"));
		}

		@Override
		protected ViewClass getViewClass() {
			return klas;
		}

		public String getLongViewName() {
			return "A Test AbstractView";
		}

		@Override
		protected void doInitialization(ViewRequestEvent viewRequest) {
		}

		public void refresh() {
		}

		@Override
		protected void doDestroy() {
		}

		@Override
		public ShowViewRequest newViewRequest() {
			return null;
		}

		public void onModelChangeEvent(ModelChangeEvent event) {
		}
	}

	/**
	 * <p>
	 * Test: TEST_VIEW_CONTAINER
	 * <p>
	 * Purpose: Renders a populated {@link ViewContainer} to verify its DOM/CSS.
	 */
	void testViewContainer() {

		TestViewClass viewClass = new TestViewClass();
		final ViewContainer vc = new ViewContainer(viewClass.newView());
		testPanel.add(new SimpleHyperLink("Pop", new ClickListener() {

			public void onClick(Widget sender) {
				SimpleHyperLink sh = (SimpleHyperLink) sender;
				if(sh.getText().equals("Pop")) {
					vc.pop(testPanel);
					sh.setText("Pin");
				}
				else {
					vc.pin(testPanel);
					sh.setText("Pop");
				}
			}
		}));
		testPanel.add(vc);
	}

	/**
	 * <p>
	 * Test: TEST_MSG_PANEL
	 * <p>
	 * Purpose: Renders a populated {@link MsgPanel} to verify its DOM/CSS.
	 */
	void testMsgPanel() {

		// create msgs
		List<Msg> msgs = new ArrayList<Msg>();
		// msgs.add(new Msg("This is a fatal message.", MsgLevel.FATAL));
		msgs.add(new Msg("This is an error message.", MsgLevel.ERROR));
		msgs.add(new Msg("This is another error message.", MsgLevel.ERROR));
		msgs.add(new Msg("This is a warn message.", MsgLevel.WARN));
		msgs.add(new Msg("This is an info message.", MsgLevel.INFO));
		msgs.add(new Msg("This is another info message.", MsgLevel.INFO));
		msgs.add(new Msg("This is yet another info message.", MsgLevel.INFO));
		// msgs.add(new Msg("This is a success message.", MsgLevel.SUCCESS));

		MsgManager.instance.post(false, msgs, Position.CENTER, RootPanel.get(), -1, true).show();
	}
}
