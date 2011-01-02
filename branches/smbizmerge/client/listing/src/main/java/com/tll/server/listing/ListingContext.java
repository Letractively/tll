/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.listing;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.server.IExceptionHandler;

/**
 * @author jpk
 */
public class ListingContext {

	/**
	 * The key identifying the {@link ListingContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(-2283719863396426465L);

	private final IRowListHandlerProvider rowListHandlerProvider;
	private final IExceptionHandler exceptionHandler;
	private final ListingCache listingCache;

	/**
	 * Constructor
	 * @param rowListHandlerProvider
	 * @param exceptionHandler
	 * @param listingCache
	 */
	@Inject
	public ListingContext(IRowListHandlerProvider rowListHandlerProvider, IExceptionHandler exceptionHandler,
			ListingCache listingCache) {
		super();
		this.rowListHandlerProvider = rowListHandlerProvider;
		this.exceptionHandler = exceptionHandler;
		this.listingCache = listingCache;
	}

	public IRowListHandlerProvider getRowListHandlerProvider() {
		return rowListHandlerProvider;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public ListingCache getListingCache() {
		return listingCache;
	}
}
