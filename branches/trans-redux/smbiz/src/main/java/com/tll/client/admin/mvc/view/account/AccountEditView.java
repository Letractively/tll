/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.admin.mvc.view.account;

import com.tll.client.admin.ui.field.account.AccountPanel;
import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.Model;
import com.tll.model.EntityType;
import com.tll.refdata.RefDataType;

/**
 * AccountEditView - Base AbstractView for editing accounts.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public class AccountEditView extends EditView {

	/**
	 * TODO This is temporary until account specific edit views are created!
	 */
	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private Class() {
			super("accountEdit");
		}

		@Override
		public IView newView() {
			return new AccountEditView();
		}

	}

	private static final EntityOptions entityOptions = new EntityOptions();

	static {
		entityOptions.requestRelated(EntityType.PAYMENT_INFO);
		entityOptions.requestRelated(EntityType.ACCOUNT_ADDRESS);
		entityOptions.requestRelatedRef(EntityType.ACCOUNT);
	}

	/**
	 * Constructor
	 */
	public AccountEditView() {
		super(new AccountPanel<Model>(), entityOptions);
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected AuxDataRequest getNeededAuxData() {
		AuxDataRequest auxDataRequest = new AuxDataRequest();
		auxDataRequest.requestEntityList(EntityType.CURRENCY);
		auxDataRequest.requestAppRefData(RefDataType.US_STATES);
		auxDataRequest.requestAppRefData(RefDataType.ISO_COUNTRY_CODES);
		auxDataRequest.requestEntityPrototype(EntityType.ACCOUNT_ADDRESS);
		return auxDataRequest;
	}

}
