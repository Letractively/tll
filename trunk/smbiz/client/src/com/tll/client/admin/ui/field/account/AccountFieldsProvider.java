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
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		fg.addField(fstext("parentName", "parent.name", "Parent", "Parent Account", 15));
		fg.addField(fenumselect("status", "status", "Status", "Status", AccountStatus.class));
		fg.addField(fddate("dateCancelled", "dateCancelled", "Date Cancelled", "Date Cancelled"));
		fg.addField(fcurrencies("currencyId", "currency.id", "Currency", "Currency"));
		fg.addField(fstext("billingModel", "billingModel", "Billing Model", "Billing Model", 18));
		fg.addField(fstext("billingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
		fg.addField(fddate("dateLastCharged", "dateLastCharged", "Last Charged", "Last Charged"));
		fg.addField(fddate("nextChargeDate", "nextChargeDate", "Next Charge", "Next Charge"));
		fg.addField(fbool("persistPymntInfo", "persistPymntInfo", "PersistPayment Info?", "PersistPayment Info?"));
	}
}