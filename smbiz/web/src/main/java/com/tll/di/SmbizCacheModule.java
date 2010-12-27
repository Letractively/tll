/**
 * The Logic Lab
 * @author jpk
 * @since Oct 24, 2009
 */
package com.tll.di;

import java.net.URL;

import net.sf.ehcache.CacheManager;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.server.listing.ListingCache.ListingCacheAware;
import com.tll.server.rpc.entity.PersistCache.PersistCacheAware;
import com.tll.service.entity.SmbizEntityServiceFactoryModule.UserCacheAware;
import com.tll.util.ClassUtil;


/**
 * SmbizCacheModule
 * @author jpk
 */
public class SmbizCacheModule extends AbstractModule {

	private static final String EHCACHE_PERSIST_FILENAME = "ehcache-smbiz-persist.xml";

	private static final String EHCACHE_WEB_FILENAME = "ehcache-smbiz-web.xml";

	@Override
	protected void configure() {

		bind(CacheManager.class).annotatedWith(ListingCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				final URL url = ClassUtil.getResource(EHCACHE_WEB_FILENAME);
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);

		bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				final URL url = ClassUtil.getResource(EHCACHE_PERSIST_FILENAME);
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);

		bind(CacheManager.class).annotatedWith(PersistCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Inject @UserCacheAware
			CacheManager cm;

			@Override
			public CacheManager get() {
				return cm;
			}
		}).in(Scopes.SINGLETON);
	}

}
