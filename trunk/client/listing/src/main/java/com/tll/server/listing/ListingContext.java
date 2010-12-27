/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.listing;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.IMarshalable;
import com.tll.server.IExceptionHandler;

/**
 * @param <S> listing search criteria
 * @param <R> row data type
 * @author jpk
 */
public class ListingContext<S extends IMarshalable, R extends IMarshalable> {

	/**
	 * The key identifying the {@link ListingContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(-2283719863396426465L);

	private final IRowListHandlerProvider<S, R> rowListHandlerProvider;
	private final IExceptionHandler exceptionHandler;
	private final ListingCache listingCache;

	/**
	 * Constructor
	 * @param rowListHandlerProvider
	 * @param exceptionHandler
	 * @param listingCache
	 */
	@Inject
	public ListingContext(IRowListHandlerProvider<S, R> rowListHandlerProvider, IExceptionHandler exceptionHandler,
			ListingCache listingCache) {
		super();
		this.rowListHandlerProvider = rowListHandlerProvider;
		this.exceptionHandler = exceptionHandler;
		this.listingCache = listingCache;
	}

	public IRowListHandlerProvider<S, R> getRowListHandlerProvider() {
		return rowListHandlerProvider;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public ListingCache getListingCache() {
		return listingCache;
	}
}
