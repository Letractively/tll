package com.tll.client.ui.field.account;

import com.tll.client.ui.field.CurrencyAwareFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.model.AccountStatus;

/**
 * AccountFieldsProvider
 * @author jpk
 */
public class AccountFieldsProvider extends CurrencyAwareFieldGroupProvider {

	@Override
	protected String getFieldGroupName() {
		return "Account";
	}

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		fg.addField(FieldFactory.ftext("acntParentName", "parent.name", "Parent", "Parent Account", 15));
		fg.addField(fenumselect("acntStatus", "status", "Status", "Status", AccountStatus.class));
		fg.addField(FieldFactory.fdate("acntDateCancelled", "dateCancelled", "Date Cancelled", "Date Cancelled"));
		fg.addField(fcurrencies("acntCurrencyId", "currency.id", "Currency", "Currency"));
		fg.addField(FieldFactory.ftext("acntBillingModel", "billingModel", "Billing Model", "Billing Model", 18));
		fg.addField(FieldFactory.ftext("acntBillingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
		fg.addField(FieldFactory.fdate("acntDateLastCharged", "dateLastCharged", "Last Charged", "Last Charged"));
		fg.addField(FieldFactory.fdate("acntNextChargeDate", "nextChargeDate", "Next Charge", "Next Charge"));
		fg.addField(FieldFactory.fcheckbox("acntPersistPymntInfo", "persistPymntInfo", "PersistPayment Info?",
				"PersistPayment Info?"));
	}
}