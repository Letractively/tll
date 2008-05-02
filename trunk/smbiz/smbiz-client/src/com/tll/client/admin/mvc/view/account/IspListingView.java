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
import com.tll.client.util.GlobalFormat;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.SelectNamedQuery;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * IspListingView
 * @author jpk
 */
public final class IspListingView extends ListingView implements IRowOptionListener {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("IspListingView");
		}

		@Override
		public AbstractView newView() {
			return new IspListingView();
		}

		public IspListingViewRequest newViewRequest(Widget source) {
			return new IspListingViewRequest(this, source);
		}
	}

	/**
	 * IspListingViewRequest
	 * @author jpk
	 */
	public static final class IspListingViewRequest extends ShowViewRequest {

		/**
		 * Constructor
		 * @param source
		 */
		IspListingViewRequest(Class viewClass, Widget source) {
			super(source, viewClass);
		}

		@Override
		protected int getViewId() {
			return klas.hashCode();
		}

	}

	/**
	 * Constructor
	 */
	public IspListingView() {
		super();
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected void doInitialization(ViewRequestEvent viewRequest) {

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, EntityType.ISP);
		criteria.setNamedQuery(SelectNamedQuery.ISP_LISTING);

		final IListingConfig config = new AccountListingConfig() {

			public Sorting getDefaultSorting() {
				return new Sorting(new SortColumn(Model.NAME_PROPERTY, "i"));
			}

			private final PropKey[] propKeys = new PropKey[] {
				new PropKey(Model.ID_PROPERTY),
				new PropKey(Model.NAME_PROPERTY),
				new PropKey(Model.DATE_CREATED_PROPERTY),
				new PropKey(Model.DATE_MODIFIED_PROPERTY),
				new PropKey("status"),
				new PropKey("billingModel"),
				new PropKey("billingCycle") };

			private final Column[] columns = new Column[] {
				new Column("#", Column.ROW_COUNT_COL_PROP, "i"),
				new Column("Name", Model.NAME_PROPERTY, "i"),
				new Column("Created", Model.DATE_CREATED_PROPERTY, "i", GlobalFormat.DATE),
				new Column("Modified", Model.DATE_MODIFIED_PROPERTY, "i", GlobalFormat.DATE),
				new Column("Status", "status", "i"),
				new Column("Billing Model", "billingModel", "i"),
				new Column("Billing Cycle", "billingCycle", "i") };

			public PropKey[] getPropKeys() {
				return propKeys;
			}

			public String getListingElementName() {
				return "Isp";
			}

			public Column[] getColumns() {
				return columns;
			}

		};

		IRowOptionsProvider rop = new IRowOptionsProvider() {

			private final Option[] rowContextOptions = new Option[] {
				Option.editOption(config.getListingElementName()),
				Option.deleteOption(config.getListingElementName()),
				new Option("Merchant Listing", App.imgs().arrow_sm_down().createImage()) };

			public boolean isStaticOptions() {
				return true;
			}

			public Option[] getOptions(RefKey rowRef) {
				return rowContextOptions;
			}

			public IRowOptionListener getRowOptionListener() {
				return IspListingView.this;
			}
		};

		setListingWidget(ListingFactory.rpcListing(config, criteria, IListingCommand.LIST_HANDLER_TYPE_PAGE, rop));
	}

	public String getLongViewName() {
		return "Isp Listing";
	}

	@Override
	public ShowViewRequest newViewRequest() {
		return klas.newViewRequest(this);
	}

	@Override
	protected void doEditRow(RefKey rowRef) {
		Dispatcher.instance().dispatch(new EditViewRequest(this, AccountEditView.klas, rowRef));
	}

	@Override
	protected void doCustomRowOption(RowOptionEvent rowOption) {
		if(rowOption.optionText.indexOf("Merchant Listing") == 0) {
			Dispatcher.instance().dispatch(MerchantListingView.klas.newViewRequest(rowOption.getWidget(), rowOption.rowRef));
		}
	}
}
