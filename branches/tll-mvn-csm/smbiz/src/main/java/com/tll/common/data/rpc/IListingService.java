/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.IMarshalable;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.search.ISearch;

/**
 * IListingService - Handles {@link ListingRequest}s.
 * @param <S> The search type
 * @param <R> The row data type
 * @author jpk
 */
public interface IListingService<S extends ISearch, R extends IMarshalable> extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	ListingPayload<R> process(ListingRequest<S> listingRequest);
}
