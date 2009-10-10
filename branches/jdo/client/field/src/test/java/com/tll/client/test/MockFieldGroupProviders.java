/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.client.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.GridRenderer;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.RadioGroupField.GridStyles;
import com.tll.common.model.Model;
import com.tll.common.model.test.AccountStatus;
import com.tll.common.model.test.AddressType;
import com.tll.common.model.test.CreditCardType;
import com.tll.common.model.test.MockEntityType;
import com.tll.common.model.test.MockModelStubber;
import com.tll.refdata.RefDataType;

/**
 * MockFieldGroupProviders
 * @author jpk
 */
public class MockFieldGroupProviders {

	/**
	 * AbstractMockFieldGroupProvider
	 * @author jpk
	 */
	static abstract class AbstractMockFieldGroupProvider extends AbstractFieldGroupProvider {

		private static boolean auxDataInitialized;

		/**
		 * Constructor
		 */
		protected AbstractMockFieldGroupProvider() {
			super();
			if(!auxDataInitialized) {
				// set needed aux data cache
				final List<Model> list = new ArrayList<Model>();
				list.add(MockModelStubber.stubCurrency());
				AuxDataCache.get().cacheEntityList(MockEntityType.CURRENCY, list);

				final Map<String, String> cc = new HashMap<String, String>();
				cc.put("us", "United States");
				cc.put("br", "Brazil");
				AuxDataCache.get().cacheRefDataMap(RefDataType.ISO_COUNTRY_CODES, cc);

				final Map<String, String> st = new HashMap<String, String>();
				st.put("MI", "Michigan");
				st.put("CA", "California");
				AuxDataCache.get().cacheRefDataMap(RefDataType.US_STATES, st);

				auxDataInitialized = true;
			}
		}
	} // AbstractMockFieldGroupProvider

	/**
	 * AddressFieldsProvider
	 * @author jpk
	 */
	public static final class AddressFieldsProvider extends AbstractMockFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Address";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			fg.addField(femail("adrsEmailAddress", "emailAddress", "Email Address", "Email Address", 30));
			fg.addField(ftext("adrsFirstName", "firstName", "First Name", "First Name", 20));
			fg.addField(ftext("adrsLastName", "lastName", "Last Name", "Last Name", 20));
			fg.addField(ftext("adrsMi", "mi", "MI", "Middle Initial", 1));
			//fg.addField(fstext("adrsCompany", "company", "Company", "Company", 20));
			//fg.addField(fstext("adrsAttn", "attn", "Attn", "Attention", 10));
			//fg.addField(fstext("adrsAddress1", "address1", "Address 1", "Address 1", 40));
			//fg.addField(fstext("adrsAddress2", "address2", "Address 2", "Address 2", 40));
			fg.addField(ftext("adrsCity", "city", "City", "City", 30));
			fg.addField(frefdata("adrsProvince", "province", "State/Province", "State/Province", RefDataType.US_STATES));
			//fg.addField(fstext("adrsPostalCode", "postalCode", "Zip", "Zip", 20));
			fg.addField(frefdata("adrsCountry", "country", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));

			// ad hoc props to verify types
			fg.addField(fcheckbox("adrsBoolean", "boolean", "Boolean", "Boolean"));
			fg.addField(ftext("adrsFloat", "float", "Float", "Float", 5));
			fg.addField(ftext("adrsDouble", "double", "Double", "Double", 5));
		}

	}

	/**
	 * PaymentInfoFieldsProvider
	 * @author jpk
	 */
	public static class PaymentInfoFieldsProvider extends AbstractMockFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Payment Info";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			fg.addField(fenumradio("ccType", "paymentData_ccType", "Type", "Type", CreditCardType.class, new GridRenderer(-1,
					GridStyles.GRID)));
			fg.addField(fcreditcard("ccNum", "paymentData_ccNum", "Num", null, 15));
			fg.addField(ftext("ccCvv2", "paymentData_ccCvv2", "CVV2", "CVV2", 4));
			fg.addField(ftext("ccExpMonth", "paymentData_ccExpMonth", "Exp Month", "Expiration Month", 2));
			fg.addField(ftext("ccExpYear", "paymentData_ccExpYear", "Exp Year", "Expiration Year", 4));
			fg.addField(ftext("ccName", "paymentData_ccName", "Name", "Name", 30));
			fg.addField(ftext("ccAddress1", "paymentData_ccAddress1", "Address 1", "Address 1", 40));
			fg.addField(ftext("ccAddress2", "paymentData_ccAddress2", "Address 2", "Address 2", 40));
			fg.addField(ftext("ccCity", "paymentData_ccCity", "City", "City", 30));
			fg.addField(frefdata("ccState", "paymentData_ccState", "State/Province", "State", RefDataType.US_STATES));
			fg.addField(ftext("ccZip", "paymentData_ccZip", "Postal Code", "Postal Code", 15));
			fg.addField(frefdata("ccCountry", "paymentData_ccCountry", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));
		}

	}

	/**
	 * AccountAddressFieldsProvider
	 * @author jpk
	 */
	public static class AccountAddressFieldsProvider extends AbstractMockFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Account Address";
		}

		@Override
		protected void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, true, "aa");
			fg.addField(fenumselect("type", "type", "Type", "Account Address Type", AddressType.class));
			final FieldGroup fgAddress = (new AddressFieldsProvider()).getFieldGroup();
			fgAddress.setName("address");
			fg.addField("address", fgAddress);
		}
	}

	/**
	 * AccountFieldsProvider - Provides non-relational account properties.
	 * @author jpk
	 */
	public static class AccountFieldsProvider extends AbstractMockFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Account";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, true, "acnt");
			fg.addField(ftext("acntParentName", "parent.name", "Parent", "Parent Account", 15));
			fg.addField(fenumselect("acntStatus", "status", "Status", "Status", AccountStatus.class));
			fg.addField(fdate("acntDateCancelled", "dateCancelled", "Date Cancelled", "Date Cancelled"));
			//fg.addField(fcurrencies("acntCurrencyId", "currency.id", "Currency", "Currency"));
			fg.addField(ftext("acntBillingModel", "billingModel", "Billing Model", "Billing Model", 18));
			fg.addField(ftext("acntBillingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
			fg.addField(fdate("acntDateLastCharged", "dateLastCharged", "Last Charged", "Last Charged"));
			fg.addField(fdate("acntNextChargeDate", "nextChargeDate", "Next Charge", "Next Charge"));
			fg
			.addField(fcheckbox("acntPersistPymntInfo", "persistPymntInfo", "PersistPayment Info?",
			"PersistPayment Info?"));
		}
	}
}