/*
 * The Logic Lab
 */
package com.tll.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.providers.dao.UserCache;
import org.springframework.security.providers.dao.cache.EhCacheBasedUserCache;
import org.springframework.security.userdetails.UserDetails;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.IEntity;
import com.tll.service.entity.EntityServiceFactory;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.account.AccountService;
import com.tll.service.entity.account.AddAccountService;
import com.tll.service.entity.account.CustomerAccountService;
import com.tll.service.entity.account.IAccountService;
import com.tll.service.entity.account.ICustomerAccountService;
import com.tll.service.entity.address.AddressService;
import com.tll.service.entity.address.IAddressService;
import com.tll.service.entity.app.AppPropertyService;
import com.tll.service.entity.app.IAppPropertyService;
import com.tll.service.entity.currency.CurrencyService;
import com.tll.service.entity.currency.ICurrencyService;
import com.tll.service.entity.intf.IInterfaceService;
import com.tll.service.entity.intf.InterfaceService;
import com.tll.service.entity.order.IOrderService;
import com.tll.service.entity.order.OrderService;
import com.tll.service.entity.product.IProdCatService;
import com.tll.service.entity.product.IProductCategoryService;
import com.tll.service.entity.product.IProductService;
import com.tll.service.entity.product.ProdCatService;
import com.tll.service.entity.product.ProductCategoryService;
import com.tll.service.entity.product.ProductService;
import com.tll.service.entity.pymnt.IPaymentInfoService;
import com.tll.service.entity.pymnt.IPaymentTransService;
import com.tll.service.entity.pymnt.PaymentInfoService;
import com.tll.service.entity.pymnt.PaymentTransService;
import com.tll.service.entity.salestax.ISalesTaxService;
import com.tll.service.entity.salestax.SalesTaxService;
import com.tll.service.entity.ship.IShipBoundCostService;
import com.tll.service.entity.ship.IShipModeService;
import com.tll.service.entity.ship.ShipBoundCostService;
import com.tll.service.entity.ship.ShipModeService;
import com.tll.service.entity.sitecode.ISiteCodeService;
import com.tll.service.entity.sitecode.SiteCodeService;
import com.tll.service.entity.user.AuthorityService;
import com.tll.service.entity.user.IAuthorityService;
import com.tll.service.entity.user.IUserService;
import com.tll.service.entity.user.UserService;
import com.tll.service.entity.visitor.IVisitorService;
import com.tll.service.entity.visitor.VisitorService;

/**
 * SmbizEntityServiceFactoryModule
 * <p>
 * <b>NOTE: </b>This module depends on the presence of a {@link CacheManager}
 * with user cache regions.
 * @author jpk
 */
public class SmbizEntityServiceFactoryModule extends AbstractModule {

	static final Log log = LogFactory.getLog(SmbizEntityServiceFactoryModule.class);

