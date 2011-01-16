package com.tll.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.RemoteDataProvider;
import com.tll.client.ui.listing.TestListingWidget;
import com.tll.common.dto.test.AddressDto;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.dao.Sorting;

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
		return new UITestCase[] { new RemoteListingWidgetTest()
		};
	}

//	static final class TestRowOptions extends AbstractRowOptions {
//
//		TestListingWidget listing;
//		AddressDto address;
//
//		/**
//		 * Constructor
//		 */
//		public TestRowOptions() {
//			super();
//			address = new AddressDto();
//		}
//
//		public void setListing(TestListingWidget listing) {
//			this.listing = listing;
//		}
//
//		@Override
//		protected void doDeleteRow(int rowIndex) {
//			listing.markRowDeleted(rowIndex, true);
//		}
//
//		@Override
//		protected void doEditRow(int rowIndex) {
//			listing.updateRow(rowIndex, address);
//		}
//
//		@Override
//		protected String getListingElementName() {
//			return "Addresses";
//		}
//	}
//
//	static final class TestAddRowDelegate implements IAddRowDelegate {
//
//		TestListingWidget listing;
//
//		public void setListing(TestListingWidget listing) {
//			this.listing = listing;
//		}
//
//		@Override
//		public void handleAddRow() {
//			listing.addRow(new AddressDto());
//		}
//
//	}
//
//	/**
//	 * The test listing config.
//	 */
//	static final class TestConfig implements IListingConfig<AddressDto> {
//
//		static final Sorting defaultSorting = new Sorting("lastName");
//
//		static final Column cName = new Column("Name", "lastName");
//		static final Column cAddress = new Column("AddressDto", "address1");
//		static final Column cCity = new Column("City", "city");
//
//		static final String[] mprops = new String[] {
//			"firstName", "lastName", "address1", "address2"
//		};
//		static final Column[] cols = new Column[] {
//			Column.ROW_COUNT_COLUMN, cName, cAddress, cCity
//		};
//
//		static final ITableCellRenderer<AddressDto> cellRenderer = new ITableCellRenderer<AddressDto>() {
//
//			@Override
//			public void renderCell(int rowIndex, int cellIndex, AddressDto rowData, Column column, HTMLTable table) {
//				String cval;
//				if(column == cName) {
//					final StringBuilder sb = new StringBuilder();
//					sb.append(rowData.getFirstName());
//					sb.append(" ");
//					sb.append(rowData.getLastName());
//					cval = sb.toString();
//				}
//				else if(column == cAddress) {
//					final StringBuilder sb = new StringBuilder();
//					sb.append(rowData.getAddress1());
//					final String a2 = rowData.getAddress2();
//					if(a2 != null) {
//						sb.append(" ");
//						sb.append(a2);
//					}
//					cval = sb.toString();
//				}
//				else if(column == cCity) {
//					cval = rowData.getCity();
//				}
//				else {
//					throw new IllegalStateException("Un-resolvable column: " + column);
//				}
//				table.setText(rowIndex, cellIndex, cval);
//			}
//		};
//
//		@Override
//		public String getListingId() {
//			return "unique";
//		}
//
//		@Override
//		public boolean isSortable() {
//			return true;
//		}
//
//		@Override
//		public boolean isShowRefreshBtn() {
//			return true;
//		}
//
//		@Override
//		public boolean isShowNavBar() {
//			return true;
//		}
//
//		@Override
//		public boolean isIgnoreCaseWhenSorting() {
//			return true;
//		}
//
//		@Override
//		public int getPageSize() {
//			return 20;
//		}
//
//		@Override
//		public String getListingElementName() {
//			return "AddressDto";
//		}
//
//		@Override
//		public Sorting getDefaultSorting() {
//			return defaultSorting;
//		}
//
//		@Override
//		public Column[] getColumns() {
//			return cols;
//		}
//
//		@Override
//		public String[] getModelProperties() {
//			return mprops;
//		}
//	} // TestConfig

//	static class TestListingWidget extends RemoteListingWidget<AddressDto, ListingTable<AddressDto>> {
//
//		static final TestConfig config = new TestConfig();
//		static final TestAddressSearch criteria = new TestAddressSearch();
//
//		static final TestRowOptions rowOptions = new TestRowOptions();
//		static final TestAddRowDelegate addRowDelegate = new TestAddRowDelegate();
//
//		static {
//
//		}
//
//		public TestListingWidget() {
//			super(config.getListingId(), config.getListingElementName(), new ListingTable<AddressDto>(config, TestConfig.cellRenderer),
//					new ListingNavBar<AddressDto>(config, addRowDelegate));
//
//			RemoteListingOperator<AddressDto, TestAddressSearch> operator =
//					RemoteListingOperator.create(config.getListingId(), criteria, config
//							.getModelProperties(), config.getPageSize(), config.getDefaultSorting());
//
//			setOperator(operator);
//
//			rowOptions.setListing(this);
//			addRowDelegate.setListing(this);
//		}
//
//	} // TestListingWidget

	/**
	 * RemoteListingWidgetTest
	 * @author jpk
	 */
	static final class RemoteListingWidgetTest extends DefaultUITestCase {

		private final RemoteDataProvider<AddressDto, TestAddressSearch> dataProvider;
		
		private TestListingWidget lw;

		/**
		 * Constructor
		 */
		public RemoteListingWidgetTest() {
			super("Listing Test", "Tests the core listing widget functionality");
			
			TestAddressSearch search = new TestAddressSearch();
			Sorting initialSorting = new Sorting("lastName");
			dataProvider = RemoteDataProvider.create("1", search, null, 25, initialSorting);
		}

		@Override
		protected Widget getContext() {
			lw = new TestListingWidget();
			dataProvider.addDataDisplay(lw);
			return lw;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}
