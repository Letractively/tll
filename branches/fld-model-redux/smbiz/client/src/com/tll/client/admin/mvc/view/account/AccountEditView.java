/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.admin.mvc.view.account;

import com.tll.client.admin.ui.field.account.AccountPanel;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.field.AbstractFieldGroupModelBinding;
import com.tll.client.model.Model;
import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.model.EntityType;
import com.tll.service.app.RefDataType;

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
	 * AccountEditBinding
	 * @author jpk
	 */
	private static final class AccountEditBinding extends AbstractFieldGroupModelBinding {

		@Override
		protected Model doResolveModel(EntityType type) {
			if(type == EntityType.ACCOUNT) {
				return getModel(null);
			}
			throw new IllegalArgumentException("Unsupported model type");
		}
	}

	/**
	 * Constructor
	 */
	public AccountEditView() {
		super(new AccountEditBinding(), new AccountPanel(), entityOptions);
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
