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
 * @param <S> listing search type
 * @param <R> The row data type
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/listing")
public interface IListingService<S extends IMarshalable, R extends IMarshalable> extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	ListingPayload<R> process(ListingRequest<S> listingRequest);
}
