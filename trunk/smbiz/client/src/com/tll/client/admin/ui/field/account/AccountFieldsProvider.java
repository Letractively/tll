package com.tll.client.admin.ui.field.account;

import java.util.Arrays;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.util.GlobalFormat;
import com.tll.model.impl.AccountStatus;

/**
 * AccountFieldsProvider
 * @author jpk
 */
public class AccountFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		fg.addField(FieldFactory.ftext("parentName", "parent.name", "Parent", "Parent Account", 15));
		fg.addField(FieldFactory.fselect("status", "status", "Status", "Status", false, Arrays.asList(AccountStatus
				.values())));
		fg.addField(FieldFactory.fdate("dateCancelled", "dateCancelled", "Date Cancelled", "Date Cancelled",
				GlobalFormat.DATE));
		fg.addField(FieldFactory.fselect("currencyId", "currency.id", "Currency", "Currency", false, AuxDataCache
				.instance().getCurrencyDataMap().values()));
		fg.addField(FieldFactory.ftext("billingModel", "billingModel", "Billing Model", "Billing Model", 18));
		fg.addField(FieldFactory.ftext("billingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
		fg.addField(FieldFactory.fdate("dateLastCharged", "dateLastCharged", "Last Charged", "Last Charged",
				GlobalFormat.DATE));
		fg
				.addField(FieldFactory.fdate("nextChargeDate", "nextChargeDate", "Next Charge", "Next Charge",
						GlobalFormat.DATE));
		fg.addField(FieldFactory.fcheckbox("persistPymntInfo", "persistPymntInfo", "PersistPayment Info?",
				"PersistPayment Info?"));
	}
}