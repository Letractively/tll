/*
 * The Logic Lab 
 */
package com.tll.di;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.DaoFactory;
import com.tll.dao.DaoMode;
import com.tll.dao.IDao;
import com.tll.dao.IDaoFactory;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.dialect.MySqlDialectHandler;
import com.tll.dao.hibernate.ComparatorTranslator;
import com.tll.dao.hibernate.PrimaryKeyGenerator;
import com.tll.dao.impl.IAccountAddressDao;
import com.tll.dao.impl.IAccountDao;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.dao.impl.IAclDao;
import com.tll.dao.impl.IAddressDao;
import com.tll.dao.impl.IAppPropertyDao;
import com.tll.dao.impl.IAuthorityDao;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.dao.impl.ICustomerAccountDao;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.dao.impl.IInterfaceOptionAccountDao;
import com.tll.dao.impl.IOrderDao;
import com.tll.dao.impl.IOrderItemDao;
import com.tll.dao.impl.IOrderItemTransDao;
import com.tll.dao.impl.IOrderTransDao;
import com.tll.dao.impl.IPCHDao;
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.dao.impl.IPaymentTransDao;
import com.tll.dao.impl.IProdCatDao;
import com.tll.dao.impl.IProductCategoryDao;
import com.tll.dao.impl.IProductInventoryDao;
import com.tll.dao.impl.ISalesTaxDao;
import com.tll.dao.impl.IShipBoundCostDao;
import com.tll.dao.impl.IShipModeDao;
import com.tll.dao.impl.ISiteCodeDao;
import com.tll.dao.impl.ISiteStatisticsDao;
import com.tll.dao.impl.IUserDao;
import com.tll.dao.impl.IVisitorDao;
import com.tll.dao.impl.hibernate.AccountAddressDao;
import com.tll.dao.impl.hibernate.AccountDao;
import com.tll.dao.impl.hibernate.AccountHistoryDao;
import com.tll.dao.impl.hibernate.AddressDao;
import com.tll.dao.impl.hibernate.AppPropertyDao;
import com.tll.dao.impl.hibernate.AuthorityDao;
import com.tll.dao.impl.hibernate.CurrencyDao;
import com.tll.dao.impl.hibernate.CustomerAccountDao;
import com.tll.dao.impl.hibernate.InterfaceDao;
import com.tll.dao.impl.hibernate.InterfaceOptionAccountDao;
import com.tll.dao.impl.hibernate.OrderDao;
import com.tll.dao.impl.hibernate.OrderItemDao;
import com.tll.dao.impl.hibernate.OrderItemTransDao;
import com.tll.dao.impl.hibernate.OrderTransDao;
import com.tll.dao.impl.hibernate.PCHDao;
import com.tll.dao.impl.hibernate.PaymentInfoDao;
import com.tll.dao.impl.hibernate.PaymentTransDao;
import com.tll.dao.impl.hibernate.ProdCatDao;
import com.tll.dao.impl.hibernate.ProductCategoryDao;
import com.tll.dao.impl.hibernate.ProductInventoryDao;
import com.tll.dao.impl.hibernate.SalesTaxDao;
import com.tll.dao.impl.hibernate.ShipBoundCostDao;
import com.tll.dao.impl.hibernate.ShipModeDao;
import com.tll.dao.impl.hibernate.SiteCodeDao;
import com.tll.dao.impl.hibernate.SiteStatisticsDao;
import com.tll.dao.impl.hibernate.UserDao;
import com.tll.dao.impl.hibernate.VisitorDao;
import com.tll.dao.impl.jdbc.acl.AclDao;
import com.tll.dao.mock.EntityGraph;
import com.tll.model.EntityAssembler;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityFactory;
import com.tll.model.MockEntityProvider;
import com.tll.model.MockPrimaryKeyGenerator;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.Address;
import com.tll.model.impl.AppProperty;
import com.tll.model.impl.Authority;
import com.tll.model.impl.Currency;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOptionAccount;
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
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;
import com.tll.util.EnumUtil;

/**
 * DaoModule
 * @author jpk
 */
public class DaoModule extends CompositeModule {

	/**
	 * DAO mode override. <code>null</code> indicates the property will be
	 * gotten from the {@link Config} instance.
	 */
	private final DaoMode daoMode;

