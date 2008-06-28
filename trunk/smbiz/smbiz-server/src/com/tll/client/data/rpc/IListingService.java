/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.model.IData;
import com.tll.client.search.ISearch;

/**
 * IListingService - Handles {@link ListingRequest}s.
 * @param <S> The search type
 * @param <R> The row data type
 * @author jpk
 */
public interface IListingService<S extends ISearch, R extends IData> extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	ListingPayload<R> process(ListingRequest<S> listingRequest);
}
