/**
 * 
 */
package com.tll.client.admin.mvc.view.account;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.listing.AccountListingConfig;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.PropKey;
import com.tll.client.event.IRowOptionListener;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsProvider;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.IEntityType;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.Dispatcher;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.ISearch;
import com.tll.client.search.impl.AccountSearch;
import com.tll.client.ui.Option;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.util.GlobalFormat;

/**
 * CustomerListingView
 * @author jpk
 */
public final class CustomerListingView extends ListingView implements IRowOptionListener {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("CustomerListingView");
		}

		@Override
		public AbstractView newView() {
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

		final AccountSearch criteria = new AccountSearch(ISearch.TYPE_SCALER_QUERY, IEntityType.CUSTOMER);
		criteria.setQueryName("account.customerList");
		criteria.setQueryParam("merchantId", mercRef.getId().toString());

		final IListingConfig config = new AccountListingConfig() {

			private final PropKey[] propKeys = new PropKey[] {
				new PropKey(Model.ID_PROPERTY),
				new PropKey(Model.NAME_PROPERTY),
				new PropKey(Model.DATE_CREATED_PROPERTY),
				new PropKey(Model.DATE_MODIFIED_PROPERTY),
				new PropKey("status"),
				new PropKey("billingModel"),
				new PropKey("billingCycle") };

			private final Column[] columns = new Column[] {
				new Column("#", Column.ROW_COUNT_COL_PROP),
				new Column("Name", Model.NAME_PROPERTY),
				new Column("Created", Model.DATE_CREATED_PROPERTY, GlobalFormat.DATE),
				new Column("Modified", Model.DATE_MODIFIED_PROPERTY, GlobalFormat.DATE),
				new Column("Status", "status"),
				new Column("Billing Model", "billingModel"),
				new Column("Billing Cycle", "billingCycle") };

			public PropKey[] getPropKeys() {
				return propKeys;
			}

			public String getListingElementName() {
				return "Customer";
			}

			public Column[] getColumns() {
				return columns;
			}

		};

		IRowOptionsProvider rop = new IRowOptionsProvider() {

			private final Option[] rowContextOptions = new Option[] {
				Option.editOption(config.getListingElementName()),
				Option.deleteOption(config.getListingElementName()) };

			public boolean isStaticOptions() {
				return true;
			}

			public Option[] getOptions(RefKey rowRef) {
				return rowContextOptions;
			}

			public IRowOptionListener getRowOptionListener() {
				return CustomerListingView.this;
			}
		};

		setListingWidget(ListingFactory.rpcListing(config, criteria, IListingCommand.LIST_HANDLER_TYPE_PAGE, rop));
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

	@Override
	protected void doEditRow(RefKey rowRef) {
		Dispatcher.instance().dispatch(new EditViewRequest(this, AccountEditView.klas, rowRef));
	}
}
