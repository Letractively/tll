/**
 * 
 */
package com.tll.client.admin.mvc.view.account;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.listing.AccountListingConfig;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.impl.AccountSearch;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.util.GlobalFormat;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * CustomerListingView
 * @author jpk
 */
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
		 * Factory method used to generate a {@link ViewRequestEvent} for
		 * {@link CustomerListingView} instances.
		 * @param source
		 * @param mercRef The Merchant ref
		 * @param ispRef The Isp ref
		 * @return {@link ViewRequestEvent}
		 */
		public CustomerListingViewRequest newViewRequest(Widget source, RefKey mercRef, RefKey ispRef) {
			return new CustomerListingViewRequest(source, mercRef, ispRef);
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
		private final RefKey ispRef;

		/**
		 * The parent Merchant ref.
		 */
		private final RefKey mercRef;

		/**
		 * Constructor
		 * @param source
		 * @param mercRef The parent merchant ref
		 */
		CustomerListingViewRequest(Widget source, RefKey mercRef, RefKey ispRef) {
			super(source, klas);
			assert mercRef != null && ispRef != null;
			this.mercRef = mercRef;
			this.ispRef = ispRef;
		}

		@Override
		protected int getViewId() {
			return klas.hashCode() + 7 * ispRef.hashCode() + 19 * mercRef.hashCode();
		}

	}

	private RefKey ispRef;

	private RefKey mercRef;

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
	public void doInitialization(ViewRequestEvent viewRequest) {
		assert viewRequest instanceof CustomerListingViewRequest;
		CustomerListingViewRequest r = (CustomerListingViewRequest) viewRequest;

		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		assert r.mercRef != null && r.mercRef.isSet();
		mercRef = r.mercRef;

		mercListingLink.setViewRequest(MerchantListingView.klas.newViewRequest(this, r.ispRef));
		mercListingLink.setText(mercRef.getName());

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, EntityType.CUSTOMER);
		criteria.setNamedQuery(SelectNamedQuery.CUSTOMER_LISTING);
		criteria.setQueryParam(new IntPropertyValue("merchantId", mercRef.getId()));

		final IListingConfig config = new AccountListingConfig() {

			public String getListingName() {
				return EntityType.CUSTOMER.name() + "_LISTING";
			}

			public String getListingElementName() {
				return EntityType.CUSTOMER.getName();
			}

			public Sorting getDefaultSorting() {
				return new Sorting(new SortColumn(Model.NAME_PROPERTY, "c"));
			}

			private final Column[] columns = new Column[] {
				new Column("#", Column.ROW_COUNT_COL_PROP, null),
				new Column("Name", Model.NAME_PROPERTY, "c"),
				new Column("Created", Model.DATE_CREATED_PROPERTY, "ca", GlobalFormat.DATE),
				new Column("Modified", Model.DATE_MODIFIED_PROPERTY, "ca", GlobalFormat.DATE),
				new Column("Status", "status", "ca"),
				new Column("Billing Model", "billingModel", "ca"),
				new Column("Billing Cycle", "billingCycle", "ca") };

			public Column[] getColumns() {
				return columns;
			}

		};

		setListingWidget(ListingFactory.rpcListing(config, criteria, ListHandlerType.PAGE,
				new ModelChangingRowOpDelegate() {

					@Override
					protected Widget getSourcingWidget() {
						return CustomerListingView.this;
					}

					@Override
					protected ViewClass getEditViewClass() {
						return AccountEditView.klas;
					}

					@Override
					protected String getListingElementName() {
						return config.getListingElementName();
					}

				}, null));
	}

	@Override
	public String getShortViewName() {
		return "Customer Listing";
	}

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
		return klas.newViewRequest(this, mercRef, ispRef);
	}
}
