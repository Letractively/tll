/**
 * The Logic Lab
 * @author jpk
 * @since Oct 23, 2009
 */
package com.tll.di;

import java.net.URL;

import net.sf.ehcache.CacheManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;


/**
 * TestCacheModule
 * @author jpk
 */
public class TestCacheModule extends AbstractModule {

	private static String DEFAULT_EHCACHE_FILENAME = "ehcache.xml";

	private final String ehcacheFilename;

	/**
	 * Constructor
	 */
	public TestCacheModule() {
		this(DEFAULT_EHCACHE_FILENAME);
	}

	/**
	 * Constructor
	 * @param ehcacheFilename
	 */
	public TestCacheModule(String ehcacheFilename) {
		super();
		this.ehcacheFilename = ehcacheFilename;
	}

	@Override
	protected void configure() {
		bind(CacheManager.class).toProvider(new Provider<CacheManager>() {

			@SuppressWarnings("synthetic-access")
			@Override
			public CacheManager get() {
				final URL url = Thread.currentThread().getContextClassLoader().getResource(ehcacheFilename);
				return new CacheManager(url);
			}
		}).in(Scopes.SINGLETON);
	}

}
