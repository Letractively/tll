/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.server.rpc.listing.IListingDataProviderResolver;
import com.tll.server.rpc.listing.IListingSearchTranslator;
import com.tll.server.rpc.listing.INamedQueryResolver;
import com.tll.server.rpc.listing.ListingCache;

/**
 * ListingModule
 * @author jpk
 */
public abstract class ListingModule extends AbstractModule {

	/**
	 * ListingCacheAware<br>
	 * Annotation indicating a {@link CacheManager} instance that supports listing caching.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
		@BindingAnnotation
		public @interface ListingCacheAware {
	}

	private static final Log log = LogFactory.getLog(ListingModule.class);

	protected Config config;

	/**
	 * Responsible for binding an {@link IListingDataProviderResolver}
	 * implementation.
	 */
	protected abstract Class<? extends IListingDataProviderResolver> getListingDataProviderResolverImplType();

	/**
	 * Responsible for binding an {@link INamedQueryResolver} implementation.
	 */
	protected abstract Class<? extends INamedQueryResolver> getNamedQueryResolverImplType();

	/**
	 * Responsible for binding an {@link IListingSearchTranslator} implementation.
	 */
	protected abstract Class<? extends IListingSearchTranslator> getListingSearchTranslatorImplType();

	@Override
	protected final void configure() {
		log.info("Employing Listing module");

		// ListingCache
		bind(ListingCache.class).toProvider(new Provider<ListingCache>() {

			@Inject
			@ListingCacheAware
			CacheManager cm;

			@Override
			public ListingCache get() {
				return new ListingCache(cm);
			}
		}).in(Scopes.SINGLETON);

		// IListingDataProviderResolver
		bind(IListingDataProviderResolver.class).to(getListingDataProviderResolverImplType()).in(Scopes.SINGLETON);

		// INamedQueryResolver
		bind(INamedQueryResolver.class).to(getNamedQueryResolverImplType()).in(Scopes.SINGLETON);

		// IListingSearchTranslator
		bind(IListingSearchTranslator.class).to(getListingSearchTranslatorImplType()).in(Scopes.SINGLETON);
	}

}
