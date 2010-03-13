package com.tll.model;

import com.google.inject.Inject;

/**
 * SmbizEntityAssembler
 * @author jpk
 */
public final class SmbizEntityAssembler implements IEntityAssembler {

	private final IEntityFactory<?> entityFactory;

	/**
	 * Constructor
	 * @param entityFactory required
	 */
	@Inject
	public SmbizEntityAssembler(IEntityFactory<?> entityFactory) {
		super();
		this.entityFactory = entityFactory;
	}

	private <E extends IEntity> E createEntity(Class<E> entityClass) {
		return entityFactory.createEntity(entityClass, true);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E assembleEntity(Class<E> entityType, IEntityProvider entityProvider) {
		E e = null;
		if(AccountAddress.class.equals(entityType)) {
			final AccountAddress ae = createEntity(AccountAddress.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			Address a = entityProvider == null ? null : entityProvider.getEntityByType(Address.class);
			if(a == null) {
				a = createEntity(Address.class);
				//if(generate) assignPrimaryKey(a);
			}
			ae.setAddress(a);
			e = (E) ae;
		}
		else if(AccountHistory.class.equals(entityType)) {
			final AccountHistory ae = createEntity(AccountHistory.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Address.class.equals(entityType)) {
			final Address ae = createEntity(Address.class);
			e = (E) ae;
		}
		else if(AppProperty.class.equals(entityType)) {
			final AppProperty ae = createEntity(AppProperty.class);
			e = (E) ae;
		}
		else if(Authority.class.equals(entityType)) {
			final Authority ae = createEntity(Authority.class);
			e = (E) ae;
		}
		else if(Currency.class.equals(entityType)) {
			final Currency ae = createEntity(Currency.class);
			e = (E) ae;
		}
		else if(Customer.class.equals(entityType)) {
			final Customer ae = createEntity(Customer.class);
			e = (E) ae;
		}
		else if(CustomerAccount.class.equals(entityType)) {
			final CustomerAccount ae = createEntity(CustomerAccount.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setCustomer(entityProvider.getEntityByType(Customer.class));
			}
			e = (E) ae;
		}
		else if(InterfaceMulti.class.equals(entityType)) {
			final InterfaceMulti ae = createEntity(InterfaceMulti.class);
			e = (E) ae;
		}
		else if(InterfaceOption.class.equals(entityType)) {
			final InterfaceOption ae = createEntity(InterfaceOption.class);
			e = (E) ae;
		}
		else if(InterfaceOptionAccount.class.equals(entityType)) {
			final InterfaceOptionAccount ae = createEntity(InterfaceOptionAccount.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
				ae.setOption(entityProvider.getEntityByType(InterfaceOption.class));
			}
			e = (E) ae;
		}
		else if(InterfaceOptionParameterDefinition.class.equals(entityType)) {
			final InterfaceOptionParameterDefinition ae = createEntity(InterfaceOptionParameterDefinition.class);
			e = (E) ae;
		}
		else if(InterfaceSingle.class.equals(entityType)) {
			final InterfaceSingle ae = createEntity(InterfaceSingle.class);
			e = (E) ae;
		}
		else if(InterfaceSwitch.class.equals(entityType)) {
			final InterfaceSwitch ae = createEntity(InterfaceSwitch.class);
			e = (E) ae;
		}
		else if(AccountInterface.class == entityType) {
			final AccountInterface ai = createEntity(AccountInterface.class);
			e = (E) ai;
		}
		else if(AccountInterfaceOption.class == entityType) {
			final AccountInterfaceOption o = createEntity(AccountInterfaceOption.class);
			e = (E) o;
		}
		else if(AccountInterfaceOptionParameter.class == entityType) {
			final AccountInterfaceOptionParameter p = createEntity(AccountInterfaceOptionParameter.class);
			e = (E) p;
		}
		else if(Isp.class.equals(entityType)) {
			final Isp ae = createEntity(Isp.class);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Merchant.class.equals(entityType)) {
			final Merchant ae = createEntity(Merchant.class);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Order.class.equals(entityType)) {
			final Order ae = createEntity(Order.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(OrderItem.class.equals(entityType)) {
			final OrderItem ae = createEntity(OrderItem.class);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(OrderItemTrans.class.equals(entityType)) {
			final OrderItemTrans ae = createEntity(OrderItemTrans.class);
			if(entityProvider != null) {
				ae.setOrderItem(entityProvider.getEntityByType(OrderItem.class));
				ae.setOrderTrans(entityProvider.getEntityByType(OrderTrans.class));
			}
			e = (E) ae;
		}
		else if(OrderTrans.class.equals(entityType)) {
			final OrderTrans ae = createEntity(OrderTrans.class);
			if(entityProvider != null) {
				ae.setOrder(entityProvider.getEntityByType(Order.class));
			}
			e = (E) ae;
		}
		else if(PaymentInfo.class.equals(entityType)) {
			final PaymentInfo ae = createEntity(PaymentInfo.class);
			if(entityProvider != null) {
				ae.setPaymentData(new PaymentData());
			}
			e = (E) ae;
		}
		else if(PaymentTrans.class.equals(entityType)) {
			final PaymentTrans ae = createEntity(PaymentTrans.class);
			e = (E) ae;
		}
		else if(ProdCat.class.equals(entityType)) {
			final ProdCat ae = createEntity(ProdCat.class);
			e = (E) ae;
		}
		else if(ProductCategory.class.equals(entityType)) {
			final ProductCategory ae = createEntity(ProductCategory.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ProductGeneral.class.equals(entityType)) {
			final ProductGeneral ae = createEntity(ProductGeneral.class);
			e = (E) ae;
		}
		else if(ProductInventory.class.equals(entityType)) {
			final ProductInventory ae = createEntity(ProductInventory.class);
			if(entityProvider != null) {
				ProductGeneral pg = entityProvider.getEntityByType(ProductGeneral.class);
				if(pg == null) {
					pg = createEntity(ProductGeneral.class);
				}
				ae.setProductGeneral(pg);
			}
			e = (E) ae;
		}
		else if(SalesTax.class.equals(entityType)) {
			final SalesTax ae = createEntity(SalesTax.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(ShipBoundCost.class.equals(entityType)) {
			final ShipBoundCost ae = createEntity(ShipBoundCost.class);
			if(entityProvider != null) {
				ae.setParent(entityProvider.getEntityByType(ShipMode.class));
			}
			e = (E) ae;
		}
		else if(ShipMode.class.equals(entityType)) {
			final ShipMode ae = createEntity(ShipMode.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(SiteCode.class.equals(entityType)) {
			final SiteCode ae = createEntity(SiteCode.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(User.class.equals(entityType)) {
			final User ae = createEntity(User.class);
			if(entityProvider != null) {
				ae.setAccount(entityProvider.getEntityByType(Account.class));
			}
			e = (E) ae;
		}
		else if(Visitor.class.equals(entityType)) {
			final Visitor ae = createEntity(Visitor.class);
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