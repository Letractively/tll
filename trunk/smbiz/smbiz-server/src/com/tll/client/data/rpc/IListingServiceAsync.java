/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.ListingPayload;
import com.tll.client.search.ISearch;

/**
 * IListingServiceAsync
 * @author jpk
 */
public interface IListingServiceAsync {

	void process(IListingCommand<? extends ISearch> listingCommand, AsyncCallback<ListingPayload> callback);
}
