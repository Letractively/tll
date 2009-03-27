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
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.AccountListingConfig;
import com.tll.client.ui.option.Option;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.dao.SortColumn;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.SmbizEntityType;

/**
 * IspListingView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class IspListingView extends ListingView<StaticViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("IspListingView");
		}

		@Override
		public IspListingView newView() {
			return new IspListingView();
		}
	}

	/**
	 * Constructor
	 */
	public IspListingView() {
		super();
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	@Override
	protected void doInitialization(StaticViewInitializer init) {

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.ISP);
		criteria.setNamedQuery("account.ispList");

		final AccountListingConfig config = new AccountListingConfig() {

			private final String listingElementName = SmbizEntityType.ISP.getName();

			private final Column[] columns =
					new Column[] {
						Column.ROW_COUNT_COLUMN, new PropertyBoundColumn("Name", Model.NAME_PROPERTY, "i"),
						new PropertyBoundColumn("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "i"),
						new PropertyBoundColumn("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "i"),
						new PropertyBoundColumn("Status", "status", "i"),
						new PropertyBoundColumn("Billing Model", "billingModel", "i"),
						new PropertyBoundColumn("Billing Cycle", "billingCycle", "i") };

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
					return new Option[] { new Option("Merchant Listing", App.imgs().arrow_sm_down().createImage()) };
				}

				@Override
				protected void handleRowOp(String optionText, int rowIndex) {
					if(optionText.indexOf("Merchant Listing") == 0) {
						ViewManager.get().dispatch(
								new ShowViewRequest(new MerchantListingViewInitializer(listingWidget.getRowKey(rowIndex))));
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

			public IRowOptionsDelegate getRowOptionsHandler() {
				return rowOps;
			}

			public IAddRowDelegate getAddRowHandler() {
				// TODO
				return null;
			}

			@Override
			public ITableCellRenderer<Model, ? extends Column> getCellRenderer() {
				return PropertyBoundCellRenderer.get();
			}
		};

		setListingWidget(ListingFactory.createRemoteListingWidget(config,
				SmbizEntityType.ISP.toString() + "_LISTING",
				ListHandlerType.PAGE, criteria, null, config.getDefaultSorting()));
	}

	@Override
	public String getLongViewName() {
		return "Isp Listing";
	}
}
