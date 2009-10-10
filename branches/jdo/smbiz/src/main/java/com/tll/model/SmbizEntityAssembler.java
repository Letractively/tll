package com.tll.model;

import javax.jdo.annotations.PersistenceAware;

import com.google.inject.Inject;

/**
 * EntityAssembler - Decorator around the {@link IEntityFactory} with additional
 * build functionality specific to the entity type.
 * @author jpk
 */
@PersistenceAware
public final class SmbizEntityAssembler implements IEntityFactory, IEntityAssembler {

	/**
	 * The decorated entity factory.
	 */
	private final IEntityFactory entityFactory;

	/**
	 * Constructor
	 * @param entityFactory
	 */
	@Inject
	public SmbizEntityAssembler(IEntityFactory entityFactory) {
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
			final AccountAddress ae = createEntity(AccountAddress.class, generate);
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
			final AccountHistory ae = createEntity(AccountHistory.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Address.class.equals(entityType)) {
			final Address ae = createEntity(Address.class, generate);
			e = (E) ae;
		}
		else if(AppProperty.class.equals(entityType)) {
			final AppProperty ae = createEntity(AppProperty.class, generate);
			e = (E) ae;
		}
		else if(Authority.class.equals(entityType)) {
			final Authority ae = createEntity(Authority.class, generate);
			e = (E) ae;
		}
		else if(Currency.class.equals(entityType)) {
			final Currency ae = createEntity(Currency.class, generate);
			e = (E) ae;
		}
		else if(Customer.class.equals(entityType)) {
			final Customer ae = createEntity(Customer.class, generate);
			e = (E) ae;
		}
		else if(CustomerAccount.class.equals(entityType)) {
			final CustomerAccount ae = createEntity(CustomerAccount.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setCustomer(entityProvider.getEntityByType(Customer.class));
			}
			e = (E) ae;
		}
		else if(InterfaceMulti.class.equals(entityType)) {
			final InterfaceMulti ae = createEntity(InterfaceMulti.class, generate);
			e = (E) ae;
		}
		else if(InterfaceOption.class.equals(entityType)) {
			final InterfaceOption ae = createEntity(InterfaceOption.class, generate);
			e = (E) ae;
		}
		else if(InterfaceOptionAccount.class.equals(entityType)) {
			final InterfaceOptionAccount ae = createEntity(InterfaceOptionAccount.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setOption(entityProvider.getEntityByType(InterfaceOption.class));
			}
			e = (E) ae;
		}
		else if(InterfaceOptionParameterDefinition.class.equals(entityType)) {
			final InterfaceOptionParameterDefinition ae = createEntity(InterfaceOptionParameterDefinition.class, generate);
			e = (E) ae;
		}
		else if(InterfaceSingle.class.equals(entityType)) {
			final InterfaceSingle ae = createEntity(InterfaceSingle.class, generate);
			e = (E) ae;
		}
		else if(InterfaceSwitch.class.equals(entityType)) {
			final InterfaceSwitch ae = createEntity(InterfaceSwitch.class, generate);
			e = (E) ae;
		}
		else if(Isp.class.equals(entityType)) {
			final Isp ae = createEntity(Isp.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Merchant.class.equals(entityType)) {
			final Merchant ae = createEntity(Merchant.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Order.class.equals(entityType)) {
			final Order ae = createEntity(Order.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(OrderItem.class.equals(entityType)) {
			final OrderItem ae = createEntity(OrderItem.class, generate);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(OrderItemTrans.class.equals(entityType)) {
			final OrderItemTrans ae = createEntity(OrderItemTrans.class, generate);
			if(entityProvider != null) {
				ae.setOrderItem(entityProvider.getEntityByType(OrderItem.class));
				ae.setOrderTrans(entityProvider.getEntityByType(OrderTrans.class));
			}
			e = (E) ae;
		}
		else if(OrderTrans.class.equals(entityType)) {
			final OrderTrans ae = createEntity(OrderTrans.class, generate);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(PaymentInfo.class.equals(entityType)) {
			final PaymentInfo ae = createEntity(PaymentInfo.class, generate);
			if(entityProvider != null) {
				ae.setPaymentData(new PaymentData());
			}
			e = (E) ae;
		}
		else if(PaymentTrans.class.equals(entityType)) {
			final PaymentTrans ae = createEntity(PaymentTrans.class, generate);
			e = (E) ae;
		}
		else if(ProdCat.class.equals(entityType)) {
			final ProdCat ae = createEntity(ProdCat.class, generate);
			e = (E) ae;
		}
		else if(ProductCategory.class.equals(entityType)) {
			final ProductCategory ae = createEntity(ProductCategory.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ProductGeneral.class.equals(entityType)) {
			final ProductGeneral ae = createEntity(ProductGeneral.class, generate);
			e = (E) ae;
		}
		else if(ProductInventory.class.equals(entityType)) {
			final ProductInventory ae = createEntity(ProductInventory.class, generate);
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
			final SalesTax ae = createEntity(SalesTax.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ShipBoundCost.class.equals(entityType)) {
			final ShipBoundCost ae = createEntity(ShipBoundCost.class, generate);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(ShipMode.class));
			}
			e = (E) ae;
		}
		else if(ShipMode.class.equals(entityType)) {
			final ShipMode ae = createEntity(ShipMode.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(SiteCode.class.equals(entityType)) {
			final SiteCode ae = createEntity(SiteCode.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(User.class.equals(entityType)) {
			final User ae = createEntity(User.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Visitor.class.equals(entityType)) {
			final Visitor ae = createEntity(Visitor.class, generate);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}

		else
			throw new IllegalArgumentException("Unsupported entity type '" + entityType + "' for assembly");
		return e;
	}
}