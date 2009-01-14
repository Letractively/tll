/**
 * The Logic Lab
 * @author jpk
 * Dec 31, 2008
 */
package com.tll.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.admin.ui.field.BankFieldsProvider;
import com.tll.client.admin.ui.field.CreditCardFieldsProvider;
import com.tll.client.admin.ui.field.account.AccountFieldsProvider;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.BooleanPropertyValue;
import com.tll.client.model.DatePropertyValue;
import com.tll.client.model.EnumPropertyValue;
import com.tll.client.model.FloatPropertyValue;
import com.tll.client.model.IModelProperty;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.ModelRefProperty;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.model.RelatedOneProperty;
import com.tll.client.model.StringPropertyValue;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.util.ObjectUtil;
import com.tll.model.EntityType;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.AddressType;
import com.tll.model.impl.CreditCardType;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.service.app.RefDataType;

/**
 * ClientTestUtils
 * @author jpk
 */
public final class ClientTestUtils {

	/**
	 * TestFieldGroupProvider
	 * @author jpk
	 */
	private static final class TestFieldGroupProvider implements IFieldGroupProvider {

		public static final TestFieldGroupProvider INSTANCE = new TestFieldGroupProvider();

		/**
		 * Constructor
		 */
		private TestFieldGroupProvider() {
			// set needed aux data cache
			List<Model> list = new ArrayList<Model>();
			list.add(stubCurrency());
			AuxDataCache.instance().cacheEntityList(EntityType.CURRENCY, list);

			Map<String, String> cc = new HashMap<String, String>();
			cc.put("us", "United States");
			cc.put("br", "Brazil");
			AuxDataCache.instance().cacheRefDataMap(RefDataType.ISO_COUNTRY_CODES, cc);

			Map<String, String> st = new HashMap<String, String>();
			st.put("MI", "Michigan");
			st.put("CA", "California");
			AuxDataCache.instance().cacheRefDataMap(RefDataType.US_STATES, st);
		}

		public FieldGroup getFieldGroup() {
			final IFieldGroupProvider fpAccount = new AccountFieldsProvider();
			FieldGroup fg = fpAccount.getFieldGroup();

			fg.addField("parent", fpAccount.getFieldGroup());

			FieldGroup fgPaymentInfo = new FieldGroup("paymentInfo");
			fgPaymentInfo.addField((new CreditCardFieldsProvider()).getFieldGroup());
			fgPaymentInfo.addField((new BankFieldsProvider()).getFieldGroup());

			fg.addField("paymentInfo", fgPaymentInfo);

			fg.addField("addresses", new FieldGroup("addresses"));

			return fg;
		}
	}

	/**
	 * TestFieldPanel
	 * @author jpk
	 */
	public static class TestFieldPanel extends FieldPanel<FlowPanel, Model> {

		FlowPanel panel = new FlowPanel();

		/**
		 * Constructor
		 */
		public TestFieldPanel() {
			super();
			initWidget(panel);
			setRenderer(new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel widget, FieldGroup fg) {
					// no-op
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return TestFieldGroupProvider.INSTANCE.getFieldGroup();
		}

	}

	/**
	 * Validate the given object is empty. Handles {@link Collection}s and arrays.
	 * @param obj
	 * @throws Exception When the given object is found not empty
	 */
	public static void validateEmpty(Object obj) throws Exception {
		if(obj == null) return;
		if(obj instanceof Collection) {
			if(((Collection<?>) obj).size() > 0) {
				throw new Exception("Non-empty collection");
			}
		}
		else if(obj.getClass().isArray()) {
			final int length = ((Object[]) obj).length;
			if(length > 0) {
				throw new Exception("Non-empty array");
			}
		}
	}

	/**
	 * Validate the 2 objects given are equal by class type
	 * @param src
	 * @param tgt
	 * @throws Exception When not of like type
	 */
	public static void validateEqualTypes(Object src, Object tgt) throws Exception {
		if((src == null && tgt != null) || (src != null && tgt == null)) {
			throw new Exception("Types differ: one is null the other is not");
		}
		if(src != null && !tgt.getClass().equals(src.getClass()))
			throw new Exception("Types differ: src type: " + src.getClass().toString() + ", cpyValue: "
					+ tgt.getClass().toString());
	}

