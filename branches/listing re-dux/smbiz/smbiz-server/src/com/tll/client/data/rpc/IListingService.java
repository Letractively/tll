/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.search.ISearch;

/**
 * IListingService - Handles {@link ListingRequest}s.
 * @author jpk
 */
public interface IListingService<S extends ISearch> extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	ListingPayload process(ListingRequest<S> listingRequest);
}
