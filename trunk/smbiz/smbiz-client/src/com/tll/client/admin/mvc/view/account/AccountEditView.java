/**
 * The Logic Lab
 * @author jpk Jan 14, 2008
 */
package com.tll.client.admin.mvc.view.account;

import com.tll.client.admin.ui.field.AccountPanel;
import com.tll.client.data.EntityOptions;
import com.tll.client.model.IEntityType;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.ViewClass;

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
		public AbstractView newView() {
			return new AccountEditView();
		}

	}

	private static final EntityOptions entityOptions = new EntityOptions();

	static {
		entityOptions.requestRelated(IEntityType.PAYMENT_INFO);
		entityOptions.requestRelated(IEntityType.ACCOUNT_ADDRESS);
		entityOptions.requestRelatedRef(IEntityType.ACCOUNT);
	}

	/**
	 * Constructor
	 */
	public AccountEditView() {
		super(new AccountPanel(null), entityOptions);
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
