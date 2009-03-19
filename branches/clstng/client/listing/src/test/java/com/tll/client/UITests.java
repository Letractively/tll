package com.tll.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.ui.listing.RemoteListingWidget;
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

		private final Column[] cols =
				new Column[] {
					new Column("First Name", "firstName"), new Column("Last Name", "lastName"), new Column("MI", "mi"),
					new Column("City", "city"), new Column("Zip", "postalCode") };
		
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
		public IRowOptionsDelegate getRowOptionsHandler() {
			return null;
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
		public ITableCellRenderer<Model> getCellRenderer() {
			return IListingConfig.MODEL_CELL_RENDERER;
		}

		@Override
		public String getCaption() {
			return "Addresses";
		}

		@Override
		public IAddRowDelegate getAddRowHandler() {
			return null;
		}
	};

	/**
	 * RemoteListingWidgetTest
	 * @author jpk
	 */
	static final class RemoteListingWidgetTest extends DefaultUITestCase {

		RemoteListingWidget lw;

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
							defaultSorting);
			return lw;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}
