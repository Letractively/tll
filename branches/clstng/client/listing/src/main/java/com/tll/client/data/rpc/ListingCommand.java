/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.tll.IMarshalable;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.IListingServiceAsync;
import com.tll.common.search.ISearch;

/**
 * ListingRequest - Issues listing commands to the server.
 * @author jpk
 * @param <S> the search type
 * @param <R> the row element type
 */
@SuppressWarnings("unchecked")
public final class ListingCommand<S extends ISearch, R extends IMarshalable> extends RpcCommand<ListingPayload<R>> {

	private static final IListingServiceAsync<ISearch, IMarshalable> svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
	}
	
	/**
	 * The listing request issued to the server.
	 */
	private final ListingRequest<S> listingRequest;

	/**
	 * Constructor
	 * @param source
	 * @param listingRequest
	 */
	public ListingCommand(Widget source, ListingRequest<S> listingRequest) {
		super(source);
		this.listingRequest = listingRequest;
	}

	@Override
	protected void doExecute() {
		if(listingRequest == null) {
			throw new IllegalStateException("No listing command set!");
		}
		svc.process((ListingRequest) listingRequest, (AsyncCallback) getAsyncCallback());
	}
}
