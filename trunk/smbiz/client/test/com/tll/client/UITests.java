package com.tll.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tll.client.admin.mvc.view.intf.InterfacesView;
import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.IBindable;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.BooleanPassThroughConverter;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.mvc.view.ViewRequestEvent;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.MsgPanel;
import com.tll.client.ui.SimpleHyperLink;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.listing.ListingNavBar;
import com.tll.client.ui.view.ViewContainer;
import com.tll.client.ui.view.ViewToolbar;
import com.tll.dao.Sorting;
import com.tll.service.app.RefDataType;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
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

	static String[] tests = new String[] {
		TEST_MSG_PANEL, TEST_TOOLBAR, TEST_VIEW_CONTAINER, TEST_FIELDS };

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
			stubTestLinks();
			initAuxDataCache();
		}
		catch(RuntimeException e) {
			GWT.log("Error in 'onModuleLoad()' method", e);
			e.printStackTrace();
		}
	}

	private void initAuxDataCache() {
		// stub aux data cache
		Map<String, String> cc = new HashMap<String, String>();
		cc.put("us", "United States");
		AuxDataCache.instance().cacheRefDataMap(RefDataType.ISO_COUNTRY_CODES, cc);

		Map<String, String> st = new HashMap<String, String>();
		st.put("ca", "California");
		AuxDataCache.instance().cacheRefDataMap(RefDataType.US_STATES, st);
	}

	/**
	 * Setup links to invoke the desired tests.
	 */
	private void stubTestLinks() {
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
				MsgManager.instance().clear();

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
				else if("Back".equals(historyToken)) {
					gotoTest = false;
				}
				toggleViewState(gotoTest);
			}

		});
	}

	/**
	 * TestFieldPanel - Used for the fields test.
	 * @author jpk
	 */
	private static final class TestFieldPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		private final FlowPanel canvas = new FlowPanel();

		private final FlowPanel addressPanel = new FlowPanel();
		private final CheckboxField<Boolean> bf;
		private final CheckboxField<Boolean> bflabel;

		/**
		 * Constructor
		 */
		public TestFieldPanel() {
			super();
			bf = FieldFactory.fcheckbox("bf", "bf", null, null, BooleanPassThroughConverter.INSTANCE);
			bflabel =
					FieldFactory.fcheckbox("bflabel", "bflabel", "Boolean with Label", "Help Text",
							BooleanPassThroughConverter.INSTANCE);
			initWidget(canvas);
			setRenderer(new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel panel, FieldGroup fg) {
					FieldGroup afg = (FieldGroup) fg.getFieldByName("address");
					afg.addFields(fg.getFields("address"));
					(new AddressFieldsRenderer()).render(addressPanel, null);

					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					cmpsr.addWidget(addressPanel);
					cmpsr.addField(bflabel);
					cmpsr.addField(bf);
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new IFieldGroupProvider() {

				public FieldGroup getFieldGroup() {
					FieldGroup fg = new FieldGroup();
					fg.setName("address");
					FieldGroup afg = (new AddressFieldsProvider()).getFieldGroup();
					fg.addField("address", afg);

					// set address2 as read only
					afg.getField("address.address2").setReadOnly(true);

					// set city as read only
					afg.getField("address.city").setReadOnly(true);

					fg.addField(bflabel);
					fg.addField(bf);

					return fg;
				}
			}).getFieldGroup();
		}
	}

	/**
	 * <p>
	 * Test: TEST_FIELDS
	 * <p>
	 * Purpose: Renders a populated {@link InterfacesView} to verify its
	 * DOM/Style.
	 */
	void testFields() {
		// use an address panel inside an edit panel as the test bed
		final TestFieldPanel<Model> fieldPanel = new TestFieldPanel<Model>();
		final EditPanel<Model> ep = new EditPanel<Model>(fieldPanel, true, true);
		testPanel.add(ep);

		// add button toggle read only/editable
		Button btnRO = new Button("Read Only", new ClickListener() {

			public void onClick(Widget sender) {
				Button b = (Button) sender;
				boolean readOnly = b.getText().equals("Read Only");
				fieldPanel.getFieldGroup().setReadOnly(readOnly);
				b.setText(readOnly ? "Editable" : "Read Only");
			}

		});
		testPanel.add(btnRO);

		// add button toggle enable/disable
		Button btnEnbl = new Button("Disable", new ClickListener() {

			public void onClick(Widget sender) {
				Button b = (Button) sender;
				boolean enable = b.getText().equals("Enable");
				fieldPanel.getFieldGroup().setEnabled(enable);
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
	 * Test: TEST_TOOLBAR
	 * <p>
	 * Purpose: Renders populated {@link Toolbar} impls to verify its DOM/Style.
	 */
	void testToolbar() {
		ViewToolbar vt =
				new ViewToolbar("AbstractView Display Name", new ViewOptions(true, true, true, true, true),
						new ClickListener() {

							public void onClick(Widget sender) {
							}

						});
		testPanel.add(vt);

		ListingNavBar<Model> listingToolbar = new ListingNavBar<Model>(new IListingConfig<Model>() {

			public boolean isSortable() {
				return true;
			}

			public boolean isIgnoreCaseWhenSorting() {
				return false;
			}

			public boolean isShowRefreshBtn() {
				return true;
			}

			public boolean isShowNavBar() {
				return true;
			}

			public ITableCellRenderer<Model> getCellRenderer() {
				return null;
			}

			public int getPageSize() {
				return 0;
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

			public IAddRowDelegate getAddRowHandler() {
				return null;
			}

			public IRowOptionsDelegate getRowOptionsHandler() {
				return null;
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
	}

	/**
	 * <p>
	 * Test: TEST_VIEW_CONTAINER
	 * <p>
	 * Purpose: Renders a populated {@link ViewContainer} to verify its DOM/Style.
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
	 * Purpose: Renders a populated {@link MsgPanel} to verify its DOM/Style.
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

		MsgManager.instance().post(false, msgs, Position.CENTER, RootPanel.get(), -1, true).show();
	}
}
