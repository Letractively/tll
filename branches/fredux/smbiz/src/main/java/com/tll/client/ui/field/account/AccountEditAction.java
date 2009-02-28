package com.tll.client.ui.field.account;

import com.tll.client.bind.AbstractModelFieldBinding;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.bind.IBindable;
import com.tll.common.model.PropertyPathException;

/**
 * AccountEditAction
 * @param <M> the model type
 * @author jpk
 */
public class AccountEditAction<M extends IBindable> extends AbstractModelFieldBinding {

	private final AccountPanel<M> accountPanel = new AccountPanel<M>();

	@Override
	public FieldPanel<?> getRootFieldPanel() {
		return accountPanel;
	}

	@Override
	protected void populateBinding() throws PropertyPathException {
		addCommonModelFieldBindings(true, true);
		addFieldBinding("parent.name");
		addFieldBinding("status");
		addFieldBinding("dateCancelled");
		addFieldBinding("currency.id");
		addFieldBinding("billingModel");
		addFieldBinding("billingCycle");
		addFieldBinding("dateLastCharged");
		addFieldBinding("nextChargeDate");
		addFieldBinding("persistPymntInfo");

		addNestedFieldBindings("paymentInfo");

		addIndexedFieldBinding("addresses", accountPanel.addressesPanel);
	}
}