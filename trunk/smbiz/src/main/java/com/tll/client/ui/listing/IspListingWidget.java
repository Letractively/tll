/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.ui.listing;

import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.IspListingConfig;
import com.tll.client.listing.ModelChangingRowHandler;
import com.tll.client.listing.RemoteListingOperator;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.mvc.view.account.MerchantListingViewInitializer;
import com.tll.client.ui.option.Option;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.NamedQuerySearch;
import com.tll.listhandler.ListHandlerType;

/**
 * IspListingWidget
 * @author jpk
 */
public class IspListingWidget extends RemoteListingWidget {

	static final Option mListing = new Option("Merchant Listing", App.imgs().arrow_sm_down().createImage());

	class RowHandler extends ModelChangingRowHandler {

		@Override
		protected Option[] getCustomRowOps(int rowIndex) {
			return new Option[] { mListing, App.OPTION_SET_CURRENT, };
		}

		@Override
		protected void handleRowOp(String optionText, int rowIndex) {
			if(mListing.getText().equals(optionText)) {
				ViewManager.get().dispatch(
						new ShowViewRequest(new MerchantListingViewInitializer(listingWidget.getRowKey(rowIndex))));
			}
			else if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
				SmbizAdmin.getAdminContextCmd().changeCurrentAccount(listingWidget.getRowKey(rowIndex));
			}
		}

		@Override
		protected ViewClass getEditViewClass() {
			return AccountEditView.klas;
		}

		@Override
		protected String getListingElementName() {
			return config.getListingElementName();
		}
	} // Row Handler

	static final IListingConfig<Model> config = new IspListingConfig();

	final NamedQuerySearch criteria;

	/**
	 * Constructor
	 */
	public IspListingWidget() {
		super(config);
		criteria = new NamedQuerySearch(SmbizEntityType.ISP, "account.ispList", true);

		setOperator(RemoteListingOperator.create(config.getListingId(),
				ListHandlerType.PAGE, criteria, config.getModelProperties(),
				config.getPageSize(), config.getDefaultSorting()));
	}

	@Override
	protected IRowOptionsDelegate getRowOptionsHandler() {
		return new RowHandler();
	}

	@Override
	protected IAddRowDelegate getAddRowHandler() {
		// TODO impl
		return null;
	}
}
