/**
 * The Logic Lab
 * @author jpk
 * @since Oct 4, 2009
 */
package com.tll.di.test;

import net.sf.ehcache.CacheManager;

import com.tll.di.ClientPersistModule;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.PersistCache.PersistCacheAware;
import com.tll.server.rpc.entity.test.TestPersistServiceImplResolver;

/**
 * TestClientPersistModule
 * @author jpk
 */
public class TestClientPersistModule extends ClientPersistModule {

	@Override
	protected Class<? extends IPersistServiceImplResolver> getPersistServiceImplResolverType() {
		return TestPersistServiceImplResolver.class;
	}

	@Override
	protected void configure() {
		bind(CacheManager.class).annotatedWith(PersistCacheAware.class).toInstance(CacheManager.create());
		super.configure();
	}

}
