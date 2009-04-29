package com.tll.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.ModelPropertyFormatter;
import com.tll.client.listing.PropertyBoundColumn;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.client.ui.option.Option;
import com.tll.common.model.Model;
import com.tll.common.search.mock.TestAddressSearch;
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

	/**
	 * The test listing config.
	 */
	static final IListingConfig<Model> config = new IListingConfig<Model>() {

		PropertyBoundColumn cName = new PropertyBoundColumn("Name", "lastName");
		PropertyBoundColumn cAddress = new PropertyBoundColumn("Address", "address1");
		PropertyBoundColumn cCity = new PropertyBoundColumn("City", "city");

		final Option[] options = new Option[] {
			new Option("Option 1"), new Option("Option 2"), new Option("Option 3") };

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
			return 5;
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
			return new IRowOptionsDelegate() {

				@Override
				public void handleOptionSelection(String optionText, int rowIndex) {
					Window.alert("Row " + rowIndex + " was selected: " + optionText);
				}

				@Override
				public Option[] getOptions(int rowIndex) {
					return options;
				}
			};
		}

		@Override
		public IAddRowDelegate getAddRowHandler() {
			return new IAddRowDelegate() {

				@Override
				public void handleAddRow() {
					Window.alert("Add new Row");
				}
			};
		}
	};

	/**
	 * RemoteListingWidgetTest
	 * @author jpk
	 */
	static final class RemoteListingWidgetTest extends DefaultUITestCase {

		ModelListingWidget lw;

		/**
		 * Constructor
		 */
		public RemoteListingWidgetTest() {
			super("Listing Test", "Tests the core listing widget functionality");
		}

		@Override
		protected Widget getContext() {
			lw =
				ListingFactory.createRemoteListingWidget(config, "addresses", ListHandlerType.PAGE, new TestAddressSearch(),
						null, defaultSorting);
			return lw;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}
