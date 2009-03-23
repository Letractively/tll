/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.PropertyBoundCellRenderer;
import com.tll.client.listing.PropertyBoundColumn;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.AccountListingConfig;
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
 * CustomerListingView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class CustomerListingView extends ListingView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("CustomerListingView");
		}

		@Override
		public IView newView() {
			return new CustomerListingView();
		}

		/**
		 * Factory method used to generate a {@link IViewRequest} for
		 * {@link CustomerListingView} instances.
		 * @param mercRef The Merchant ref
		 * @param ispRef The Isp ref
		 * @return {@link IViewRequest}
		 */
		public CustomerListingViewRequest newViewRequest(ModelKey mercRef, ModelKey ispRef) {
			return new CustomerListingViewRequest(mercRef, ispRef);
		}

	}

	/**
	 * CustomerListingViewRequest - CustomerListingView specific view request.
	 * @author jpk
	 */
	public static final class CustomerListingViewRequest extends ShowViewRequest {

		/**
		 * The grand-parent Isp ref.
		 */
		private final ModelKey ispRef;

		/**
		 * The parent Merchant ref.
		 */
		private final ModelKey mercRef;

		/**
		 * Constructor
		 * @param mercRef The parent merchant ref
		 * @param ispRef
		 */
		CustomerListingViewRequest(ModelKey mercRef, ModelKey ispRef) {
			super(klas);
			assert mercRef != null && ispRef != null;
			this.mercRef = mercRef;
			this.ispRef = ispRef;
		}

		@Override
		protected int getViewId() {
			return klas.hashCode() + 7 * ispRef.hashCode() + 19 * mercRef.hashCode();
		}

	}

	private ModelKey ispRef;

	private ModelKey mercRef;

	/**
	 * The link to the parent merchant listing view.
	 */
	private final ViewRequestLink mercListingLink = new ViewRequestLink();

	/**
	 * Constructor
	 */
	public CustomerListingView() {
		super();
		addWidget(mercListingLink);
	}

	@Override
	public void doInitialization(IViewRequest viewRequest) {
		assert viewRequest instanceof CustomerListingViewRequest;
		final CustomerListingViewRequest r = (CustomerListingViewRequest) viewRequest;

		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		assert r.mercRef != null && r.mercRef.isSet();
		mercRef = r.mercRef;

		mercListingLink.setViewRequest(MerchantListingView.klas.newViewRequest(r.ispRef));
		mercListingLink.setText(mercRef.getName());

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

			private final ModelChangingRowOpDelegate rowOps = new ModelChangingRowOpDelegate() {

				@Override
				protected ViewClass getEditViewClass() {
					return AccountEditView.klas;
				}

				@Override
				protected String getListingElementName() {
					return listingElementName;
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
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	public ShowViewRequest newViewRequest() {
		return klas.newViewRequest(mercRef, ispRef);
	}
}
