/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.ui.listing;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.listing.AbstractAccountListingConfig;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.RemoteListingOperator;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.mvc.view.account.MerchantListingViewInitializer;
import com.tll.client.ui.option.Option;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.NamedQuerySearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * IspListingWidget
 * @author jpk
 */
public class IspListingWidget extends ModelListingWidget<ModelListingTable> {

	static final Option mListing = new Option("Merchant Listing", AbstractImagePrototype.create(App.imgs().arrow_sm_down()).createImage());

	/**
	 * IspListingConfig
	 * @author jpk
	 */
	static class IspListingConfig extends AbstractAccountListingConfig {

		private static final String listingElementName = SmbizEntityType.ISP.getName();

		private static final Sorting defaultSorting = new Sorting("name");

		private static final Column[] cols = new Column[] {
			Column.ROW_COUNT_COLUMN,
			new Column("Name", Model.NAME_PROPERTY, "i"),
			new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "i"),
			new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "i"),
			new Column("Status", "status", "i"),
			new Column("Billing Model", "billingModel", "i"),
			new Column("Billing Cycle", "billingCycle", "i"),
		};

		private static final String[] mprops = new String[] {
			Model.NAME_PROPERTY,
			Model.DATE_CREATED_PROPERTY,
			Model.DATE_MODIFIED_PROPERTY,
			"status",
			"billingModel",
			"billingCycle",
		};

		/**
		 * Constructor
		 */
		public IspListingConfig() {
			super(listingElementName, mprops, cols, defaultSorting);
		}
	}

	class RowHandler extends AbstractRowOptions {

		@Override
		protected Option[] getCustomRowOps(int rowIndex) {
			return new Option[] { mListing, App.OPTION_SET_CURRENT, };
		}

		@Override
		protected void handleRowOp(String optionText, int rowIndex) {
			if(mListing.getText().equals(optionText)) {
				ViewManager.get().dispatch(
						new ShowViewRequest(new MerchantListingViewInitializer(getRowKey(rowIndex))));
			}
			else if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
				SmbizAdmin.getAdminContextCmd().changeCurrentAccount(getRowKey(rowIndex));
			}
		}

		@Override
		protected String getListingElementName() {
			return config.getListingElementName();
		}

		@Override
		protected void doEditRow(int rowIndex) {
		 Logger.debug("doEditRow - rowIndex: " + rowIndex);
			ViewManager.get().dispatch(
					new ShowViewRequest(new EditViewInitializer(AccountEditView.klas, getRowKey(rowIndex))));
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			CrudCommand.deleteModel(IspListingWidget.this, getRowKey(rowIndex)).execute();
		}
	} // Row Handler

	static final IListingConfig<Model> config = new IspListingConfig();

	final NamedQuerySearch criteria;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public IspListingWidget() {
		super(config.getListingId(), config.getListingElementName(), new ModelListingTable(config), new ListingNavBar<Model>(config, null));
		criteria = new NamedQuerySearch(SmbizEntityType.ISP, "account.ispList", true);

		setOperator(RemoteListingOperator.create(config.getListingId(),
				ListHandlerType.PAGE, criteria, config.getModelProperties(),
				config.getPageSize(), config.getDefaultSorting()));
	}

	/*
	@Override
	protected IRowOptionsDelegate getRowOptionsHandler() {
		return new RowHandler();
	}

	@Override
	protected IAddRowDelegate getAddRowHandler() {
		// TODO impl
		return null;
	}
	*/
}
