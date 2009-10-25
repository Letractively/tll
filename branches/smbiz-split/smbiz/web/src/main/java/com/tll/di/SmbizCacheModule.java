/**
 * The Logic Lab
 * @author jpk
 * @since Oct 24, 2009
 */
package com.tll.di;

import java.net.URL;

import net.sf.ehcache.CacheManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.di.ListingModule.ListingCacheAware;
import com.tll.di.SmbizEntityServiceFactoryModule.UserCacheAware;
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
				final URL url = ClassUtil.getRootResourceRef(EHCACHE_WEB_FILENAME);
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);

		bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				final URL url = ClassUtil.getRootResourceRef(EHCACHE_PERSIST_FILENAME);
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);

	}

}