	/**
	 * UserCacheAware<br>
	 * Annotation indicating a {@link CacheManager} instance that supports {@link UserDetails} caching.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
		@BindingAnnotation
		public @interface UserCacheAware {
	}

	public static final String USER_DETAILS_CACHE_NAME = "acegiUserDetailsCache";

	@Override
	protected void configure() {
		log.info("Employing Entity service module");

		// UserCache (userCache)
		bind(UserCache.class).toProvider(new Provider<UserCache>() {

			@Inject
			@UserCacheAware
			CacheManager cm;

			@Override
			public UserCache get() {
				final EhCacheBasedUserCache uc = new EhCacheBasedUserCache();
				final Cache userDetailsCache = cm.getCache(USER_DETAILS_CACHE_NAME);
				if(userDetailsCache == null) throw new IllegalStateException("No user details cache found");
				uc.setCache(userDetailsCache);
				return uc;
			}

		}).in(Scopes.SINGLETON);

		bind(IAuthorityService.class).to(AuthorityService.class).in(Scopes.SINGLETON);
		bind(IAccountService.class).to(AccountService.class).in(Scopes.SINGLETON);
		bind(ICustomerAccountService.class).to(CustomerAccountService.class).in(Scopes.SINGLETON);
		bind(IAddressService.class).to(AddressService.class).in(Scopes.SINGLETON);
		bind(IAppPropertyService.class).to(AppPropertyService.class).in(Scopes.SINGLETON);
		bind(ICurrencyService.class).to(CurrencyService.class).in(Scopes.SINGLETON);
		bind(IInterfaceService.class).to(InterfaceService.class).in(Scopes.SINGLETON);
		bind(IOrderService.class).to(OrderService.class).in(Scopes.SINGLETON);
		bind(IProdCatService.class).to(ProdCatService.class).in(Scopes.SINGLETON);
		bind(IProductCategoryService.class).to(ProductCategoryService.class).in(Scopes.SINGLETON);
		bind(IProductService.class).to(ProductService.class).in(Scopes.SINGLETON);
		bind(IPaymentInfoService.class).to(PaymentInfoService.class).in(Scopes.SINGLETON);
		bind(IPaymentTransService.class).to(PaymentTransService.class).in(Scopes.SINGLETON);
		bind(ISalesTaxService.class).to(SalesTaxService.class).in(Scopes.SINGLETON);
		bind(IShipBoundCostService.class).to(ShipBoundCostService.class).in(Scopes.SINGLETON);
		bind(IShipModeService.class).to(ShipModeService.class).in(Scopes.SINGLETON);
		bind(ISiteCodeService.class).to(SiteCodeService.class).in(Scopes.SINGLETON);
		bind(IUserService.class).to(UserService.class).in(Scopes.SINGLETON);
		bind(IVisitorService.class).to(VisitorService.class).in(Scopes.SINGLETON);

		// add account service
		bind(AddAccountService.class).in(Scopes.SINGLETON);

		// EntityServiceFactory
		bind(IEntityServiceFactory.class).toProvider(new Provider<IEntityServiceFactory>() {

			@Inject
			IAuthorityService auths;
			@Inject
			IAccountService accs;
			@Inject
			ICustomerAccountService cas;
			@Inject
			IAddressService adrs;
			@Inject
			IAppPropertyService aps;
			@Inject
			ICurrencyService cs;
			@Inject
			IInterfaceService is;
			@Inject
			IOrderService os;
			@Inject
			IProdCatService pcs;
			@Inject
			IProductCategoryService pcats;
			@Inject
			IProductService ps;
			@Inject
			IPaymentInfoService pis;
			@Inject
			IPaymentTransService pts;
			@Inject
			ISalesTaxService sts;
			@Inject
			IShipBoundCostService sbcs;
			@Inject
			IShipModeService sms;
			@Inject
			ISiteCodeService scs;
			@Inject
			IUserService us;
			@Inject
			IVisitorService vs;

			public IEntityServiceFactory get() {
				final Map<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>> map =
					new HashMap<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>>();

				map.put(IAuthorityService.class, auths);
				map.put(IAccountService.class, accs);
				map.put(ICustomerAccountService.class, cas);
				map.put(IAppPropertyService.class, aps);
				map.put(IAddressService.class, adrs);
				map.put(ICurrencyService.class, cs);
				map.put(IInterfaceService.class, is);
				map.put(IOrderService.class, os);
				map.put(IProdCatService.class, pcs);
				map.put(IProductCategoryService.class, pcats);
				map.put(IProductService.class, ps);
				map.put(IPaymentInfoService.class, pis);
				map.put(IPaymentTransService.class, pts);
				map.put(ISalesTaxService.class, sts);
				map.put(IShipBoundCostService.class, sbcs);
				map.put(IShipModeService.class, sms);
				map.put(ISiteCodeService.class, scs);
				map.put(IUserService.class, us);
				map.put(IVisitorService.class, vs);

				return new EntityServiceFactory(map);
			}

		}).in(Scopes.SINGLETON);
	}
}
