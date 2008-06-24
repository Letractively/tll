/**
 * The Logic Lab
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.model.IEntity;

/**
 * IListingServiceAsync
 * @author jpk
 */
public interface IListingServiceAsync<E extends IEntity> {

	void process(ListingRequest<E> listingCommand, AsyncCallback<ListingPayload> callback);
}
