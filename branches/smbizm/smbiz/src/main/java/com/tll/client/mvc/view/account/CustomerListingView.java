/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.PropertyBoundCellRenderer;
import com.tll.client.listing.PropertyBoundColumn;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.AccountListingConfig;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.view.ViewLink;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * CustomerListingView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class CustomerListingView extends ListingView<CustomerListingViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends AbstractListingViewClass {

		@Override
		public String getName() {
			return "CustomerListingView";
		}

		@Override
		public CustomerListingView newView() {
			return new CustomerListingView();
		}
	}

	private ModelKey mercRef;

	/**
	 * The link to the parent merchant listing view.
	 */
	private final ViewLink mercListingLink = new ViewLink();

	/**
	 * Constructor
	 */
	public CustomerListingView() {
		super();
		addWidget(mercListingLink);
	}

	@Override
	public void doInitialization(CustomerListingViewInitializer r) {
		assert r.mercRef != null && r.mercRef.isSet();
		mercRef = r.mercRef;

		if(r.getMerchantParentRef() != null) {
			mercListingLink.setViewInitializer(new MerchantListingViewInitializer(r.getMerchantParentRef()));
			mercListingLink.setText(r.getMerchantParentRef().getName());
		}
		mercListingLink.setVisible(r.getMerchantParentRef() != null);

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.CUSTOMER);
		criteria.setNamedQuery("account.customerList");
		criteria.setQueryParam(new IntPropertyValue("merchantId", mercRef.getId()));

		final AccountListingConfig config = new AccountListingConfig() {

			private final String listingElementName = SmbizEntityType.CUSTOMER.getName();

			private final PropertyBoundColumn cName = new PropertyBoundColumn("Name", Model.NAME_PROPERTY, "c");
			private final PropertyBoundColumn cDCreated =
				new PropertyBoundColumn("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "ca");
			private final PropertyBoundColumn cDModified =
				new PropertyBoundColumn("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "ca");
			private final PropertyBoundColumn cStatus = new PropertyBoundColumn("Status", "status", "ca");
			private final PropertyBoundColumn cBillingModel = new PropertyBoundColumn("Billing Model", "billingModel", "ca");
			private final PropertyBoundColumn cBillingCycle = new PropertyBoundColumn("Billing Cycle", "billingCycle", "ca");

			private final Column[] columns =
				new Column[] {
				Column.ROW_COUNT_COLUMN, cName, cDCreated, cDModified, cStatus, cBillingModel, cBillingCycle };

			private final ModelChangingRowHandler rowOps = new ModelChangingRowHandler() {

				@Override
				protected ViewClass getEditViewClass() {
					return AccountEditView.klas;
				}

				@Override
				protected String getListingElementName() {
					return listingElementName;
				}

				@Override
				protected Option[] getCustomRowOps(int rowIndex) {
					final ModelKey rowRef = listingWidget.getRowKey(rowIndex);
					if(SmbizAdmin.canSetAsCurrent(rowRef, mercRef)) {
						return new Option[] { App.OPTION_SET_CURRENT };
					}
					return null;
				}

				@Override
				protected void handleRowOp(String optionText, int rowIndex) {
					if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
						SmbizAdmin.getAdminContextCmd().changeCurrentAccount(listingWidget.getRowKey(rowIndex));
					}
				}
			};

			public String getListingElementName() {
				return listingElementName;
			}

			public Sorting getDefaultSorting() {
				return new Sorting(new SortColumn(Model.NAME_PROPERTY));
			}

			public Column[] getColumns() {
				return columns;
			}

			@Override
			public ITableCellRenderer<Model, ? extends Column> getCellRenderer() {
				return PropertyBoundCellRenderer.get();
			}

			public IRowOptionsDelegate getRowOptionsHandler() {
				return rowOps;
			}

			public IAddRowDelegate getAddRowHandler() {
				// TODO
				return null;
			}
		};

		setListingWidget(ListingFactory.createRemoteListingWidget(config, SmbizEntityType.CUSTOMER.toString()
				+ "_LISTING",
				ListHandlerType.PAGE, criteria, null, config.getDefaultSorting()));
	}

	@Override
	public String getShortViewName() {
		return "Customer Listing";
	}

	@Override
	public String getLongViewName() {
		assert mercRef != null;
		return "Customer Listing for " + mercRef.descriptor();
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}
}
