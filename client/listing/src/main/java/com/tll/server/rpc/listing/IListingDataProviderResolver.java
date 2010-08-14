/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.data.rpc.ListingRequest;
import com.tll.listhandler.IListingDataProvider;


/**
 * IListingDataProviderResolver
 * @author jpk
 */
public interface IListingDataProviderResolver {

	/**
	 * Resolves a listing data provider from a listing request.
	 * @param request the listing request
	 * @return the resolved data provider
	 * @throws IllegalArgumentException when the data provider can't be resolved
	 */
	IListingDataProvider resolve(ListingRequest request)
	throws IllegalArgumentException;
}
