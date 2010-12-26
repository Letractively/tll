/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.IMarshalable;

/**
 * IListingServiceAsync
 * @author jpk
 * @param <S> listing search type
 * @param <R> the row type
 */
public interface IListingServiceAsync<S extends IMarshalable, R extends IMarshalable> {

	void process(ListingRequest<S> listingCommand, AsyncCallback<ListingPayload<R>> callback);
}
