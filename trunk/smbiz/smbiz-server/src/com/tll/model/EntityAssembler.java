package com.tll.model;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.Address;
import com.tll.model.impl.AppProperty;
import com.tll.model.impl.Authority;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Customer;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.EntityType;
import com.tll.model.impl.InterfaceMulti;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionAccount;
import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.impl.InterfaceSingle;
import com.tll.model.impl.InterfaceSwitch;
import com.tll.model.impl.Isp;
import com.tll.model.impl.Merchant;
import com.tll.model.impl.Order;
import com.tll.model.impl.OrderItem;
import com.tll.model.impl.OrderItemTrans;
import com.tll.model.impl.OrderTrans;
import com.tll.model.impl.PaymentData;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.impl.PaymentTrans;
import com.tll.model.impl.ProdCat;
import com.tll.model.impl.ProductCategory;
import com.tll.model.impl.ProductGeneral;
import com.tll.model.impl.ProductInventory;
import com.tll.model.impl.SalesTax;
import com.tll.model.impl.ShipBoundCost;
import com.tll.model.impl.ShipMode;
import com.tll.model.impl.SiteCode;
import com.tll.model.impl.User;
import com.tll.model.impl.Visitor;

/**
 * The entity assembler. Decorates {@link IEntityFactory} so client contexts only need have a ref to
 * the {@link EntityAssembler}.
 * @author jpk
 */
public final class EntityAssembler implements IEntityFactory {

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

	/**
	 * Generically assembles an entity of the given type.
	 * @param <E>
	 * @param entityType
	 * @param entityProvider Contains related entities that are wired to the assembled entity. May be
	 *          <code>null</code>.
	 * @param generate Generate an id?
	 * @return The assembled IEntity of the specified type.
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> E assembleEntity(EntityType entityType, IEntityProvider entityProvider, boolean generate) {
		E e = null;
		switch(entityType) {
			case ACCOUNT_ADDRESS: {
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
				break;
			}
			case ACCOUNT_HISTORY: {
				AccountHistory ae = createEntity(AccountHistory.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case ADDRESS: {
				Address ae = createEntity(Address.class, generate);
				e = (E) ae;
				break;
			}
			case APP_PROPERTY: {
				AppProperty ae = createEntity(AppProperty.class, generate);
				e = (E) ae;
				break;
			}
			case AUTHORITY: {
				Authority ae = createEntity(Authority.class, generate);
				e = (E) ae;
				break;
			}
			case CURRENCY: {
				Currency ae = createEntity(Currency.class, generate);
				e = (E) ae;
				break;
			}
			case CUSTOMER: {
				Customer ae = createEntity(Customer.class, generate);
				e = (E) ae;
				break;
			}
			case CUSTOMER_ACCOUNT: {
				CustomerAccount ae = createEntity(CustomerAccount.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
					ae.setCustomer(entityProvider.getEntityByType(Customer.class));
				}
				e = (E) ae;
				break;
			}
			case INTERFACE_MULTI: {
				InterfaceMulti ae = createEntity(InterfaceMulti.class, generate);
				e = (E) ae;
				break;
			}
			case INTERFACE_OPTION: {
				InterfaceOption ae = createEntity(InterfaceOption.class, generate);
				e = (E) ae;
				break;
			}
			case INTERFACE_OPTION_ACCOUNT: {
				InterfaceOptionAccount ae = createEntity(InterfaceOptionAccount.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
					ae.setOption(entityProvider.getEntityByType(InterfaceOption.class));
				}
				e = (E) ae;
				break;
			}
			case INTERFACE_OPTION_PARAMETER_DEFINITION: {
				InterfaceOptionParameterDefinition ae = createEntity(InterfaceOptionParameterDefinition.class, generate);
				e = (E) ae;
				break;
			}
			case INTERFACE_SINGLE: {
				InterfaceSingle ae = createEntity(InterfaceSingle.class, generate);
				e = (E) ae;
				break;
			}
			case INTERFACE_SWITCH: {
				InterfaceSwitch ae = createEntity(InterfaceSwitch.class, generate);
				e = (E) ae;
				break;
			}
			case ISP: {
				Isp ae = createEntity(Isp.class, generate);
				if(entityProvider != null) {
					ae.setParent(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case MERCHANT: {
				Merchant ae = createEntity(Merchant.class, generate);
				if(entityProvider != null) {
					ae.setParent(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case ORDER: {
				Order ae = createEntity(Order.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case ORDER_ITEM: {
				OrderItem ae = createEntity(OrderItem.class, generate);
				if(entityProvider != null) {
					ae.setOrder(entityProvider.getEntityByType(Order.class));
				}
				e = (E) ae;
				break;
			}
			case ORDER_ITEM_TRANS: {
				OrderItemTrans ae = createEntity(OrderItemTrans.class, generate);
				if(entityProvider != null) {
					ae.setOrderItem(entityProvider.getEntityByType(OrderItem.class));
					ae.setOrderTrans(entityProvider.getEntityByType(OrderTrans.class));
				}
				e = (E) ae;
				break;
			}
			case ORDER_TRANS: {
				OrderTrans ae = createEntity(OrderTrans.class, generate);
				if(entityProvider != null) {
					ae.setOrder(entityProvider.getEntityByType(Order.class));
				}
				e = (E) ae;
				break;
			}
			case PAYMENT_INFO: {
				PaymentInfo ae = createEntity(PaymentInfo.class, generate);
				if(entityProvider != null) {
					ae.setPaymentData(new PaymentData());
				}
				e = (E) ae;
				break;
			}
			case PAYMENT_TRANS: {
				PaymentTrans ae = createEntity(PaymentTrans.class, generate);
				e = (E) ae;
				break;
			}
			case PROD_CAT: {
				ProdCat ae = createEntity(ProdCat.class, generate);
				e = (E) ae;
				break;
			}
			case PRODUCT_CATEGORY: {
				ProductCategory ae = createEntity(ProductCategory.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case PRODUCT_GENERAL: {
				ProductGeneral ae = createEntity(ProductGeneral.class, generate);
				e = (E) ae;
				break;
			}
			case PRODUCT_INVENTORY: {
				ProductInventory ae = createEntity(ProductInventory.class, generate);
				if(entityProvider != null) {
					ProductGeneral pg = entityProvider.getEntityByType(ProductGeneral.class);
					if(pg == null) {
						pg = createEntity(ProductGeneral.class, generate);
					}
					ae.setProductGeneral(pg);
				}
				e = (E) ae;
				break;
			}
			case SALES_TAX: {
				SalesTax ae = createEntity(SalesTax.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case SHIP_BOUND_COST: {
				ShipBoundCost ae = createEntity(ShipBoundCost.class, generate);
				if(entityProvider != null) {
					ae.setParent(entityProvider.getEntityByType(ShipMode.class));
				}
				e = (E) ae;
				break;
			}
			case SHIP_MODE: {
				ShipMode ae = createEntity(ShipMode.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case SITE_CODE: {
				SiteCode ae = createEntity(SiteCode.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case USER: {
				User ae = createEntity(User.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}
			case VISITOR: {
				Visitor ae = createEntity(Visitor.class, generate);
				if(entityProvider != null) {
					ae.setAccount(entityProvider.getEntityByType(Account.class));
				}
				e = (E) ae;
				break;
			}

				// case ACCOUNT:
				// case ASP:
				// case INTERFACE:
				// case PCH:
			default:
				throw new SystemError("Unsupported entity type '" + entityType + "' for assembly");
		}
		return e;
	}
}