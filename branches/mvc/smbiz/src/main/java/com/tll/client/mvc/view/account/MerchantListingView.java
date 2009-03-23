/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.App;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.PropertyBoundCellRenderer;
import com.tll.client.listing.PropertyBoundColumn;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.AccountListingConfig;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.SmbizEntityType;

/**
 * MerchantListingView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class MerchantListingView extends ListingView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("MerchantListingView");
		}

		@Override
		public IView newView() {
			return new MerchantListingView();
		}

		/**
		 * Factory method used to generate a {@link IViewRequest} for
		 * {@link MerchantListingView} instances.
		 * @param ispRef The Isp ref
		 * @return MerchantListingViewRequest
		 */
		public MerchantListingViewRequest newViewRequest(ModelKey ispRef) {
			return new MerchantListingViewRequest(ispRef);
		}

	}

	/**
	 * MerchantListingViewRequest - MerchantListingView specific view request.
	 * @author jpk
	 */
	public static final class MerchantListingViewRequest extends ShowViewRequest {

		/**
		 * The parent Isp ref.
		 */
		private final ModelKey ispRef;

		/**
		 * Constructor
		 * @param ispRef The required parent isp ref
		 */
		MerchantListingViewRequest(ModelKey ispRef) {
			super(klas);

			assert ispRef != null;
			this.ispRef = ispRef;
		}

		@Override
		protected int getViewId() {
			return klas.hashCode() + 7 * ispRef.hashCode();
		}
	}

	/**
	 * Ref of the parent ISP
	 */
	private ModelKey ispRef;

	/**
	 * The link to the parent isp listing view.
	 */
	private final ViewRequestLink ispListingLink = new ViewRequestLink();

	/**
	 * Constructor
	 */
	public MerchantListingView() {
		super();
		addWidget(ispListingLink);
	}

	@Override
	public void doInitialization(IViewRequest viewRequest) {
		assert viewRequest instanceof MerchantListingViewRequest;
		final MerchantListingViewRequest r = (MerchantListingViewRequest) viewRequest;

		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		ispListingLink.setViewRequest(IspListingView.klas.newViewRequest());
		ispListingLink.setText(ispRef.getName());

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.MERCHANT);
		criteria.setNamedQuery("account.merchantList");
		criteria.setQueryParam(new IntPropertyValue("ispId", ispRef.getId()));

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

			private final ModelChangingRowOpDelegate rowOps = new ModelChangingRowOpDelegate() {

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
					return new Option[] { new Option("Customer Listing", App.imgs().arrow_sm_down().createImage()) };
				}

				@Override
				protected void handleRowOp(String optionText, int rowIndex) {
					if(optionText.indexOf("Customer Listing") == 0) {
						ViewManager.get().dispatch(
								CustomerListingView.klas.newViewRequest(listingWidget.getRowKey(rowIndex),
										ispRef));
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
	protected ViewClass getViewClass() {
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

	@Override
	public ShowViewRequest newViewRequest() {
		return klas.newViewRequest(ispRef);
	}
}
