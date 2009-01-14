package com.tll.client.admin.ui.field.account;

import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.model.impl.AccountStatus;

/**
 * AccountFieldsProvider
 * @author jpk
 */
public class AccountFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	protected String getFieldGroupName() {
		return "Account";
	}

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		fg.addField(fstext("acntParentName", "parent.name", "Parent", "Parent Account", 15));
		fg.addField(fenumselect("acntStatus", "status", "Status", "Status", AccountStatus.class));
		fg.addField(fddate("acntDateCancelled", "dateCancelled", "Date Cancelled", "Date Cancelled"));
		fg.addField(fcurrencies("acntCurrencyId", "currency.id", "Currency", "Currency"));
		fg.addField(fstext("acntBillingModel", "billingModel", "Billing Model", "Billing Model", 18));
		fg.addField(fstext("acntBillingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
		fg.addField(fddate("acntDateLastCharged", "dateLastCharged", "Last Charged", "Last Charged"));
		fg.addField(fddate("acntNextChargeDate", "nextChargeDate", "Next Charge", "Next Charge"));
		fg.addField(fbool("acntPersistPymntInfo", "persistPymntInfo", "PersistPayment Info?", "PersistPayment Info?"));
	}
}