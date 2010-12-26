/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.IMarshalable;

/**
 * IListingServiceAsync
 * @author jpk
 * @param <R> the row type
 */
public interface IListingServiceAsync<R extends IMarshalable> {

	void process(ListingRequest listingCommand, AsyncCallback<ListingPayload<R>> callback);
}
