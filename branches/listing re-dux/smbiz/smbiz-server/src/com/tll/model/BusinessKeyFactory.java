/**
 * The Logic Lab
 * @author jpk
 * Jun 26, 2008
 */
package com.tll.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;

import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.Address;
import com.tll.model.impl.AppProperty;
import com.tll.model.impl.Authority;
import com.tll.model.impl.Currency;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionAccount;
import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.impl.Order;
import com.tll.model.impl.OrderItem;
import com.tll.model.impl.OrderItemTrans;
import com.tll.model.impl.OrderTrans;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.impl.PaymentTrans;
import com.tll.model.impl.ProdCat;
import com.tll.model.impl.ProductCategory;
import com.tll.model.impl.ProductInventory;
import com.tll.model.impl.SalesTax;
import com.tll.model.impl.ShipBoundCost;
import com.tll.model.impl.ShipMode;
import com.tll.model.impl.SiteCode;
import com.tll.model.impl.User;
import com.tll.model.impl.Visitor;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * BusinessKeyFactory - Defines all entity business keys in the application and
 * provides utility methods relating to them.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public abstract class BusinessKeyFactory {

	/**
	 * BusinessKeyDefinition - Local impl of {@link IBusinessKeyDefinition}.
	 * @author jpk
	 */
	private static final class BusinessKeyDefinition<E extends IEntity> implements IBusinessKeyDefinition<E> {

		private final Class<E> entityClass;
		private final String[] propertyNames;
		private final String businessKeyName;

		public BusinessKeyDefinition(Class<E> entityClass, String businessKeyName, String[] propertyNames) {
			if(entityClass == null) throw new IllegalArgumentException("An entity type must be specified.");
			if(propertyNames == null || propertyNames.length < 1) {
				throw new IllegalArgumentException("At least one property must be specified in a business key");
			}
			this.entityClass = entityClass;
			this.propertyNames = propertyNames;
			this.businessKeyName = businessKeyName;
		}

		public Class<E> getType() {
			return entityClass;
		}

		public String getBusinessKeyName() {
			return businessKeyName;
		}

		public String[] getPropertyNames() {
			return propertyNames;
		}
	}

	private static final Map<Class<? extends IEntity>, IBusinessKeyDefinition[]> map =
			new HashMap<Class<? extends IEntity>, IBusinessKeyDefinition[]>();

	static {

		map.put(Account.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Account>(Account.class, "Name",
				new String[] { "name" }) });

		map.put(AccountAddress.class, new IBusinessKeyDefinition[] {
			new BusinessKeyDefinition<AccountAddress>(AccountAddress.class, "Binder", new String[] {
				"account.id",
				"address.id" }),
			new BusinessKeyDefinition<AccountAddress>(AccountAddress.class, "Account Id and Name", new String[] {
				"account.id",
				"name" }) });

		map.put(AccountHistory.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<AccountHistory>(
				AccountHistory.class, "Name", new String[] {
					"account.id",
					"transDate",
					"status" }) });

		map.put(Address.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Address>(Address.class,
				"Address and Postal Code", new String[] {
					"address1",
					"postalCode" }) });

		map.put(AppProperty.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<AppProperty>(AppProperty.class,
				"Name", new String[] { "name" }) });

		map.put(Authority.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Authority>(Authority.class,
				"Authority", new String[] { Authority.FIELDNAME_AUTHORITY }) });

		map.put(Currency.class, new IBusinessKeyDefinition[] {
			new BusinessKeyDefinition<Currency>(Currency.class, "Name", new String[] { INamedEntity.NAME }),
			new BusinessKeyDefinition<Currency>(Currency.class, "Symbol", new String[] { "symbol" }),
			new BusinessKeyDefinition<Currency>(Currency.class, "iso4217", new String[] { "iso4217" }) });

		map.put(CustomerAccount.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<CustomerAccount>(
				CustomerAccount.class, "Binder", new String[] {
					"customer.id",
					"account.id" }) });

		map.put(Interface.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Interface>(Interface.class,
				"Code", new String[] { "code" }) });

		map.put(InterfaceOption.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<InterfaceOption>(
				InterfaceOption.class, "Code", new String[] { "code" }) });

		map.put(InterfaceOptionAccount.class,
				new IBusinessKeyDefinition[] { new BusinessKeyDefinition<InterfaceOptionAccount>(InterfaceOptionAccount.class,
						"Binder", new String[] {
							"option.id",
							"account.id" }) });

		map.put(InterfaceOptionParameterDefinition.class,
				new IBusinessKeyDefinition[] { new BusinessKeyDefinition<InterfaceOptionParameterDefinition>(
						InterfaceOptionParameterDefinition.class, "Code", new String[] { "code" }) });

		map.put(Order.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Order>(Order.class, "Order Key",
				new String[] {
					"dateCreated",
					"account.id",
					"customer.id" }) });

		map.put(OrderItem.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<OrderItem>(OrderItem.class,
				"Order Id and Product SKU", new String[] {
					"order.id",
					"sku" }) });

		map.put(OrderItemTrans.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<OrderItemTrans>(
				OrderItemTrans.class, "Order Item Trans Binder", new String[] {
					"orderItem.id",
					"orderTrans.id" }) });

		map.put(OrderTrans.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<OrderTrans>(OrderTrans.class,
				"Order Trans Key", new String[] {
					"order.id",
					"dateCreated",
					"username" }) });

		map.put(PaymentInfo.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<PaymentInfo>(PaymentInfo.class,
				"Name", new String[] { INamedEntity.NAME }) });

		map.put(PaymentTrans.class, new IBusinessKeyDefinition[] {
			new BusinessKeyDefinition<PaymentTrans>(PaymentTrans.class, "Date and Op", new String[] {
				"payTransDate",
				"payOp",
				"payType" }),
			new BusinessKeyDefinition<PaymentTrans>(PaymentTrans.class, "Ref Num", new String[] { "refNum" }) });

		map.put(ProdCat.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<ProdCat>(ProdCat.class, "Binder",
				new String[] {
					"product.id",
					"category.id" }) });

		map.put(ProductCategory.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<ProductCategory>(
				ProductCategory.class, "Account Id and Name", new String[] {
					"account.id",
					"name" }) });

		map.put(ProductInventory.class, new IBusinessKeyDefinition[] {
			new BusinessKeyDefinition<ProductInventory>(ProductInventory.class, "Account Id and SKU", new String[] {
				"account.id",
				"sku" }),
			new BusinessKeyDefinition<ProductInventory>(ProductInventory.class, "Account Id and Title", new String[] {
				"account.id",
				"d1",
				"d2" }) });

		map.put(SalesTax.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<SalesTax>(SalesTax.class,
				"Province, County and Postal Code", new String[] {
					"account.id",
					"province",
					"county",
					"postalCode" }) });

		map.put(ShipBoundCost.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<ShipBoundCost>(
				ShipBoundCost.class, "Bounds", new String[] {
					"shipMode.id",
					"lbound",
					"ubound" }) });

		map.put(ShipMode.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<ShipMode>(ShipMode.class, "Name",
				new String[] {
					"account.id",
					"name" }) });

		map.put(SiteCode.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<SiteCode>(SiteCode.class, "Code",
				new String[] { "code" }) });

		map.put(User.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<User>(User.class, "Email Address",
				new String[] { "emailAddress" }) });

		map.put(Visitor.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Visitor>(Visitor.class, "Visitor",
				new String[] {
					"account.id",
					"dateCreated",
					"remoteHost" }) });

	}

	/**
	 * Provides all defined business key definitions for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return All defined business key definitions
	 * @throws BusinessKeyNotDefinedException Whe no business keys are defined for
	 *         the given entity type.
	 */
	public static <E extends IEntity> IBusinessKeyDefinition<E>[] definitions(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = map.get(entityClass);
		if(defs == null) {
			throw new BusinessKeyNotDefinedException(entityClass);
		}
		return defs;
	}

	/**
	 * Creates new and empty business keys for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return Array of empty business keys.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> BusinessKey<E>[] create(Class<E> entityClass) throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		BusinessKey<E>[] bks = new BusinessKey[defs.length];
		for(int i = 0; i < defs.length; ++i) {
			bks[i] = new BusinessKey<E>(defs[i]);
		}
		return bks;
	}

	/**
	 * Creates a new and empty business key for the given entity type and business
	 * key name.
	 * @param <E> The entity type.
	 * @param entityClass The entity class
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	public static <E extends IEntity> BusinessKey<E> create(Class<E> entityClass, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		IBusinessKeyDefinition<E> theDef = null;
		for(IBusinessKeyDefinition<E> def : defs) {
			if(def.getBusinessKeyName().equals(businessKeyName)) {
				theDef = def;
				break;
			}
		}
		if(theDef == null) {
			throw new BusinessKeyNotDefinedException(entityClass, businessKeyName);
		}
		return new BusinessKey<E>(theDef);
	}

	/**
	 * Creates all defined business keys with state extracted from the given
	 * entity.
	 * @param <E> The entity type
	 * @param entity The entity from which business keys are created
	 * @return Array of business keys with state extracted from the entity.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> BusinessKey<E>[] create(E entity) throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions((Class<E>) entity.entityClass());
		BusinessKey<E>[] bks = new BusinessKey[defs.length];
		for(int i = 0; i < defs.length; ++i) {
			bks[i] = new BusinessKey<E>(defs[i]);
		}
		fill(entity, bks);
		return bks;
	}

	/**
	 * Creates a single business key for the given entity type and business key
	 * name.
	 * @param <E> The entity type.
	 * @param entity The entity instance
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	public static <E extends IEntity> BusinessKey<E> create(E entity, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions((Class<E>) entity.entityClass());
		IBusinessKeyDefinition<E> theDef = null;
		for(IBusinessKeyDefinition<E> def : defs) {
			if(def.getBusinessKeyName().equals(businessKeyName)) {
				theDef = def;
				break;
			}
		}
		if(theDef == null) {
			throw new BusinessKeyNotDefinedException(entity.entityClass(), businessKeyName);
		}
		BusinessKey<E>[] bks = new BusinessKey[] { new BusinessKey(theDef) };
		fill(entity, bks);
		return bks[0];
	}

	/**
	 * Fills the given business keys with the state of the given entity that type
	 * matches.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bks The business keys for the entity type that are to be filled by
	 *        the values held in the given entity.
	 */
	private static <E extends IEntity> void fill(E entity, BusinessKey<E>[] bks) {
		BeanWrapperImpl bw = new BeanWrapperImpl(entity);
		for(BusinessKey<E> bk : bks) {
			for(String pname : bk.getPropertyNames()) {
				bk.setPropertyValue(pname, bw.getPropertyValue(pname));
			}
		}
	}
}
