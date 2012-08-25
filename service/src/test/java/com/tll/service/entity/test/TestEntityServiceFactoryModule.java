/*
 * The Logic Lab
 */
package com.tll.service.entity.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.service.IService;
import com.tll.service.entity.EntityServiceFactory;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * TestEntityServiceFactoryModule
 * @author jpk
 */
public class TestEntityServiceFactoryModule extends AbstractModule {

	private static final Logger log = LoggerFactory.getLogger(TestEntityServiceFactoryModule.class);

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

			@Override
			public IEntityServiceFactory get() {
				final Map<Class<? extends IService>, IService> map =
					new HashMap<Class<? extends IService>, IService>();

				map.put(IAccountService.class, accs);
				map.put(IAddressService.class, adrs);

				return new EntityServiceFactory(map);
			}

		}).in(Scopes.SINGLETON);

	}

}
