/*
 * The Logic Lab
 */
package com.tll.di;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.IEntity;
import com.tll.service.entity.AccountService;
import com.tll.service.entity.AddressService;
import com.tll.service.entity.EntityServiceFactory;
import com.tll.service.entity.IAccountService;
import com.tll.service.entity.IAddressService;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * EntityServiceFactoryModule
 * @author jpk
 */
public class EntityServiceFactoryModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(EntityServiceFactoryModule.class);

	@Override
	protected void configure() {
		log.info("Employing Entity service module");
		bind(IAddressService.class).to(AddressService.class).in(Scopes.SINGLETON);
		bind(IAccountService.class).to(AccountService.class).in(Scopes.SINGLETON);

		// EntityServiceFactory
		bind(IEntityServiceFactory.class).toProvider(new Provider<IEntityServiceFactory>() {

			@Inject
			IAccountService accs;
			@Inject
			IAddressService adrs;

			public IEntityServiceFactory get() {
				final Map<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>> map =
					new HashMap<Class<? extends IEntityService<? extends IEntity>>, IEntityService<? extends IEntity>>();

				map.put(IAccountService.class, accs);
				map.put(IAddressService.class, adrs);

				return new EntityServiceFactory(map);
			}

		}).in(Scopes.SINGLETON);

	}

}
