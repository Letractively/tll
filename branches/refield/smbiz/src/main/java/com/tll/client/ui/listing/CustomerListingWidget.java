/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.ui.listing;

import com.tll.client.App;
import com.tll.client.SmbizAdmin;
import com.tll.client.listing.CustomerListingConfig;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ModelChangingRowHandler;
import com.tll.client.listing.RemoteListingOperator;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.ui.option.Option;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.model.StringPropertyValue;
import com.tll.common.search.NamedQuerySearch;
import com.tll.listhandler.ListHandlerType;

/**
 * CustomerListingWidget
 * @author jpk
 */
public class CustomerListingWidget extends RemoteListingWidget {

	class RowHandler extends ModelChangingRowHandler {

		@Override
		protected Option[] getCustomRowOps(int rowIndex) {
			final ModelKey rowRef = listingWidget.getRowKey(rowIndex);
			if(SmbizAdmin.canSetAsCurrent(rowRef, parentAccountRef)) {
				return new Option[] { App.OPTION_SET_CURRENT };
			}
			return null;
		}

		@Override
		protected void handleRowOp(String optionText, int rowIndex) {
			if(App.OPTION_SET_CURRENT.getText().equals(optionText)) {
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

	static final IListingConfig<Model> config = new CustomerListingConfig();

	final NamedQuerySearch criteria;

	final ModelKey parentAccountRef;

	/**
	 * Constructor
	 * @param parentAccountRef Ref to the account that is parent to the listed
	 *        customers
	 */
	public CustomerListingWidget(ModelKey parentAccountRef) {
		super(config);
		this.parentAccountRef = parentAccountRef;
		criteria = new NamedQuerySearch(SmbizEntityType.CUSTOMER, "account.customerList", true);
		criteria.addParam(new StringPropertyValue("merchantId", parentAccountRef.getId()));

		setOperator(RemoteListingOperator.create(config.getListingId(),
				ListHandlerType.PAGE, criteria, config.getModelProperties(),
				config.getPageSize(), config.getDefaultSorting()));
	}

	public ModelKey getParentAccountRef() {
		return parentAccountRef;
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
