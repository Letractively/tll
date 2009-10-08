/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.field.account.AccountPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.SmbizEntityType;
import com.tll.refdata.RefDataType;

/**
 * AccountEditView - Base AbstractView for editing accounts.
 * @author jpk
 */
public class AccountEditView extends EditView {

	/**
	 * TODO This is temporary until account specific edit views are created!
	 */
	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "accountEdit";
		}

		@Override
		public AccountEditView newView() {
			return new AccountEditView();
		}

	}

	/**
	 * Constructor
	 */
	public AccountEditView() {
		super(new AccountPanel());
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected AuxDataRequest getNeededAuxData() {
		final AuxDataRequest auxDataRequest = new AuxDataRequest();
		auxDataRequest.requestEntityList(SmbizEntityType.CURRENCY);
		auxDataRequest.requestAppRefData(RefDataType.US_STATES);
		auxDataRequest.requestAppRefData(RefDataType.ISO_COUNTRY_CODES);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.ACCOUNT_ADDRESS);
		return auxDataRequest;
	}

}
