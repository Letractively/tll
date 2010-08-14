/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.listing.AbstractAccountListingConfig;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.rpc.RemoteListingOperator;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.account.CustomerListingViewInitializer;
import com.tll.client.ui.option.Option;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.search.NamedQuerySearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * MerchantListingWidget
 * @author jpk
 */
public class MerchantListingWidget extends ModelListingWidget<ModelListingTable> {

	/**
	 * MerchantListingConfig
	 * @author jpk
	 */
	static class MerchantListingConfig extends AbstractAccountListingConfig {

		private static final String listingElementName = SmbizEntityType.MERCHANT.getName();

		private static final Sorting defaultSorting = new Sorting("name");

		private static final Column[] cols = new Column[] {
			Column.ROW_COUNT_COLUMN,
			new Column("Name", Model.NAME_PROPERTY, "rowData"),
			new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "rowData"),
			new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "rowData"),
			new Column("Status", "status", "rowData"),
			new Column("Billing Model", "billingModel", "rowData"),
			new Column("Billing Cycle", "billingCycle", "rowData"),
			new Column("Store Name", "storeName", "rowData")
		};

		private static final String[] mprops = new String[] {
			Model.NAME_PROPERTY,
			Model.DATE_CREATED_PROPERTY,
			Model.DATE_MODIFIED_PROPERTY,
			"status",
			"billingModel",
			"billingCycle",
			"storeName",
		};

		/**
		 * Constructor
		 */
		public MerchantListingConfig() {
			super(listingElementName, mprops, cols, defaultSorting);
		}
	}

	class RowHandler extends AbstractRowOptions {

		@Override
		protected Option[] getCustomRowOps(int rowIndex) {
			final ModelKey rowRef = getRowKey(rowIndex);
			if(SmbizAdmin.canSetAsCurrent(rowRef, parentAccountRef)) {
				return new Option[] {
					cListing, App.OPTION_SET_CURRENT };
			}
			return new Option[] { cListing };
		}

		@Override
		protected void handleRowOp(String optionText, int rowIndex) {
			if(cListing.getText().equals(optionText)) {
				ViewManager.get().dispatch(
						new ShowViewRequest(new CustomerListingViewInitializer(getRowKey(rowIndex), parentAccountRef)));
			}
			else if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
				SmbizAdmin.getAdminContextCmd().changeCurrentAccount(getRowKey(rowIndex));
			}
		}

		@Override
		protected String getListingElementName() {
			return config.getListingElementName();
		}
	} // Row Handler

	static final IListingConfig<Model> config = new MerchantListingConfig();

	static final Option cListing = new Option("Customer Listing", AbstractImagePrototype.create(App.imgs().arrow_sm_down()).createImage());

	final NamedQuerySearch criteria;

	final ModelKey parentAccountRef;

	/**
	 * Constructor
	 * @param parentAccountRef Ref to the account that is parent to the listed
	 *        customers
	 */
	@SuppressWarnings("unchecked")
	public MerchantListingWidget(ModelKey parentAccountRef) {
		super(config.getListingId(), config.getListingElementName(), new ModelListingTable(config), null);
		this.parentAccountRef = parentAccountRef;
		criteria = new NamedQuerySearch(SmbizEntityType.MERCHANT, "account.merchantList", true);
		criteria.addParam(new StringPropertyValue("parentId", parentAccountRef.getId()));

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