	/**
	 * Constructor
	 */
	public DaoModule() {
		this(null);
	}

	/**
	 * Constructor
	 * @param daoMode
	 */
	public DaoModule(DaoMode daoMode) {
		super();
		this.daoMode =
				daoMode == null ? EnumUtil.fromString(DaoMode.class, Config.instance().getString(
						ConfigKeys.DAO_MODE_PARAM.getKey())) : daoMode;
	}

	@Override
	protected Module[] getModulesToBind() {
		if(DaoMode.MOCK.equals(daoMode)) {
			return new Module[] { new MockDaoModule() };
		}
		return new Module[] { new HibernateDaoModule() };
	}

	/**
	 * AbstractDaoModule
	 * @author jpk
	 */
	protected static abstract class AbstractDaoModule extends GModule {

		@Override
		protected void configure() {
			// IEntityFactory
			bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

			// EntityAssembler
			bind(EntityAssembler.class).in(Scopes.SINGLETON);

			// ISchemaInfo
			bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);
		}

	}

	/**
	 * MockDaoModule
	 * @author jpk
	 */
	private static class MockDaoModule extends AbstractDaoModule {

		/**
		 * Constructor
		 */
		public MockDaoModule() {
			super();
			log.info("Employing MOCK Dao");
		}

		protected static abstract class MockDaoProvider<D extends IDao> implements Provider<D> {

			@Inject
			EntityGraph entityGraph;
		}

		@Override
		protected void configure() {
			super.configure();

			// IPrimaryKeyGenerator
			bind(IPrimaryKeyGenerator.class).to(MockPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			bind(MockEntityProvider.class).in(Scopes.SINGLETON);
			bind(EntityGraph.class).in(Scopes.SINGLETON);

			bind(IAccountAddressDao.class).toProvider(new MockDaoProvider<IAccountAddressDao>() {

				@Override
				public IAccountAddressDao get() {
					return new com.tll.dao.impl.mock.AccountAddressDao(entityGraph.getEntitySet(AccountAddress.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IAccountDao.class).toProvider(new MockDaoProvider<IAccountDao>() {

				@Override
				public IAccountDao get() {
					return new com.tll.dao.impl.mock.AccountDao(entityGraph.getEntitySet(Account.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IAccountHistoryDao.class).toProvider(new MockDaoProvider<IAccountHistoryDao>() {

				@Override
				public IAccountHistoryDao get() {
					return new com.tll.dao.impl.mock.AccountHistoryDao(entityGraph.getEntitySet(AccountHistory.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IAclDao.class).toProvider(new MockDaoProvider<IAclDao>() {

				@Override
				public IAclDao get() {
					return new AclDao();
				}

			}).in(Scopes.SINGLETON);
			bind(IAddressDao.class).toProvider(new MockDaoProvider<IAddressDao>() {

				@Override
				public IAddressDao get() {
					return new com.tll.dao.impl.mock.AddressDao(entityGraph.getEntitySet(Address.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IAppPropertyDao.class).toProvider(new MockDaoProvider<IAppPropertyDao>() {

				@Override
				public IAppPropertyDao get() {
					return new com.tll.dao.impl.mock.AppPropertyDao(entityGraph.getEntitySet(AppProperty.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IAuthorityDao.class).toProvider(new MockDaoProvider<IAuthorityDao>() {

				@Override
				public IAuthorityDao get() {
					return new com.tll.dao.impl.mock.AuthorityDao(entityGraph.getEntitySet(Authority.class));
				}

			}).in(Scopes.SINGLETON);
			bind(ICurrencyDao.class).toProvider(new MockDaoProvider<ICurrencyDao>() {

				@Override
				public ICurrencyDao get() {
					return new com.tll.dao.impl.mock.CurrencyDao(entityGraph.getEntitySet(Currency.class));
				}

			}).in(Scopes.SINGLETON);
			bind(ICustomerAccountDao.class).toProvider(new MockDaoProvider<ICustomerAccountDao>() {

				@Override
				public ICustomerAccountDao get() {
					return new com.tll.dao.impl.mock.CustomerAccountDao(entityGraph.getEntitySet(CustomerAccount.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IInterfaceDao.class).toProvider(new MockDaoProvider<IInterfaceDao>() {

				@Override
				public IInterfaceDao get() {
					return new com.tll.dao.impl.mock.InterfaceDao(entityGraph.getEntitySet(Interface.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IInterfaceOptionAccountDao.class).toProvider(new MockDaoProvider<IInterfaceOptionAccountDao>() {

				@Override
				public IInterfaceOptionAccountDao get() {
					return new com.tll.dao.impl.mock.InterfaceOptionAccountDao(entityGraph
							.getEntitySet(InterfaceOptionAccount.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IOrderDao.class).toProvider(new MockDaoProvider<IOrderDao>() {

				@Override
				public IOrderDao get() {
					return new com.tll.dao.impl.mock.OrderDao(entityGraph.getEntitySet(Order.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IOrderItemDao.class).toProvider(new MockDaoProvider<IOrderItemDao>() {

				@Override
				public IOrderItemDao get() {
					return new com.tll.dao.impl.mock.OrderItemDao(entityGraph.getEntitySet(OrderItem.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IOrderTransDao.class).toProvider(new MockDaoProvider<IOrderTransDao>() {

				@Override
				public IOrderTransDao get() {
					return new com.tll.dao.impl.mock.OrderTransDao(entityGraph.getEntitySet(OrderTrans.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IOrderItemTransDao.class).toProvider(new MockDaoProvider<IOrderItemTransDao>() {

				@Override
				public IOrderItemTransDao get() {
					return new com.tll.dao.impl.mock.OrderItemTransDao(entityGraph.getEntitySet(OrderItemTrans.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IPaymentInfoDao.class).toProvider(new MockDaoProvider<IPaymentInfoDao>() {

				@Override
				public IPaymentInfoDao get() {
					return new com.tll.dao.impl.mock.PaymentInfoDao(entityGraph.getEntitySet(PaymentInfo.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IPaymentTransDao.class).toProvider(new MockDaoProvider<IPaymentTransDao>() {

				@Override
				public IPaymentTransDao get() {
					return new com.tll.dao.impl.mock.PaymentTransDao(entityGraph.getEntitySet(PaymentTrans.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IPCHDao.class).toProvider(new MockDaoProvider<IPCHDao>() {

				@Override
				public IPCHDao get() {
					return new com.tll.dao.impl.mock.PCHDao();
				}

			}).in(Scopes.SINGLETON);
			bind(IProdCatDao.class).toProvider(new MockDaoProvider<IProdCatDao>() {

				@Override
				public IProdCatDao get() {
					return new com.tll.dao.impl.mock.ProdCatDao(entityGraph.getEntitySet(ProdCat.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IProductCategoryDao.class).toProvider(new MockDaoProvider<IProductCategoryDao>() {

				@Override
				public IProductCategoryDao get() {
					return new com.tll.dao.impl.mock.ProductCategoryDao(entityGraph.getEntitySet(ProductCategory.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IProductInventoryDao.class).toProvider(new MockDaoProvider<IProductInventoryDao>() {

				@Override
				public IProductInventoryDao get() {
					return new com.tll.dao.impl.mock.ProductInventoryDao(entityGraph.getEntitySet(ProductInventory.class));
				}

			}).in(Scopes.SINGLETON);
			bind(ISalesTaxDao.class).toProvider(new MockDaoProvider<ISalesTaxDao>() {

				@Override
				public ISalesTaxDao get() {
					return new com.tll.dao.impl.mock.SalesTaxDao(entityGraph.getEntitySet(SalesTax.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IShipBoundCostDao.class).toProvider(new MockDaoProvider<IShipBoundCostDao>() {

				@Override
				public IShipBoundCostDao get() {
					return new com.tll.dao.impl.mock.ShipBoundCostDao(entityGraph.getEntitySet(ShipBoundCost.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IShipModeDao.class).toProvider(new MockDaoProvider<IShipModeDao>() {

				@Override
				public IShipModeDao get() {
					return new com.tll.dao.impl.mock.ShipModeDao(entityGraph.getEntitySet(ShipMode.class));
				}

			}).in(Scopes.SINGLETON);
			bind(ISiteCodeDao.class).toProvider(new MockDaoProvider<ISiteCodeDao>() {

				@Override
				public ISiteCodeDao get() {
					return new com.tll.dao.impl.mock.SiteCodeDao(entityGraph.getEntitySet(SiteCode.class));
				}

			}).in(Scopes.SINGLETON);
			bind(ISiteStatisticsDao.class).toProvider(new MockDaoProvider<ISiteStatisticsDao>() {

				@Override
				public ISiteStatisticsDao get() {
					return new com.tll.dao.impl.mock.SiteStatisticsDao();
				}

			}).in(Scopes.SINGLETON);
			bind(IUserDao.class).toProvider(new MockDaoProvider<IUserDao>() {

				@Override
				public IUserDao get() {
					return new com.tll.dao.impl.mock.UserDao(entityGraph.getEntitySet(User.class));
				}

			}).in(Scopes.SINGLETON);
			bind(IVisitorDao.class).toProvider(new MockDaoProvider<IVisitorDao>() {

				@Override
				public IVisitorDao get() {
					return new com.tll.dao.impl.mock.VisitorDao(entityGraph.getEntitySet(Visitor.class));
				}

			}).in(Scopes.SINGLETON);

			bind(IDaoFactory.class).toProvider(new DaoFactoryProvider());

		}

	}

	/**
	 * HibernateDaoModule
	 * @author jpk
	 */
	private static class HibernateDaoModule extends AbstractDaoModule {

		/**
		 * Constructor
		 */
		public HibernateDaoModule() {
			super();
			log.info("Employing ORM Dao");
		}

		@Override
		protected void configure() {
			super.configure();

			// IPrimaryKeyGenerator
			bind(IPrimaryKeyGenerator.class).to(PrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			// IComparatorTranslator
			bind(new TypeLiteral<IComparatorTranslator<Criterion>>() {}).to(ComparatorTranslator.class).in(Scopes.SINGLETON);

			// IDbDialectHandler
			bind(IDbDialectHandler.class).to(MySqlDialectHandler.class).in(Scopes.SINGLETON);

			bind(IAccountAddressDao.class).to(AccountAddressDao.class).in(Scopes.SINGLETON);
			bind(IAccountDao.class).to(AccountDao.class).in(Scopes.SINGLETON);
			bind(IAccountHistoryDao.class).to(AccountHistoryDao.class).in(Scopes.SINGLETON);
			bind(IAclDao.class).to(AclDao.class).in(Scopes.SINGLETON);
			bind(IAddressDao.class).to(AddressDao.class).in(Scopes.SINGLETON);
			bind(IAppPropertyDao.class).to(AppPropertyDao.class).in(Scopes.SINGLETON);
			bind(IAuthorityDao.class).to(AuthorityDao.class).in(Scopes.SINGLETON);
			bind(ICurrencyDao.class).to(CurrencyDao.class).in(Scopes.SINGLETON);
			bind(ICustomerAccountDao.class).to(CustomerAccountDao.class).in(Scopes.SINGLETON);
			bind(IInterfaceDao.class).to(InterfaceDao.class).in(Scopes.SINGLETON);
			bind(IInterfaceOptionAccountDao.class).to(InterfaceOptionAccountDao.class).in(Scopes.SINGLETON);
			bind(IOrderDao.class).to(OrderDao.class).in(Scopes.SINGLETON);
			bind(IOrderItemDao.class).to(OrderItemDao.class).in(Scopes.SINGLETON);
			bind(IOrderTransDao.class).to(OrderTransDao.class).in(Scopes.SINGLETON);
			bind(IOrderItemTransDao.class).to(OrderItemTransDao.class).in(Scopes.SINGLETON);
			bind(IPaymentInfoDao.class).to(PaymentInfoDao.class).in(Scopes.SINGLETON);
			bind(IPaymentTransDao.class).to(PaymentTransDao.class).in(Scopes.SINGLETON);
			bind(IPCHDao.class).to(PCHDao.class).in(Scopes.SINGLETON);
			bind(IProdCatDao.class).to(ProdCatDao.class).in(Scopes.SINGLETON);
			bind(IProductCategoryDao.class).to(ProductCategoryDao.class).in(Scopes.SINGLETON);
			bind(IProductInventoryDao.class).to(ProductInventoryDao.class).in(Scopes.SINGLETON);
			bind(ISalesTaxDao.class).to(SalesTaxDao.class).in(Scopes.SINGLETON);
			bind(IShipBoundCostDao.class).to(ShipBoundCostDao.class).in(Scopes.SINGLETON);
			bind(IShipModeDao.class).to(ShipModeDao.class).in(Scopes.SINGLETON);
			bind(ISiteCodeDao.class).to(SiteCodeDao.class).in(Scopes.SINGLETON);
			bind(ISiteStatisticsDao.class).to(SiteStatisticsDao.class).in(Scopes.SINGLETON);
			bind(IUserDao.class).to(UserDao.class).in(Scopes.SINGLETON);
			bind(IVisitorDao.class).to(VisitorDao.class).in(Scopes.SINGLETON);

			// IDaoFactory impl
			bind(IDaoFactory.class).toProvider(new DaoFactoryProvider());
		}

	}

	/**
	 * DaoFactoryProvider
	 * @author jpk
	 */
	protected static class DaoFactoryProvider implements Provider<IDaoFactory> {

		@Inject
		IAccountAddressDao aad;
		@Inject
		IAccountDao ad;
		@Inject
		IAccountHistoryDao ahd;
		@Inject
		IAclDao acld;
		@Inject
		IAddressDao add;
		@Inject
		IAppPropertyDao apd;
		@Inject
		IAuthorityDao aud;
		@Inject
		ICurrencyDao cd;
		@Inject
		ICustomerAccountDao cad;
		@Inject
		IInterfaceDao id;
		@Inject
		IInterfaceOptionAccountDao ioad;
		@Inject
		IOrderDao od;
		@Inject
		IOrderItemDao oid;
		@Inject
		IOrderTransDao otd;
		@Inject
		IOrderItemTransDao oitd;
		@Inject
		IPaymentInfoDao pid;
		@Inject
		IPaymentTransDao ptd;
		@Inject
		IPCHDao pchd;
		@Inject
		IProdCatDao pcd;
		@Inject
		IProductCategoryDao pcatd;
		@Inject
		IProductInventoryDao pinvd;
		@Inject
		ISalesTaxDao std;
		@Inject
		IShipBoundCostDao sbcd;
		@Inject
		IShipModeDao smd;
		@Inject
		ISiteCodeDao scd;
		@Inject
		ISiteStatisticsDao ssd;
		@Inject
		IUserDao ud;
		@Inject
		IVisitorDao vd;

		Map<Class<? extends IDao>, IDao> map;
		IDaoFactory daoFactory;

		public IDaoFactory get() {
			if(daoFactory == null) {
				map = new HashMap<Class<? extends IDao>, IDao>();
				map.put(IAccountAddressDao.class, aad);
				map.put(IAccountDao.class, ad);
				map.put(IAccountHistoryDao.class, ahd);
				map.put(IAclDao.class, acld);
				map.put(IAddressDao.class, add);
				map.put(IAppPropertyDao.class, apd);
				map.put(IAuthorityDao.class, aud);
				map.put(ICurrencyDao.class, cd);
				map.put(ICustomerAccountDao.class, cad);
				map.put(IInterfaceDao.class, id);
				map.put(IInterfaceOptionAccountDao.class, ioad);
				map.put(IOrderDao.class, od);
				map.put(IOrderItemDao.class, oid);
				map.put(IOrderTransDao.class, otd);
				map.put(IOrderItemTransDao.class, oitd);
				map.put(IPaymentInfoDao.class, pid);
				map.put(IPaymentTransDao.class, ptd);
				map.put(IPCHDao.class, pchd);
				map.put(IProdCatDao.class, pcd);
				map.put(IProductCategoryDao.class, pcatd);
				map.put(IProductInventoryDao.class, pinvd);
				map.put(ISalesTaxDao.class, std);
				map.put(IShipBoundCostDao.class, sbcd);
				map.put(IShipModeDao.class, smd);
				map.put(ISiteCodeDao.class, scd);
				map.put(ISiteStatisticsDao.class, ssd);
				map.put(IUserDao.class, ud);
				map.put(IVisitorDao.class, vd);
				daoFactory = new DaoFactory(map);
			}
			return daoFactory;
		}
	}
}
