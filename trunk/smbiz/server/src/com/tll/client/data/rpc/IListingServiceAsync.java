/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.model.IData;
import com.tll.client.search.ISearch;

/**
 * IListingServiceAsync
 * @author jpk
 */
public interface IListingServiceAsync<S extends ISearch, R extends IData> {

	void process(ListingRequest<S> listingCommand, AsyncCallback<ListingPayload<R>> callback);
}
