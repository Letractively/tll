/**
 * 
 */
package com.tll.client.admin.mvc.view.account;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.ui.listing.AccountListingConfig;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.PropKey;
import com.tll.client.event.IRowOptionListener;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.RowOptionEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsProvider;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.Dispatcher;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.impl.AccountSearch;
import com.tll.client.ui.Option;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.util.GlobalFormat;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * MerchantListingView
 * @author jpk
 */
public final class MerchantListingView extends ListingView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("MerchantListingView");
		}

		@Override
		public AbstractView newView() {
			return new MerchantListingView();
		}

		/**
		 * Factory method used to generate a {@link ViewRequestEvent} for
		 * {@link MerchantListingView} instances.
		 * @param source
		 * @param ispRef The Isp ref
		 * @return MerchantListingViewRequest
		 */
		public MerchantListingViewRequest newViewRequest(Widget source, RefKey ispRef) {
			return new MerchantListingViewRequest(source, ispRef);
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
		private final RefKey ispRef;

		/**
		 * Constructor
		 * @param source
		 * @param ispRef The required parent isp ref
		 */
		MerchantListingViewRequest(Widget source, RefKey ispRef) {
			super(source, klas);

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
	private RefKey ispRef;

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
	public void doInitialization(ViewRequestEvent viewRequest) {
		assert viewRequest instanceof MerchantListingViewRequest;
		MerchantListingViewRequest r = (MerchantListingViewRequest) viewRequest;

		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		ispListingLink.setViewRequest(IspListingView.klas.newViewRequest(this));
		ispListingLink.setText(ispRef.getName());

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, EntityType.MERCHANT);
		criteria.setNamedQuery(SelectNamedQuery.MERCHANT_LISTING);
		criteria.setQueryParam("ispId", ispRef.getId().toString());

		final IListingConfig config = new AccountListingConfig() {

			public Sorting getDefaultSorting() {
				return new Sorting(new SortColumn(Model.NAME_PROPERTY, "m"));
			}

			private final PropKey[] propKeys = new PropKey[] {
				new PropKey(Model.ID_PROPERTY),
				new PropKey(Model.NAME_PROPERTY),
				new PropKey(Model.DATE_CREATED_PROPERTY),
				new PropKey(Model.DATE_MODIFIED_PROPERTY),
				new PropKey("status"),
				new PropKey("billingModel"),
				new PropKey("billingCycle"),
				new PropKey("storeName") };

			private final Column[] columns = new Column[] {
				new Column("#", Column.ROW_COUNT_COL_PROP, null),
				new Column("Name", Model.NAME_PROPERTY, "m"),
				new Column("Created", Model.DATE_CREATED_PROPERTY, "m", GlobalFormat.DATE),
				new Column("Modified", Model.DATE_MODIFIED_PROPERTY, "m", GlobalFormat.DATE),
				new Column("Status", "status", "m"),
				new Column("Billing Model", "billingModel", "m"),
				new Column("Billing Cycle", "billingCycle", "m"),
				new Column("Store Name", "storeName", "m") };

			public PropKey[] getPropKeys() {
				return propKeys;
			}

			public String getListingElementName() {
				return "Merchant";
			}

			public Column[] getColumns() {
				return columns;
			}

			@Override
			// TODO temp debug
			public int getPageSize() {
				return 2;
			}

		};

		final IRowOptionsProvider rop = new IRowOptionsProvider() {

			private final Option[] rowContextOptions = new Option[] {
				Option.editOption(config.getListingElementName()),
				Option.deleteOption(config.getListingElementName()),
				new Option("Customer Listing", App.imgs().arrow_sm_down().createImage()) };

			public boolean isStaticOptions() {
				return true;
			}

			public Option[] getOptions(RefKey rowRef) {
				return rowContextOptions;
			}

			public IRowOptionListener getRowOptionListener() {
				return MerchantListingView.this;
			}
		};

		setListingWidget(ListingFactory.rpcListing(config, criteria, IListingCommand.LIST_HANDLER_TYPE_PAGE, rop));
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	public String getShortViewName() {
		return "Merchant Listing";
	}

	public String getLongViewName() {
		assert ispRef != null;
		return "Merchant Listing for " + ispRef.descriptor();
	}

	@Override
	public ShowViewRequest newViewRequest() {
		return klas.newViewRequest(this, ispRef);
	}

	@Override
	protected void doEditRow(RefKey rowRef) {
		Dispatcher.instance().dispatch(new EditViewRequest(this, AccountEditView.klas, rowRef));
	}

	@Override
	protected void doCustomRowOption(RowOptionEvent rowOption) {
		ViewRequestEvent vr = CustomerListingView.klas.newViewRequest(rowOption.getWidget(), rowOption.rowRef, ispRef);
		Dispatcher.instance().dispatch(vr);
	}
}
