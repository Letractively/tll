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
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.AccountListingConfig;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.view.ViewLink;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.search.NamedQuerySearch;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * MerchantListingView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class MerchantListingView extends ListingView<MerchantListingViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends AbstractListingViewClass {

		@Override
		public String getName() {
			return "MerchantListingView";
		}

		@Override
		public MerchantListingView newView() {
			return new MerchantListingView();
		}
	}

	/**
	 * Ref of the parent ISP
	 */
	private ModelKey ispRef;

	/**
	 * The link to the parent isp listing view.
	 */
	private final ViewLink ispListingLink = new ViewLink();

	/**
	 * Constructor
	 */
	public MerchantListingView() {
		super();
		addWidget(ispListingLink);
	}

	@Override
	public void doInitialization(MerchantListingViewInitializer r) {
		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		ispListingLink.setViewInitializer(new StaticViewInitializer(IspListingView.klas));
		ispListingLink.setText(ispRef.getName());

		final NamedQuerySearch criteria = new NamedQuerySearch(SmbizEntityType.MERCHANT, "account.merchantList", true);
		criteria.addParam(new StringPropertyValue("ispId", ispRef.getId()));

		final AccountListingConfig config = new AccountListingConfig() {

			private final String listingElementName = SmbizEntityType.MERCHANT.getName();

			private final Column[] columns =
				new Column[] {
				Column.ROW_COUNT_COLUMN, new PropertyBoundColumn("Name", Model.NAME_PROPERTY, "rowData"),
				new PropertyBoundColumn("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "rowData"),
				new PropertyBoundColumn("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "rowData"),
				new PropertyBoundColumn("Status", "status", "rowData"),
				new PropertyBoundColumn("Billing Model", "billingModel", "rowData"),
				new PropertyBoundColumn("Billing Cycle", "billingCycle", "rowData"),
				new PropertyBoundColumn("Store Name", "storeName", "rowData") };

			private final Option cListing = new Option("Customer Listing", App.imgs().arrow_sm_down().createImage());

			private final ModelChangingRowHandler rowOps = new ModelChangingRowHandler() {

				@Override
				protected String getListingElementName() {
					return listingElementName;
				}

				@Override
				protected ViewClass getEditViewClass() {
					return AccountEditView.klas;
				}

				@Override
				protected Option[] getCustomRowOps(int rowIndex) {
					final ModelKey rowRef = listingWidget.getRowKey(rowIndex);
					if(SmbizAdmin.canSetAsCurrent(rowRef, ispRef)) {
						return new Option[] {
							cListing, App.OPTION_SET_CURRENT };
					}
					return new Option[] { cListing };
				}

				@Override
				protected void handleRowOp(String optionText, int rowIndex) {
					if(cListing.getText().equals(optionText)) {
						ViewManager.get().dispatch(
								new ShowViewRequest(new CustomerListingViewInitializer(listingWidget.getRowKey(rowIndex), ispRef)));
					}
					else if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
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
			// TODO temp debug
			public int getPageSize() {
				return 2;
			}

			public IRowOptionsDelegate getRowOptionsHandler() {
				return rowOps;
			}

			public IAddRowDelegate getAddRowHandler() {
				return null;
			}

			@Override
			public ITableCellRenderer<Model, ? extends Column> getCellRenderer() {
				return PropertyBoundCellRenderer.get();
			}
		};

		setListingWidget(ListingFactory.createRemoteListingWidget(config, SmbizEntityType.MERCHANT.toString()
				+ "_LISTING",
				ListHandlerType.PAGE, criteria, null, config.getDefaultSorting()));
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	@Override
	public String getShortViewName() {
		return "Merchant Listing";
	}

	@Override
	public String getLongViewName() {
		assert ispRef != null;
		return "Merchant Listing for " + ispRef.descriptor();
	}
}
