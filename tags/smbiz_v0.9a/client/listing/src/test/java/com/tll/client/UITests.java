package com.tll.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ModelPropertyFormatter;
import com.tll.client.listing.RemoteListingOperator;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.client.ui.listing.RemoteListingWidget;
import com.tll.common.model.Model;
import com.tll.common.model.test.TestModelStubber;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-listing module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new RemoteListingWidgetTest() };
	}

	static final class TestRowOptions extends AbstractRowOptions {

		TestListingWidget listing;
		Model address;

		/**
		 * Constructor
		 */
		public TestRowOptions() {
			super();
			address = TestModelStubber.stubAddress(1);
		}

		public void setListing(TestListingWidget listing) {
			this.listing = listing;
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			listing.markRowDeleted(rowIndex, true);
		}

		@Override
		protected void doEditRow(int rowIndex) {
			listing.updateRow(rowIndex, address);
		}

		@Override
		protected String getListingElementName() {
			return "Addresses";
		}
	}

	static final class TestAddRowDelegate implements IAddRowDelegate {

		TestListingWidget listing;

		public void setListing(TestListingWidget listing) {
			this.listing = listing;
		}

		@Override
		public void handleAddRow() {
			listing.addRow(TestModelStubber.stubAddress(1));
		}

	}

	/**
	 * The test listing config.
	 */
	static final class TestConfig implements IListingConfig<Model> {

		static final Sorting defaultSorting = new Sorting("lastName");

		static final Column cName = new Column("Name", "lastName");
		static final Column cAddress = new Column("Address", "address1");
		static final Column cCity = new Column("City", "city");

		static final String[] mprops = new String[] { "firstName", "lastName", "address1", "address2" };
		static final Column[] cols = new Column[] { Column.ROW_COUNT_COLUMN, cName, cAddress, cCity };

		private final ITableCellRenderer<Model> cellRenderer = new ITableCellRenderer<Model>() {

			@Override
			public String getCellValue(Model rowData, Column column) {
				if(column == cName) {
					final StringBuilder sb = new StringBuilder();
					sb.append(ModelPropertyFormatter.pformat(rowData, "firstName", null));
					sb.append(" ");
					sb.append(ModelPropertyFormatter.pformat(rowData, "lastName", null));
					return sb.toString();
				}
				else if(column == cAddress) {
					final StringBuilder sb = new StringBuilder();
					sb.append(ModelPropertyFormatter.pformat(rowData, "address1", null));
					final String a2 = ModelPropertyFormatter.pformat(rowData, "address2", null);
					if(a2 != null) {
						sb.append(" ");
						sb.append(a2);
					}
					return sb.toString();
				}
				else if(column == cCity) {
					return ModelPropertyFormatter.pformat(rowData, "city", null);
				}
				throw new IllegalStateException("Un-resolvable column: " + column);
			}
		};

		@Override
		public String getListingId() {
			return "unique";
		}

		@Override
		public boolean isSortable() {
			return true;
		}

		@Override
		public boolean isShowRefreshBtn() {
			return true;
		}

		@Override
		public boolean isShowNavBar() {
			return true;
		}

		@Override
		public boolean isIgnoreCaseWhenSorting() {
			return true;
		}

		@Override
		public int getPageSize() {
			return 20;
		}

		@Override
		public String getListingElementName() {
			return "Address";
		}

		@Override
		public Sorting getDefaultSorting() {
			return defaultSorting;
		}

		@Override
		public Column[] getColumns() {
			return cols;
		}

		@Override
		public String[] getModelProperties() {
			return mprops;
		}

		@Override
		public ITableCellRenderer<Model> getCellRenderer() {
			return cellRenderer;
		}

		@Override
		public String getCaption() {
			return "Addresses";
		}
	} // TestConfig

	static class TestListingWidget extends RemoteListingWidget {

		static final TestConfig config = new TestConfig();
		static final TestAddressSearch criteria = new TestAddressSearch();

		static final TestRowOptions rowOptions = new TestRowOptions();
		static final TestAddRowDelegate addRowDelegate = new TestAddRowDelegate();

		public TestListingWidget() {
			super(config);

			setOperator(RemoteListingOperator.create(config.getListingId(), ListHandlerType.PAGE, criteria, config
					.getModelProperties(), config.getPageSize(), config.getDefaultSorting()));

			rowOptions.setListing(this);
			addRowDelegate.setListing(this);
		}

		@Override
		protected IAddRowDelegate getAddRowHandler() {
			return addRowDelegate;
		}

		@Override
		protected IRowOptionsDelegate getRowOptionsHandler() {
			return rowOptions;
		}

	} // TestListingWidget

	/**
	 * RemoteListingWidgetTest
	 * @author jpk
	 */
	static final class RemoteListingWidgetTest extends DefaultUITestCase {

		TestConfig config;
		ModelListingWidget lw;

		/**
		 * Constructor
		 */
		public RemoteListingWidgetTest() {
			super("Listing Test", "Tests the core listing widget functionality");
		}

		@Override
		protected Widget getContext() {
			lw = new TestListingWidget();
			lw.setPortalHeight("300px");
			return lw;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}