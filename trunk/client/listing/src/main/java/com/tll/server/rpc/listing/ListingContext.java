/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.server.rpc.IExceptionHandler;

/**
 * ListingContext
 * @author jpk
 */
public class ListingContext {

	/**
	 * The key identifying the {@link ListingContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(-2283719863396426465L);

	private final IListingSearchTranslator searchTranslator;
	private final IListingDataProviderResolver listingDataProviderResolver;
	private final INamedQueryResolver namedQueryResolver;
	private final IExceptionHandler exceptionHandler;
	private final ListingCache listingCache;

	/**
	 * Constructor
	 * @param searchTranslator
	 * @param listingDataProviderResolver
	 * @param namedQueryResolver
	 * @param exceptionHandler
	 * @param listingCache
	 */
	@Inject
	public ListingContext(IListingSearchTranslator searchTranslator,
			IListingDataProviderResolver listingDataProviderResolver, INamedQueryResolver namedQueryResolver,
			IExceptionHandler exceptionHandler, ListingCache listingCache) {
		super();
		this.searchTranslator = searchTranslator;
		this.listingDataProviderResolver = listingDataProviderResolver;
		this.namedQueryResolver = namedQueryResolver;
		this.exceptionHandler = exceptionHandler;
		this.listingCache = listingCache;
	}

	public IListingSearchTranslator getSearchTranslator() {
		return searchTranslator;
	}

	public IListingDataProviderResolver getListingDataProviderResolver() {
		return listingDataProviderResolver;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public INamedQueryResolver getNamedQueryResolver() {
		return namedQueryResolver;
	}

	public ListingCache getListingCache() {
		return listingCache;
	}
}
