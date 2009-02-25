/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.common.model.mock;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.tll.common.model.BooleanPropertyValue;
import com.tll.common.model.CharacterPropertyValue;
import com.tll.common.model.DatePropertyValue;
import com.tll.common.model.EnumPropertyValue;
import com.tll.common.model.FloatPropertyValue;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedManyProperty;
import com.tll.common.model.RelatedOneProperty;
import com.tll.common.model.StringPropertyValue;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;


/**
 * TestModelFactory - Ad-hoc factory for generating testable {@link Model} instances.
 * @author jpk
 */
public class MockModelStubber {

	/**
	 * ModelType - The test model types.
	 * @author jpk
	 */
	public static enum ModelType {
		/**
		 * Simple model having non-relational properties only.
		 */
		SIMPLE,
		/**
		 * Model having a related many property, a related one property and one or
		 * more non-relational properties including one "nested" related one
		 * property.
		 */
		COMPLEX;
	}

	/**
	 * Creates a {@link Model} instance of the given mock type.
	 * @param modelType the desired model complexity
	 * @return newly created instance
	 */
	public static Model create(ModelType modelType) {
		switch(modelType) {
			case SIMPLE:
				return stubAddress(1);
			case COMPLEX:
				return stubAccount();
			default:
				throw new UnsupportedOperationException("Unhandled model type");
		}
	}

	/**
	 * Stubs a model of the given type having the given id.
	 * @param type the model entity type
	 * @param id the id to ascribe
	 * @param name optional name property value. If non-<code>null</code>, a
	 *        standard entity name property will be added.
	 * @param name add the standard entity timestamping properties?
	 * @return the stubbed model
	 */
	private static Model stubModel(MockEntityType type, int id, String name, boolean timestamping) {
		final Model m = new Model(type);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.INT, false, true, -1), id));
		if(name != null) {
			m.set(new StringPropertyValue(Model.NAME_PROPERTY, new PropertyMetadata(PropertyType.STRING, false, true, 32),
					name));
		}
		m.set(new DatePropertyValue(Model.DATE_CREATED_PROPERTY, new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
		m.set(new DatePropertyValue(Model.DATE_MODIFIED_PROPERTY, new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
		return m;
	}

	/**
	 * Stubs a suitable {@link Model} instance for testing.
	 * <p>
	 * Of note, this model has a related one model ("parent") and two indexable
	 * models ("addresses").
	 * @return A stubbed root model for testing.
	 */
	public static Model stubAccount() {
		final Model account = stubAccount(stubAccount(null, MockEntityType.ACCOUNT, 1), MockEntityType.ACCOUNT, 2);

		final Model aa1 = stubAccountAddress(account, stubAddress(1), 1);
		final Model aa2 = stubAccountAddress(account, stubAddress(2), 2);

		final Set<Model> addresses = new LinkedHashSet<Model>();
		addresses.add(aa1);
		addresses.add(aa2);
		account.set(new RelatedManyProperty(MockEntityType.ACCOUNT_ADDRESS, "addresses", false, addresses));

		return account;
	}

	/**
	 * Stubs an account instance
	 * @param parentAccount
	 * @param accountType
	 * @param num
	 * @return new instance
	 */
	public static Model stubAccount(Model parentAccount, MockEntityType accountType, int num) {
		final Model m = stubModel(accountType, num, "ISP " + num, true);
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
		m.set(new RelatedOneProperty(MockEntityType.CURRENCY, "currency", true, stubCurrency()));
		m.set(new RelatedOneProperty(MockEntityType.PAYMENT_INFO, "paymentInfo", false, stubPaymentInfo()));
		m.set(new RelatedOneProperty(MockEntityType.ACCOUNT, "parent", true, parentAccount));
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
		final Model m = stubModel(MockEntityType.ACCOUNT_ADDRESS, num, ("Adrs " + num), true);
		m.set(new EnumPropertyValue("type", new PropertyMetadata(PropertyType.ENUM, false, true, 8),
				AddressType.values()[num - 1]));
		m.set(new RelatedOneProperty(MockEntityType.ACCOUNT, "account", true, account));
		m.set(new RelatedOneProperty(MockEntityType.ADDRESS, "address", false, address));
		return m;
	}

	/**
	 * Stubs an address
	 * @param num
	 * @return new instance
	 */
	public static Model stubAddress(int num) {
		final Model address = stubModel(MockEntityType.ADDRESS, num, null, false);
		address.set(new StringPropertyValue("emailAddress", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"email" + num + "@domain.com"));
		address.set(new StringPropertyValue("firstName", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"firstname " + num));
		address.set(new StringPropertyValue("lastName", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"lastname " + num));
		address.set(new CharacterPropertyValue("mi", new PropertyMetadata(PropertyType.CHAR, false, false, 1), 'm'));
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
		address.set(new FloatPropertyValue("float", new PropertyMetadata(PropertyType.FLOAT, false, true, 8), 33.33f));
		
		return address;
	}

	/**
	 * Stubs a currency model
	 * @return new instance
	 */
	public static Model stubCurrency() {
		final Model m = stubModel(MockEntityType.CURRENCY, 1, null, false);
		m.set(new StringPropertyValue("iso4217", new PropertyMetadata(PropertyType.STRING, false, true, 8), "usd"));
		m.set(new StringPropertyValue("symbol", new PropertyMetadata(PropertyType.STRING, false, true, 8), "$"));
		m.set(new FloatPropertyValue("usdExchangeRage", new PropertyMetadata(PropertyType.FLOAT, false, true, -1), 1f));
		return m;
	}

	/**
	 * Stubs payment info
	 * @return new Model representing payment info
	 */
	public static Model stubPaymentInfo() {
		final Model m = stubModel(MockEntityType.PAYMENT_INFO, 1, null, false);
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
		m.set(new IntPropertyValue("paymentData_ccExpMonth", new PropertyMetadata(PropertyType.INT, false, false, 16), 8));
		m
				.set(new IntPropertyValue("paymentData_ccExpYear", new PropertyMetadata(PropertyType.INT, false, false, 16),
						2012));
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
