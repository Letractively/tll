/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.ui.listing;

import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.listing.AbstractAccountListingConfig;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.RemoteListingOperator;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.ui.option.Option;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.search.NamedQuerySearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.sort.Sorting;

/**
 * CustomerListingWidget
 * @author jpk
 */
public class CustomerListingWidget extends ModelListingWidget<ModelListingTable> {

	/**
	 * CustomerListingConfig
	 * @author jpk
	 */
	static class CustomerListingConfig extends AbstractAccountListingConfig {

		private static final String listingElementName = SmbizEntityType.CUSTOMER.getName();

		private static final Sorting defaultSorting = new Sorting("customer.name");

		private static final Column[] cols = new Column[] {
			Column.ROW_COUNT_COLUMN,
			new Column("Name", "customer.name", "c"),
			new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "ca"),
			new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "ca"),
			new Column("Status", "status", "ca"),
			new Column("Initial Visitor", "initialVisitorRecord.remoteHost", "ca"),
			new Column("Billing Model", "customer.billingModel", "ca"),
			new Column("Billing Cycle", "customer.billingCycle", "ca"),
		};

		private static final String[] mprops = new String[] {
			"customer.id",
			"customer.name",
			Model.DATE_CREATED_PROPERTY,
			Model.DATE_MODIFIED_PROPERTY,
			"source",
			"status",
			"initialVisitorRecord.remoteHost",
			"customer.billingModel",
			"customer.billingCycle",
		};

		/**
		 * Constructor
		 */
		public CustomerListingConfig() {
			super(listingElementName, mprops, cols, defaultSorting);
		}
	}

	class RowHandler extends AbstractRowOptions {

		@Override
		protected Option[] getCustomRowOps(int rowIndex) {
			final ModelKey rowRef = getRowKey(rowIndex);
			if(SmbizAdmin.canSetAsCurrent(rowRef, parentAccountRef)) {
				return new Option[] { App.OPTION_SET_CURRENT };
			}
			return null;
		}

		@Override
		protected void handleRowOp(String optionText, int rowIndex) {
			if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
				SmbizAdmin.getAdminContextCmd().changeCurrentAccount(getRowKey(rowIndex));
			}
		}

		@Override
		protected String getListingElementName() {
			return config.getListingElementName();
		}

		@Override
		protected void doEditRow(int rowIndex) {
			final Model rowData = getRowData(rowIndex);
			final String cid = rowData.asString("customer.id");
			final ModelKey ck = new ModelKey(SmbizEntityType.CUSTOMER, cid, null);
			ViewManager.get().dispatch(
					new ShowViewRequest(new EditViewInitializer(AccountEditView.klas, ck)));
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			CrudCommand.deleteModel(CustomerListingWidget.this, getRowKey(rowIndex)).execute();
		}
	} // Row Handler

	static final IListingConfig<Model> config = new CustomerListingConfig();

	final NamedQuerySearch criteria;

	final ModelKey parentAccountRef;

	/**
	 * Constructor
	 * @param parentAccountRef Ref to the account that is parent to the listed
	 *        customers
	 */
	@SuppressWarnings("unchecked")
	public CustomerListingWidget(ModelKey parentAccountRef) {
		super(config.getListingId(), config.getListingElementName(), new ModelListingTable(config), new ListingNavBar<Model>(config, null));
		this.parentAccountRef = parentAccountRef;
		criteria = new NamedQuerySearch(SmbizEntityType.CUSTOMER, "account.customerList", true);
		criteria.addParam(new StringPropertyValue("parentId", parentAccountRef.getId()));

		setOperator(RemoteListingOperator.create(config.getListingId(),
				ListHandlerType.PAGE, criteria, config.getModelProperties(),
				config.getPageSize(), config.getDefaultSorting()));
	}

	public ModelKey getParentAccountRef() {
		return parentAccountRef;
	}

	protected IRowOptionsDelegate getRowOptionsHandler() {
		return new RowHandler();
	}

	protected IAddRowDelegate getAddRowHandler() {
		// TODO impl
		return null;
	}
}
