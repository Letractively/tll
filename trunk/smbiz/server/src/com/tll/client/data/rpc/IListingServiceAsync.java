/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.IMarshalable;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.search.ISearch;

/**
 * IListingServiceAsync
 * @author jpk
 * @param <S> the search type
 * @param <R> the row type
 */
public interface IListingServiceAsync<S extends ISearch, R extends IMarshalable> {

	void process(ListingRequest<S> listingCommand, AsyncCallback<ListingPayload<R>> callback);
}
