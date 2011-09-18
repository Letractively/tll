package com.tll.server.listing.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.ListableBeanFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.model.egraph.EntityGraph;
import com.tll.model.test.Address;
import com.tll.model.test.TestEntityFactory;
import com.tll.server.LogExceptionHandlerModule;
import com.tll.server.listing.IRowListHandlerProvider;
import com.tll.server.listing.ListingCache.ListingCacheAware;

/**
 * @author jpk
 */
public class TestListingModule extends AbstractModule {

	@Override
	protected void configure() {

		// IEntityFactory<Long>
		bind(IEntityFactory.class).toProvider(new Provider<IEntityFactory>() {

			@Override
			public IEntityFactory get() {
				return new TestEntityFactory();
			}
		}).in(Scopes.SINGLETON);

		// ListableBeanFactory
		bind(ListableBeanFactory.class).toProvider(new Provider<ListableBeanFactory>() {

			@Override
			public ListableBeanFactory get() {
				try {
					URI uri = new URI("com/tll/model/test/mock-entities.xml");
					return EntityBeanFactory.loadBeanDefinitions(uri);
				}
				catch(URISyntaxException e) {
					throw new RuntimeException(e);
				}
			}
		}).in(Scopes.SINGLETON);

		// IEntityMetadata
		bind(IEntityMetadata.class).to(EntityMetadata.class).in(Scopes.SINGLETON);

		// EntityGraph
		bind(EntityGraph.class).toProvider(new Provider<EntityGraph>() {

			@Inject
			EntityBeanFactory ebf;

			@Inject
			IEntityMetadata emd;

			@Inject
			BusinessKeyFactory bkf;

			@Override
			public EntityGraph get() {
				Collection<Address> addresses = ebf.getNEntityCopies(Address.class, 500);
				EntityGraph eg = new EntityGraph(emd, bkf);
				try {
					eg.setEntities(addresses, true);
				}
				catch(Exception e) {
					throw new RuntimeException(e);
				}
				return eg;
			}
		}).in(Scopes.SINGLETON);

		// CacheManager
		bind(CacheManager.class).annotatedWith(ListingCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				final URL url = Thread.currentThread().getContextClassLoader().getResource("ehcache.xml");
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);

		// IRowListHandlerProvider
		bind(IRowListHandlerProvider.class).to(TestRowListHandlerProvider.class).in(Scopes.SINGLETON);
		
		install(new LogExceptionHandlerModule());
	}
}