	/**
	 * Validate the 2 objects given are equal. When the given objects are
	 * <code>null</code>, this validation passes.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateEquals(Object src, Object tgt) throws Exception {
		if(!ObjectUtil.equals(src, tgt))
			throw new Exception("Objects do not equal: src: " + (src == null ? "null" : src.toString()) + ", tgt: "
					+ (tgt == null ? "null" : tgt.toString()));
	}

	/**
	 * Validate the 2 objects given are NOTE equal by memory address. This
	 * validation passes when the given objects are <code>null</code>.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateNotEqualByMemoryAddress(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt)
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
	}

	/**
	 * Validate the 2 objects given are locically equal AND NOT by memory address.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateEqualsAndNotAddressEquals(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt) {
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
		}
		validateEquals(src, tgt);
	}

	/**
	 * Validates a source {@link Model} to a copied {@link Model} verifying the
	 * copied model's properties.
	 * @param source
	 * @param copy
	 * @param compareReferences Ignore the isReference property for related model
	 *        properties?
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void validateCopy(final Model source, final Model copy, final boolean compareReferences)
			throws Exception {
		compare(source, copy, compareReferences, true, new ArrayList<Model>());
	}

	/**
	 * Checks for {@link Model} instance equality.
	 * @param source
	 * @param target
	 * @param compareReferences Ignore the isReference property for related model
	 *        properties?
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void equals(final Model source, final Model target, final boolean compareReferences) throws Exception {
		compare(source, target, compareReferences, false, new ArrayList<Model>());
	}

	/**
	 * Compares two {@link Model} instances.
	 * <p>
	 * <em><b>IMPT: </b>This method doesn't consider those properties in the target that do <b>not</b> exist on the source.</em>
	 * @param source The source
	 * @param target The target
	 * @param compareReferences Compare nested relational properties marked as
	 *        reference?
	 * @param requireDistinctModelProperties Fail if any encountered
	 *        {@link IModelProperty} on the source is equal by memory address on
	 *        the copy?
	 * @param visited
	 * @throws Exception
	 */
	private static void compare(Model source, Model target, final boolean compareReferences,
			boolean requireDistinctModelProperties, List<Model> visited) throws Exception {
		assert source != null && target != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcProp = itr.next();
			final String propName = srcProp.getPropertyName();
			final IModelProperty tgtProp = target.getModelProperty(propName);

			// verify like types
			ClientTestUtils.validateEqualTypes(srcProp, tgtProp);

			if(requireDistinctModelProperties) {
				// ensure distinct memory address
				ClientTestUtils.validateNotEqualByMemoryAddress(srcProp, tgtProp);
			}

			final PropertyType pvType = srcProp.getType();
			if(pvType.isValue()) {
				// require logical equals
				final Object srcValue = srcProp.getValue();
				final Object tgtValue = tgtProp.getValue();
				ClientTestUtils.validateEquals(srcValue, tgtValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final ModelRefProperty src = (ModelRefProperty) srcProp;
				final ModelRefProperty tgt = (ModelRefProperty) tgtProp;
				final Model srcModel = src.getModel();
				final Model tgtModel = tgt.getModel();
				if(srcModel == null && tgtModel != null) {
					throw new Exception("Nested source model null but not on the target for prop: " + propName);
				}
				else if(tgtModel == null && srcModel != null) {
					throw new Exception("Nested source model non-null but not on the target for prop: " + propName);
				}
				else if((compareReferences || !src.isReference()) && srcModel != null && !visited.contains(srcModel)) {
					visited.add(srcModel);
					compare(srcModel, tgtModel, compareReferences, requireDistinctModelProperties, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty src = (RelatedManyProperty) srcProp;
				final RelatedManyProperty tgt = (RelatedManyProperty) tgtProp;
				final Collection<Model> srcSet = src.getList();
				final Collection<Model> tgtSet = tgt.getList();

				if((srcSet == null && tgtSet != null) || (srcSet != null && tgtSet == null)) {
					throw new Exception("Related many lists differ by null for prop:" + propName);
				}
				if(srcSet != null) {
					if(srcSet.size() != tgtSet.size()) {
						throw new Exception("Source and copy related many list sizes differ for prop: " + propName);
					}
					if(compareReferences || !src.isReference()) {
						final Iterator<Model> citr = tgtSet.iterator();
						for(final Model srcPvg : srcSet) {
							final Model cpyPvg = citr.next();
							if(!visited.contains(srcPvg)) {
								visited.add(srcPvg);
								compare(srcPvg, cpyPvg, compareReferences, requireDistinctModelProperties, visited);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Stubs a suitable {@link Model} instance for testing.
	 * <p>
	 * Of note, this model has a related one model ("parent") and two indexable
	 * models ("addresses").
	 * @return A stubbed root model for testing.
	 */
	public static Model getTestRootModel() {
		Model account = stubAccount(stubAccount(null, EntityType.ASP, 1), EntityType.ISP, 2);

		Model aa1 = stubAccountAddress(account, stubAddress(1), 1);
		Model aa2 = stubAccountAddress(account, stubAddress(2), 2);

		Set<Model> addresses = new LinkedHashSet<Model>();
		addresses.add(aa1);
		addresses.add(aa2);
		account.set(new RelatedManyProperty(EntityType.ACCOUNT_ADDRESS, "addresses", false, addresses));

		return account;
	}

	/**
	 * Stubs a currency model
	 * @return new instance
	 */
	public static Model stubCurrency() {
		Model m = new Model(EntityType.CURRENCY);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.INT, false, true, -1), 1));
		m.set(new StringPropertyValue("iso4217", new PropertyMetadata(PropertyType.STRING, false, true, 8), "usd"));
		m.set(new StringPropertyValue("symbol", new PropertyMetadata(PropertyType.STRING, false, true, 8), "$"));
		m.set(new FloatPropertyValue("usdExchangeRage", new PropertyMetadata(PropertyType.FLOAT, false, true, -1), 1f));
		return m;
	}

	/**
	 * Stubs an address
	 * @param num
	 * @return new instance
	 */
	public static Model stubAddress(int num) {
		Model address = new Model(EntityType.ADDRESS);
		address.set(new IntPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.INT, false, true, -1), num));
		address.set(new StringPropertyValue("emailAddress", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"email" + num + "@domain.com"));
		address.set(new StringPropertyValue("firstName", new PropertyMetadata(PropertyType.STRING, false, false, 32),
				"firstname " + num));
		address.set(new StringPropertyValue("lastName", new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"lastname " + num));
		address.set(new StringPropertyValue("mi", new PropertyMetadata(PropertyType.CHAR, false, false, 1), "m"));
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
		return address;
	}

	/**
	 * Stubs payment info
	 * @return new Model representing payment info
	 */
	public static Model stubPaymentInfo() {
		Model m = new Model(EntityType.PAYMENT_INFO);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.INT, false, true, -1), 1));
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

	/**
	 * Stubs an account instance
	 * @param parentAccount
	 * @param accountType
	 * @param num
	 * @return new instance
	 */
	public static Model stubAccount(Model parentAccount, EntityType accountType, int num) {
		Model m = new Model(accountType);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, new PropertyMetadata(PropertyType.INT, false, true, -1), num));
		m.set(new StringPropertyValue(Model.NAME_PROPERTY, new PropertyMetadata(PropertyType.STRING, false, true, 32),
				"ISP " + num));
		m.set(new DatePropertyValue(Model.DATE_CREATED_PROPERTY, new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
		m.set(new DatePropertyValue(Model.DATE_MODIFIED_PROPERTY, new PropertyMetadata(PropertyType.DATE, false, true, 32),
				new Date()));
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
		m.set(new RelatedOneProperty(EntityType.CURRENCY, "currency", true, stubCurrency()));
		m.set(new RelatedOneProperty(EntityType.PAYMENT_INFO, "paymentInfo", false, stubPaymentInfo()));
		m.set(new RelatedOneProperty(EntityType.ACCOUNT, "parent", true, parentAccount));
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
		Model m = new Model(EntityType.ACCOUNT_ADDRESS);
		m.set(new IntPropertyValue(Model.ID_PROPERTY, num));
		m.set(new EnumPropertyValue("type", new PropertyMetadata(PropertyType.ENUM, false, true, 8),
				AddressType.values()[num - 1]));
		m.set(new RelatedOneProperty(EntityType.ACCOUNT, "account", true, account));
		m.set(new RelatedOneProperty(EntityType.ADDRESS, "address", false, address));
		return m;
	}

	/**
	 * @return A test {@link IFieldGroupProvider} that compliments
	 *         {@link #getTestRootModel()}.
	 */
	public static IFieldGroupProvider getRootFieldGroupProvider() {
		return TestFieldGroupProvider.INSTANCE;
	}

	/**
	 * Constructor
	 */
	private ClientTestUtils() {
		super();
	}

}
