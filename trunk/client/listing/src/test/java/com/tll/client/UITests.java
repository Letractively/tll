package com.tll.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.ModelPropertyFormatter;
import com.tll.client.listing.PropertyBoundColumn;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.common.model.Model;
import com.tll.common.model.test.MockModelStubber;
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

	static final Sorting defaultSorting = new Sorting("lastName");

	static final class TestRowOptions extends AbstractRowOptions {

		ListingWidget<Model, ?> listing;
		Model address;

		/**
		 * Constructor
		 */
		public TestRowOptions() {
			super();
			address = MockModelStubber.stubAddress(1);
		}

		public void setListing(ListingWidget<Model, ?> listing) {
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

		ListingWidget<Model, ?> listing;

		public void setListing(ListingWidget<Model, ?> listing) {
			this.listing = listing;
		}

		@Override
		public void handleAddRow() {
			listing.addRow(MockModelStubber.stubAddress(1));
		}

	}

	/**
	 * The test listing config.
	 */
	static final class TestConfig implements IListingConfig<Model> {

		PropertyBoundColumn cName = new PropertyBoundColumn("Name", "lastName");
		PropertyBoundColumn cAddress = new PropertyBoundColumn("Address", "address1");
		PropertyBoundColumn cCity = new PropertyBoundColumn("City", "city");
		TestRowOptions rowOptions = new TestRowOptions();
		TestAddRowDelegate addRowDelegate = new TestAddRowDelegate();

		private final Column[] cols = new Column[] {
			Column.ROW_COUNT_COLUMN, cName, cAddress, cCity };

		private final ITableCellRenderer<Model, Column> cellRenderer = new ITableCellRenderer<Model, Column>() {

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
		public ITableCellRenderer<Model, ? extends Column> getCellRenderer() {
			return cellRenderer;
		}

		@Override
		public String getCaption() {
			return "Addresses";
		}

		@Override
		public IRowOptionsDelegate getRowOptionsHandler() {
			return rowOptions;
		}

		@Override
		public IAddRowDelegate getAddRowHandler() {
			return addRowDelegate;
		}
	} // TestConfig

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
			config = new TestConfig();
			lw =
				ListingFactory.createRemoteListingWidget(config, "addresses", ListHandlerType.PAGE, new TestAddressSearch(),
						null, defaultSorting);
			config.rowOptions.setListing(lw);
			config.addRowDelegate.setListing(lw);
			lw.setPortalHeight("300px");
			return lw;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}
