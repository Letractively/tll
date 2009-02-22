/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.client.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.IFormattedConverter;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.mock.AccountStatus;
import com.tll.common.model.mock.CreditCardType;
import com.tll.common.model.mock.MockEntityType;
import com.tll.common.model.mock.MockModelStubber;
import com.tll.refdata.RefDataType;


/**
 * MockFieldGroupProviders
 * @author jpk
 */
public class MockFieldGroupProviders {

	/**
	 * AccountFieldsProvider
	 * @author jpk
	 */
	static class AccountFieldsProvider extends AbstractFieldGroupProvider {

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
			//fg.addField(fcurrencies("acntCurrencyId", "currency.id", "Currency", "Currency"));
			fg.addField(fstext("acntBillingModel", "billingModel", "Billing Model", "Billing Model", 18));
			fg.addField(fstext("acntBillingCycle", "billingCycle", "Billing Cycle", "Billing Cycle", 18));
			fg.addField(fddate("acntDateLastCharged", "dateLastCharged", "Last Charged", "Last Charged"));
			fg.addField(fddate("acntNextChargeDate", "nextChargeDate", "Next Charge", "Next Charge"));
			fg.addField(fbool("acntPersistPymntInfo", "persistPymntInfo", "PersistPayment Info?", "PersistPayment Info?"));
		}
	}

	/**
	 * CreditCardFieldsProvider
	 * @author jpk
	 */
	static class CreditCardFieldsProvider extends AbstractFieldGroupProvider {

		private final IFormattedConverter<String, Integer> noFormatIntToStringConverter =
				new IFormattedConverter<String, Integer>() {

					public String convert(Integer o) throws IllegalArgumentException {
						return o == null ? "" : o.toString();
					}

					public GlobalFormat getFormat() {
						return null;
					}
				};

		@Override
		protected String getFieldGroupName() {
			return "Credit Card";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			fg.addField(fenumselect("ccType", "paymentData_ccType", "Type", "Type", CreditCardType.class));
			fg.addField(fcreditcard("ccNum", "paymentData_ccNum", "Num", null, 15));
			fg.addField(fstext("ccCvv2", "paymentData_ccCvv2", "CVV2", "CVV2", 4));
			fg.addField(FieldFactory.ftext("ccExpMonth", "paymentData_ccExpMonth", "Exp Month", "Expiration Month", 2,
					noFormatIntToStringConverter));
			fg.addField(FieldFactory.ftext("ccExpYear", "paymentData_ccExpYear", "Exp Year", "Expiration Year", 4,
					noFormatIntToStringConverter));
			fg.addField(fstext("ccName", "paymentData_ccName", "Name", "Name", 30));
			fg.addField(fstext("ccAddress1", "paymentData_ccAddress1", "Address 1", "Address 1", 40));
			fg.addField(fstext("ccAddress2", "paymentData_ccAddress2", "Address 2", "Address 2", 40));
			fg.addField(fstext("ccCity", "paymentData_ccCity", "City", "City", 30));
			fg.addField(frefdata("ccState", "paymentData_ccState", "State/Province", "State", RefDataType.US_STATES));
			fg.addField(fstext("ccZip", "paymentData_ccZip", "Postal Code", "Postal Code", 15));
			fg.addField(frefdata("ccCountry", "paymentData_ccCountry", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));
		}
	}

	/**
	 * BankFieldsProvider
	 * @author jpk
	 */
	static class BankFieldsProvider extends AbstractFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Bank Info";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			fg.addField(fstext("bankName", "paymentData_bankName", "Bank Name", "Bank Name", 40));
			fg.addField(fstext("bankAccountNo", "paymentData_bankAccountNo", "Account Num", "Account Num", 30));
			fg.addField(fstext("bankRoutingNo", "paymentData_bankRoutingNo", "Routing Num", "Routing Num", 20));
		}

	}

	/**
	 * MockFieldGroupProvider
	 * @author jpk
	 */
	// TODO delete and mockify the other existing providers
	static final class MockFieldGroupProvider implements IFieldGroupProvider {

		public static final MockFieldGroupProvider INSTANCE = new MockFieldGroupProvider();

		/**
		 * Constructor
		 */
		private MockFieldGroupProvider() {
			// set needed aux data cache
			final List<Model> list = new ArrayList<Model>();
			list.add(MockModelStubber.stubCurrency());
			AuxDataCache.instance().cacheEntityList(MockEntityType.CURRENCY, list);

			final Map<String, String> cc = new HashMap<String, String>();
			cc.put("us", "United States");
			cc.put("br", "Brazil");
			AuxDataCache.instance().cacheRefDataMap(RefDataType.ISO_COUNTRY_CODES, cc);

			final Map<String, String> st = new HashMap<String, String>();
			st.put("MI", "Michigan");
			st.put("CA", "California");
			AuxDataCache.instance().cacheRefDataMap(RefDataType.US_STATES, st);
		}

		public FieldGroup getFieldGroup() {
			final IFieldGroupProvider fpAccount = new AccountFieldsProvider();
			final FieldGroup fg = fpAccount.getFieldGroup();

			fg.addField("parent", fpAccount.getFieldGroup());

			final FieldGroup fgPaymentInfo = new FieldGroup("paymentInfo");
			fgPaymentInfo.addField((new CreditCardFieldsProvider()).getFieldGroup());
			fgPaymentInfo.addField((new BankFieldsProvider()).getFieldGroup());

			fg.addField("paymentInfo", fgPaymentInfo);

			fg.addField("addresses", new FieldGroup("addresses"));

			return fg;
		}
	}

	/**
	 * @return A test {@link IFieldGroupProvider}.
	 */
	public static IFieldGroupProvider getRootFieldGroupProvider() {
		return MockFieldGroupProvider.INSTANCE;
	}
}
