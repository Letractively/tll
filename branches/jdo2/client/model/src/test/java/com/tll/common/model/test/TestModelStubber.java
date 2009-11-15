/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.common.model.test;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.tll.common.model.BooleanPropertyValue;
import com.tll.common.model.CharacterPropertyValue;
import com.tll.common.model.DatePropertyValue;
import com.tll.common.model.DoublePropertyValue;
import com.tll.common.model.EnumPropertyValue;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedManyProperty;
import com.tll.common.model.RelatedOneProperty;
import com.tll.common.model.StringPropertyValue;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.model.test.AccountStatus;
import com.tll.model.test.AddressType;
import com.tll.model.test.CreditCardType;

/**
 * TestModelFactory - Ad-hoc factory for generating testable {@link Model}
 * instances w/o the need for db access.
 * @author jpk
 */
public class TestModelStubber {

	/**
	 * Global static for ensuring all ids are unique for proper equals/hashcode
	 * behavior down the line.
	 */
	private static int nextUniqueId = 0;

	/**
	 * Stubs an empty model but with with core entity properties.
	 * @param type the model entity type
	 * @param version the desired entity version which may be <code>null</code>
	 * @param timestamping add the standard entity timestamping properties?
	 * @param name optional name property value. If non-<code>null</code>, a
	 *        standard entity name property will be added.
	 * @return the stubbed model
	 */
	public static Model stubModel(TestEntityType type, Integer version, boolean timestamping, String name) {
		final Model m = new Model(type);
		m.set(new StringPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.STRING, false, true, 10),
				Integer.toString(++nextUniqueId)));
		m.set(new IntPropertyValue(Model.VERSION_PROPERTY, version));
		if(name != null) {
			m.set(new StringPropertyValue(Model.NAME_PROPERTY, new PropertyMetadata(PropertyType.STRING, false, true, 32),
					name));
		}
		m.set(new DatePropertyValue(Model.DATE_CREATED_PROPERTY, new PropertyMetadata(PropertyType.DATE, true, true, 32),
				new Date()));
		m.set(new DatePropertyValue(Model.DATE_MODIFIED_PROPERTY, new PropertyMetadata(PropertyType.DATE, true, true, 32),
				new Date()));
		return m;
	}

	/**
	 * Stubs a suitable {@link Model} instance for testing.
	 * <p>
	 * Of note, this model has a related one model ("parent") and two indexable
	 * models ("addresses").
	 * @param addAddresses Add related many addresses?
	 * @return A stubbed root model for testing.
	 */
	public static Model stubAccount(boolean addAddresses) {
		final Model account = stubAccount(stubAccount(null, TestEntityType.ACCOUNT, 1), TestEntityType.ACCOUNT, 2);

		final Model aa1 = stubAccountAddress(account, stubAddress(1), 1);
		final Model aa2 = stubAccountAddress(account, stubAddress(2), 2);

		final Set<Model> addresses = addAddresses ? new LinkedHashSet<Model>() : null;
		if(addresses != null) {
			addresses.add(aa1);
			addresses.add(aa2);
		}
		account.set(new RelatedManyProperty(TestEntityType.ACCOUNT_ADDRESS, "addresses", false, addresses));

		return account;
	}

	/**
	 * Stubs an account instance
	 * @param parentAccount
	 * @param accountType
	 * @param num
	 * @return new instance
	 */
	private static Model stubAccount(Model parentAccount, TestEntityType accountType, int num) {
		final Model m = stubModel(accountType, null, true, accountType.descriptor() + " " + num);
		m
		.set(new EnumPropertyValue("status", new PropertyMetadata(PropertyType.ENUM, false, true, 16),
				AccountStatus.OPEN));
		m.set(new BooleanPropertyValue("persistPymntInfo", new PropertyMetadata(PropertyType.BOOL, false, true, -1),
				Boolean.TRUE));
		m.set(new StringPropertyValue("billingModel", new PropertyMetadata(PropertyType.STRING, false, true, 32),
		"a billing model"));
		m.set(new StringPropertyValue("billingCycle", new PropertyMetadata(PropertyType.STRING, false, true, 32),
		"a billing cycle"));
		m
		.set(new DatePropertyValue("dateLastCharged", new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
		m
		.set(new DatePropertyValue("nextChargeDate", new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
		m.set(new DatePropertyValue("dateCancelled", new PropertyMetadata(PropertyType.DATE, false, true, 32), new Date()));
		m.set(new RelatedOneProperty(TestEntityType.CURRENCY, stubCurrency(), "currency", true));
		m.set(new RelatedOneProperty(TestEntityType.PAYMENT_INFO, stubPaymentInfo(), "paymentInfo", false));
		m.set(new RelatedOneProperty(TestEntityType.ACCOUNT, parentAccount, "parent", true));
		return m;
	}

	/**
	 * Stubs an account address.
	 * @param account
	 * @param address
	 * @param num
	 * @return new instance
	 */
	public static Model stubAccountAddress(Model account, Model address, int num) {
		final Model m = stubModel(TestEntityType.ACCOUNT_ADDRESS, null, true, ("Adrs " + num));
		m.set(new EnumPropertyValue("type", new PropertyMetadata(PropertyType.ENUM, false, true, 8),
				AddressType.values()[num - 1]));
		m.set(new RelatedOneProperty(TestEntityType.ACCOUNT, account, "account", true));
		m.set(new RelatedOneProperty(TestEntityType.ADDRESS, address, "address", false));
		return m;
	}

	/**
	 * Stubs an address
	 * @param num
	 * @return new instance
	 */
	public static Model stubAddress(int num) {
		final Model address = stubModel(TestEntityType.ADDRESS, null, false, null);
		address.set(new StringPropertyValue("emailAddress", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"email" + num + "@domain.com"));
		address.set(new StringPropertyValue("firstName", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"firstname " + num));
		address.set(new StringPropertyValue("lastName", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"lastname " + num));
		address.set(new CharacterPropertyValue("mi", new PropertyMetadata(PropertyType.CHAR, false, false, 1), Character.valueOf('m')));
		address.set(new StringPropertyValue("address1", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"address1 " + num));
		address.set(new StringPropertyValue("address2", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"address2 " + num));
		address.set(new StringPropertyValue("city", new PropertyMetadata(PropertyType.STRING, false, true, 32), "city "
				+ num));
		address.set(new StringPropertyValue("province", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"province " + num));
		address.set(new StringPropertyValue("postalCode", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"zip " + num));
		address.set(new StringPropertyValue("country", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"country " + num));

		// test boolean prop types..
		address.set(new BooleanPropertyValue("boolean", new PropertyMetadata(PropertyType.BOOL, false, true, -1),
				Boolean.TRUE));

		// test float prop types..
		address.set(new DoublePropertyValue("float", new PropertyMetadata(PropertyType.FLOAT, false, true, 8), Double.valueOf(33.33d)));

		// test double prop types..
		address.set(new DoublePropertyValue("double", new PropertyMetadata(PropertyType.DOUBLE, false, true, 8), Double.valueOf(44.44d)));

		return address;
	}

	/**
	 * Stubs a currency model
	 * @return new instance
	 */
	public static Model stubCurrency() {
		final Model m = stubModel(TestEntityType.CURRENCY, null, false, null);
		m.set(new StringPropertyValue("iso4217", new PropertyMetadata(PropertyType.STRING, false, true, 8), "usd"));
		m.set(new StringPropertyValue("symbol", new PropertyMetadata(PropertyType.STRING, false, true, 8), "$"));
		m.set(new DoublePropertyValue("usdExchangeRage", new PropertyMetadata(PropertyType.DOUBLE, false, true, -1), Double.valueOf(1d)));
		return m;
	}

	/**
	 * Stubs payment info
	 * @return new Model representing payment info
	 */
	public static Model stubPaymentInfo() {
		final Model m = stubModel(TestEntityType.PAYMENT_INFO, null, false, null);
		m.set(new StringPropertyValue("paymentData_bankAccountNo", new PropertyMetadata(PropertyType.STRING, false, false,
				16), "0005543"));
		m.set(new StringPropertyValue("paymentData_bankName", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"bank name"));
		m.set(new StringPropertyValue("paymentData_bankRoutingNo", new PropertyMetadata(PropertyType.STRING, false, false,
				16), "77777"));
		m.set(new EnumPropertyValue("paymentData_ccType", new PropertyMetadata(PropertyType.ENUM, false, false, 16),
				CreditCardType.VISA));
		m.set(new StringPropertyValue("paymentData_ccNum", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"4111111111111111"));
		m.set(new StringPropertyValue("paymentData_ccCvv2", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"834"));
		m.set(new IntPropertyValue("paymentData_ccExpMonth", new PropertyMetadata(PropertyType.INT, false, false, 16), Integer.valueOf(8)));
		m
		.set(new IntPropertyValue("paymentData_ccExpYear", new PropertyMetadata(PropertyType.INT, false, false, 16),
				Integer.valueOf(2012)));
		m.set(new StringPropertyValue("paymentData_ccName", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"cc name"));
		m.set(new StringPropertyValue("paymentData_ccAddress1",
				new PropertyMetadata(PropertyType.STRING, false, false, 16), "88 Broadway"));
		m.set(new StringPropertyValue("paymentData_ccAddress2",
				new PropertyMetadata(PropertyType.STRING, false, false, 16), "#32"));
		m.set(new StringPropertyValue("paymentData_ccCity", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"Sacramento"));
		m.set(new StringPropertyValue("paymentData_ccState", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"CA"));
		m.set(new StringPropertyValue("paymentData_ccZip", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"99885"));
		m.set(new StringPropertyValue("paymentData_ccCountry", new PropertyMetadata(PropertyType.STRING, false, false, 16),
		"us"));

		return m;
	}
}
