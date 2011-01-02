/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.IMarshalable;

/**
 * IListingService - Handles {@link ListingRequest}s.
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/listing")
public interface IListingService extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param <S> listing search type
	 * @param <R> The row data type
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	<S extends IMarshalable, R extends IMarshalable> ListingPayload<R> process(ListingRequest<S> listingRequest);
}
