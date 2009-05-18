/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import java.io.Serializable;

import javax.servlet.ServletContext;

import com.google.inject.Inject;

/**
 * ListingContext
 * @author jpk
 */
public class ListingContext implements Serializable {

	private static final long serialVersionUID = -2283719863396426465L;

	/**
	 * The key identifying the {@link ListingContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(serialVersionUID);

	private final IListingDataProviderResolver listingDataProviderResolver;

	/**
	 * Constructor
	 * @param listingDataProviderResolver
	 */
	@Inject
	public ListingContext(IListingDataProviderResolver listingDataProviderResolver) {
		super();
		this.listingDataProviderResolver = listingDataProviderResolver;
	}

	/**
	 * @return the listingDataProviderResolver
	 */
	public IListingDataProviderResolver getListingDataProviderResolver() {
		return listingDataProviderResolver;
	}
}
