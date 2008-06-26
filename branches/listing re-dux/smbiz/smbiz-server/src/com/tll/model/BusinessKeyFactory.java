/**
 * The Logic Lab
 * @author jpk
 * Jun 26, 2008
 */
package com.tll.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private static final Set<IBusinessKeyDefinition> set = new HashSet<IBusinessKeyDefinition>();

	static {
		set.add(new BusinessKeyDefinition(Account.class, "Name", new String[] { "name" }));

		set.add(new BusinessKeyDefinition(AccountAddress.class, "Binder", new String[] {
			"account.id",
			"address.id" }));
		set.add(new BusinessKeyDefinition(AccountAddress.class, "Account Id and Name", new String[] {
			"account.id",
			"name" }));

		set.add(new BusinessKeyDefinition(AccountHistory.class, "Name", new String[] {
			"account.id",
			"transDate",
			"status" }));

		set.add(new BusinessKeyDefinition(Address.class, "Address and Postal Code", new String[] {
			"address1",
			"postalCode" }));

		set.add(new BusinessKeyDefinition(AppProperty.class, "Name", new String[] { "name" }));

		set.add(new BusinessKeyDefinition(Authority.class, "Authority", new String[] { Authority.FIELDNAME_AUTHORITY }));

		set.add(new BusinessKeyDefinition(Currency.class, "Name", new String[] { INamedEntity.NAME }));
		set.add(new BusinessKeyDefinition(Currency.class, "Symbol", new String[] { "symbol" }));
		set.add(new BusinessKeyDefinition(Currency.class, "iso4217", new String[] { "iso4217" }));

		set.add(new BusinessKeyDefinition(CustomerAccount.class, "Binder", new String[] {
			"customer.id",
			"account.id" }));

		set.add(new BusinessKeyDefinition(Interface.class, "Code", new String[] { "code" }));

		set.add(new BusinessKeyDefinition(InterfaceOption.class, "Code", new String[] { "code" }));

		set.add(new BusinessKeyDefinition(InterfaceOptionAccount.class, "Binder", new String[] {
			"option.id",
			"account.id" }));

		set.add(new BusinessKeyDefinition(InterfaceOptionParameterDefinition.class, "Code", new String[] { "code" }));

		set.add(new BusinessKeyDefinition(Order.class, "Order Key", new String[] {
			"dateCreated",
			"account.id",
			"customer.id" }));

		set.add(new BusinessKeyDefinition(OrderItem.class, "Order Id and Product SKU", new String[] {
			"order.id",
			"sku" }));

		set.add(new BusinessKeyDefinition(OrderItemTrans.class, "Order Item Trans Binder", new String[] {
			"orderItem.id",
			"orderTrans.id" }));

		set.add(new BusinessKeyDefinition(OrderTrans.class, "Order Trans Key", new String[] {
			"order.id",
			"dateCreated",
			"username" }));

		set.add(new BusinessKeyDefinition(PaymentInfo.class, "Name", new String[] { INamedEntity.NAME }));

		set.add(new BusinessKeyDefinition(PaymentTrans.class, "Date and Op", new String[] {
			"payTransDate",
			"payOp",
			"payType" }));
		set.add(new BusinessKeyDefinition(PaymentTrans.class, "Ref Num", new String[] { "refNum" }));

		set.add(new BusinessKeyDefinition(ProdCat.class, "Binder", new String[] {
			"product.id",
			"category.id" }));

		set.add(new BusinessKeyDefinition(ProductCategory.class, "Account Id and Name", new String[] {
			"account.id",
			"name" }));

		set.add(new BusinessKeyDefinition(ProductInventory.class, "Account Id and SKU", new String[] {
			"account.id",
			"sku" }));
		set.add(new BusinessKeyDefinition(ProductInventory.class, "Account Id and Title", new String[] {
			"account.id",
			"d1",
			"d2" }));

		set.add(new BusinessKeyDefinition(SalesTax.class, "Province, County and Postal Code", new String[] {
			"account.id",
			"province",
			"county",
			"postalCode" }));

		set.add(new BusinessKeyDefinition(ShipBoundCost.class, "Bounds", new String[] {
			"shipMode.id",
			"lbound",
			"ubound" }));

		set.add(new BusinessKeyDefinition(ShipMode.class, "Name", new String[] {
			"account.id",
			"name" }));

		set.add(new BusinessKeyDefinition(SiteCode.class, "Code", new String[] { "code" }));

		set.add(new BusinessKeyDefinition(User.class, "Email Address", new String[] { "emailAddress" }));

		set.add(new BusinessKeyDefinition(Visitor.class, "Visitor", new String[] {
			"account.id",
			"dateCreated",
			"remoteHost" }));

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
		List<IBusinessKeyDefinition<? extends E>> list = new ArrayList<IBusinessKeyDefinition<? extends E>>();
		for(IBusinessKeyDefinition<IEntity> def : set) {
			if(def.getType().isAssignableFrom(entityClass)) {
				list.add((IBusinessKeyDefinition<? extends E>) def);
			}
		}
		if(list.size() < 1) {
			throw new BusinessKeyNotDefinedException(entityClass);
		}
		return list.toArray(new IBusinessKeyDefinition[list.size()]);
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
