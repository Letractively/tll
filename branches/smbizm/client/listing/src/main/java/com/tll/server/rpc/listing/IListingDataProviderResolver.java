/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.data.ListingRequest;
import com.tll.common.search.ISearch;
import com.tll.listhandler.IListingDataProvider;
import com.tll.server.RequestContext;


/**
 * IListingDataProviderResolver
 * @author jpk
 */
public interface IListingDataProviderResolver {

	/**
	 * Resolves a listing data provider from a listing request.
	 * @param requestContext the request context
	 * @param request the listing request
	 * @return the resolved data provider
	 * @throws IllegalArgumentException when the data provider can't be resolved
	 */
	IListingDataProvider resolve(RequestContext requestContext, ListingRequest<? extends ISearch> request)
			throws IllegalArgumentException;
}
