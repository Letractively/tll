/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.ListingPayload;

/**
 * IListingServiceAsync
 * @author jpk
 */
public interface IListingServiceAsync {

	void process(IListingCommand listingCommand, AsyncCallback<ListingPayload> callback);
}
