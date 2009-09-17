/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.IExceptionHandler;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;

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
	private final Marshaler marshaler;
	private final IMarshalOptionsResolver marshalOptionsResolver;
	private final INamedQueryResolver namedQueryResolver;
	private final IExceptionHandler exceptionHandler;

	/**
	 * Constructor
	 * @param searchTranslator
	 * @param listingDataProviderResolver
	 * @param marshaler
	 * @param marshalOptionsResolver
	 * @param namedQueryResolver
	 * @param exceptionHandler
	 */
	@Inject
	public ListingContext(IListingSearchTranslator searchTranslator,
			IListingDataProviderResolver listingDataProviderResolver, Marshaler marshaler,
			IMarshalOptionsResolver marshalOptionsResolver, INamedQueryResolver namedQueryResolver,
			IExceptionHandler exceptionHandler) {
		super();
		this.searchTranslator = searchTranslator;
		this.listingDataProviderResolver = listingDataProviderResolver;
		this.marshaler = marshaler;
		this.marshalOptionsResolver = marshalOptionsResolver;
		this.namedQueryResolver = namedQueryResolver;
		this.exceptionHandler = exceptionHandler;
	}

	public IListingSearchTranslator getSearchTranslator() {
		return searchTranslator;
	}

	public IListingDataProviderResolver getListingDataProviderResolver() {
		return listingDataProviderResolver;
	}

	public Marshaler getMarshaler() {
		return marshaler;
	}

	public IMarshalOptionsResolver getMarshalOptionsResolver() {
		return marshalOptionsResolver;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public INamedQueryResolver getNamedQueryResolver() {
		return namedQueryResolver;
	}

}
