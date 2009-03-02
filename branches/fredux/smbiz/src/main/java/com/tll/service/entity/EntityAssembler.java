package com.tll.service.entity;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.AccountHistory;
import com.tll.model.Address;
import com.tll.model.AppProperty;
import com.tll.model.Authority;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityProvider;
import com.tll.model.InterfaceMulti;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.InterfaceSingle;
import com.tll.model.InterfaceSwitch;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.Order;
import com.tll.model.OrderItem;
import com.tll.model.OrderItemTrans;
import com.tll.model.OrderTrans;
import com.tll.model.PaymentData;
import com.tll.model.PaymentInfo;
import com.tll.model.PaymentTrans;
import com.tll.model.ProdCat;
import com.tll.model.ProductCategory;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;
import com.tll.model.SalesTax;
import com.tll.model.ShipBoundCost;
import com.tll.model.ShipMode;
import com.tll.model.SiteCode;
import com.tll.model.User;
import com.tll.model.Visitor;
import com.tll.service.entity.IEntityAssembler;

/**
 * EntityAssembler - Decorator around the {@link IEntityFactory} with additional
 * build functionality specific to the entity type.
 * @author jpk
 */
public final class EntityAssembler implements IEntityFactory, IEntityAssembler {

	/**
	 * The decorated entity factory.
	 */
	private final IEntityFactory entityFactory;

	/**
	 * Constructor
	 * @param entityFactory
	 */
	@Inject
	public EntityAssembler(IEntityFactory entityFactory) {
		super();
		this.entityFactory = entityFactory;
	}

	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) {
		return entityFactory.createEntity(entityClass, generate);
	}

	public <E extends IEntity> void setGenerated(E entity) {
		entityFactory.setGenerated(entity);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E assembleEntity(Class<E> entityType, IEntityProvider entityProvider, boolean generate) {
		E e = null;
		if(AccountAddress.class.equals(entityType)) {
			AccountAddress ae = createEntity(AccountAddress.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			Address a = entityProvider == null ? null : entityProvider.getEntityByType(Address.class);
			if(a == null) {
				a = createEntity(Address.class, generate);
			}
			ae.setAddress(a);
			e = (E) ae;
		}
		else if(AccountHistory.class.equals(entityType)) {
			AccountHistory ae = createEntity(AccountHistory.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Address.class.equals(entityType)) {
			Address ae = createEntity(Address.class, generate);
			e = (E) ae;
		}
		else if(AppProperty.class.equals(entityType)) {
			AppProperty ae = createEntity(AppProperty.class, generate);
			e = (E) ae;
		}
		else if(Authority.class.equals(entityType)) {
			Authority ae = createEntity(Authority.class, generate);
			e = (E) ae;
		}
		else if(Currency.class.equals(entityType)) {
			Currency ae = createEntity(Currency.class, generate);
			e = (E) ae;
		}
		else if(Customer.class.equals(entityType)) {
			Customer ae = createEntity(Customer.class, generate);
			e = (E) ae;
		}
		else if(CustomerAccount.class.equals(entityType)) {
			CustomerAccount ae = createEntity(CustomerAccount.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setCustomer(entityProvider.getEntityByType(Customer.class));
			}
			e = (E) ae;
		}
		else if(InterfaceMulti.class.equals(entityType)) {
			InterfaceMulti ae = createEntity(InterfaceMulti.class, generate);
			e = (E) ae;
		}
		else if(InterfaceOption.class.equals(entityType)) {
			InterfaceOption ae = createEntity(InterfaceOption.class, generate);
			e = (E) ae;
		}
		else if(InterfaceOptionAccount.class.equals(entityType)) {
			InterfaceOptionAccount ae = createEntity(InterfaceOptionAccount.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setOption(entityProvider.getEntityByType(InterfaceOption.class));
			}
			e = (E) ae;
		}
		else if(InterfaceOptionParameterDefinition.class.equals(entityType)) {
			InterfaceOptionParameterDefinition ae = createEntity(InterfaceOptionParameterDefinition.class, generate);
			e = (E) ae;
		}
		else if(InterfaceSingle.class.equals(entityType)) {
			InterfaceSingle ae = createEntity(InterfaceSingle.class, generate);
			e = (E) ae;
		}
		else if(InterfaceSwitch.class.equals(entityType)) {
			InterfaceSwitch ae = createEntity(InterfaceSwitch.class, generate);
			e = (E) ae;
		}
		else if(Isp.class.equals(entityType)) {
			Isp ae = createEntity(Isp.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Merchant.class.equals(entityType)) {
			Merchant ae = createEntity(Merchant.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Order.class.equals(entityType)) {
			Order ae = createEntity(Order.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(OrderItem.class.equals(entityType)) {
			OrderItem ae = createEntity(OrderItem.class, generate);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(OrderItemTrans.class.equals(entityType)) {
			OrderItemTrans ae = createEntity(OrderItemTrans.class, generate);
			if(entityProvider != null) {
				ae.setOrderItem(entityProvider.getEntityByType(OrderItem.class));
				ae.setOrderTrans(entityProvider.getEntityByType(OrderTrans.class));
			}
			e = (E) ae;
		}
		else if(OrderTrans.class.equals(entityType)) {
			OrderTrans ae = createEntity(OrderTrans.class, generate);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(PaymentInfo.class.equals(entityType)) {
			PaymentInfo ae = createEntity(PaymentInfo.class, generate);
			if(entityProvider != null) {
				ae.setPaymentData(new PaymentData());
			}
			e = (E) ae;
		}
		else if(PaymentTrans.class.equals(entityType)) {
			PaymentTrans ae = createEntity(PaymentTrans.class, generate);
			e = (E) ae;
		}
		else if(ProdCat.class.equals(entityType)) {
			ProdCat ae = createEntity(ProdCat.class, generate);
			e = (E) ae;
		}
		else if(ProductCategory.class.equals(entityType)) {
			ProductCategory ae = createEntity(ProductCategory.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ProductGeneral.class.equals(entityType)) {
			ProductGeneral ae = createEntity(ProductGeneral.class, generate);
			e = (E) ae;
		}
		else if(ProductInventory.class.equals(entityType)) {
			ProductInventory ae = createEntity(ProductInventory.class, generate);
			if(entityProvider != null) {
				ProductGeneral pg = entityProvider.getEntityByType(ProductGeneral.class);
				if(pg == null) {
					pg = createEntity(ProductGeneral.class, generate);
				}
				ae.setProductGeneral(pg);
			}
			e = (E) ae;
		}
		else if(SalesTax.class.equals(entityType)) {
			SalesTax ae = createEntity(SalesTax.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ShipBoundCost.class.equals(entityType)) {
			ShipBoundCost ae = createEntity(ShipBoundCost.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(ShipMode.class));
			}
			e = (E) ae;
		}
		else if(ShipMode.class.equals(entityType)) {
			ShipMode ae = createEntity(ShipMode.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(SiteCode.class.equals(entityType)) {
			SiteCode ae = createEntity(SiteCode.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(User.class.equals(entityType)) {
			User ae = createEntity(User.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Visitor.class.equals(entityType)) {
			Visitor ae = createEntity(Visitor.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}

		else
			throw new SystemError("Unsupported entity type '" + entityType + "' for assembly");
		return e;
	}
}