/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.IMarshalable;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;

/**
 * IListingService - Handles {@link ListingRequest}s.
 * @param <R> The row data type
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/listing")
public interface IListingService<R extends IMarshalable> extends RemoteService {

	/**
	 * Processes a listing request.
	 * @param listingRequest The listing request
	 * @return ListingPayload The listing response
	 */
	ListingPayload<R> process(ListingRequest listingRequest);
}
