/**
 * The Logic Lab
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.IMarshalable;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.search.ISearch;

/**
 * IListingServiceAsync
 * @author jpk
 * @param <S> the search type
 * @param <R> the row type
 */
public interface IListingServiceAsync<S extends ISearch, R extends IMarshalable> {

	void process(ListingRequest<S> listingCommand, AsyncCallback<ListingPayload<R>> callback);
}
