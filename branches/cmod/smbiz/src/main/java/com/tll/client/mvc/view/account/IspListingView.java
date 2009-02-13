/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ListingFactory;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewRequestEvent;
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
public final class IspListingView extends ListingView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("IspListingView");
		}

		@Override
		public IView newView() {
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
	@SuppressWarnings("serial")
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

		final AccountSearch criteria = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.ISP);
		criteria.setNamedQuery("account.ispList");

		final AccountListingConfig config = new AccountListingConfig() {

			private final String listingElementName = SmbizEntityType.ISP.getName();

			private final Column[] columns =
					new Column[] {
						new Column("#", Column.ROW_COUNT_COL_PROP, "i"), new Column("Name", Model.NAME_PROPERTY, "i"),
						new Column("Created", Model.DATE_CREATED_PROPERTY, "i", GlobalFormat.DATE),
						new Column("Modified", Model.DATE_MODIFIED_PROPERTY, "i", GlobalFormat.DATE),
						new Column("Status", "status", "i"), new Column("Billing Model", "billingModel", "i"),
						new Column("Billing Cycle", "billingCycle", "i") };

			private final ModelChangingRowOpDelegate rowOps = new ModelChangingRowOpDelegate() {

				@Override
				protected String getListingElementName() {
					return listingElementName;
				}

				@Override
				protected Widget getSourcingWidget() {
					return IspListingView.this;
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
						ViewManager.instance().dispatch(
								MerchantListingView.klas.newViewRequest(IspListingView.this, listingWidget.getRowRef(rowIndex)));
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
		};

		setListingWidget(ListingFactory.createListingWidget(this, config, SmbizEntityType.ISP.toString() + "_LISTING",
				ListHandlerType.PAGE, criteria, null, config.getDefaultSorting()));
	}

	@Override
	public String getLongViewName() {
		return "Isp Listing";
	}

	@Override
	public ShowViewRequest newViewRequest() {
		return klas.newViewRequest(this);
	}
}
