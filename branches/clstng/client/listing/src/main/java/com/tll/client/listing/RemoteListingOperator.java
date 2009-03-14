/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.client.listing;

import com.google.gwt.user.client.ui.Widget;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.common.search.ISearch;
import com.tll.dao.Sorting;

/**
 * RemoteListingOperator
 * @author jpk
 * @param <S> the search type
 * @param <R> the row element type
 */
public final class RemoteListingOperator<S extends ISearch, R extends IMarshalable> extends
		AbstractListingOperator<R>
		implements IRpcHandler<ListingPayload<R>> {

	/**
	 * The unique name that identifies the listing this command targets on the
	 * server.
	 */
	private final String listingName;

	/**
	 * The server-side listing definition.
	 */
	private final RemoteListingDefinition<S> listingDef;

	private transient ListingRequest<S> listingRequest;

	/**
	 * Constructor
	 * @param sourcingWidget the required widget that will serve as the rpc event
	 *        source
	 * @param listingName the identifying listing name for client and server. this
	 *        name <em>must</em> be unique among the others that are presently
	 *        cached server side.
	 * @param listingDef the remote listing definition
	 */
	public RemoteListingOperator(Widget sourcingWidget, String listingName, RemoteListingDefinition<S> listingDef) {
		super(sourcingWidget);
		if(listingName == null || listingDef == null) throw new IllegalArgumentException();
		this.listingName = listingName;
		this.listingDef = listingDef;
	}

	private void execute() {
		assert listingRequest != null;
		final ListingCommand<S, R> cmd = new ListingCommand<S, R>(sourcingWidget, listingRequest);
		cmd.addRpcHandler(this);
		cmd.execute();
	}

	/**
	 * Fetches listing data sending the listing definition in case it isn't cached
	 * server-side.
	 * @param offset The listing index offset
	 * @param sorting The sorting directive
	 * @param refresh Force the listing to be re-queried on the server if it is
	 *        cached?
	 */
	private void fetch(int offset, Sorting sorting, boolean refresh) {
		this.listingRequest =
				new ListingRequest<S>(listingName, listingDef, refresh ? ListingOp.REFRESH : ListingOp.FETCH, offset, sorting);
		execute();
	}

	/**
	 * Fetches listing data against a listing that is presumed to be cached
	 * server-side. If the listing is found not to be cached server-side, the
	 * listing response will indicate this and a subsequent fetch containing the
	 * listing definition will be issued. This is intended to save on network
	 * bandwidth as the case when the server-side listing cache is expired is
	 * assumed to not occur frequently.
	 * @param offset The listing index offset
	 * @param sorting The sorting directive
	 */
	@Override
	protected void doFetch(int offset, Sorting sorting) {
		listingRequest = new ListingRequest<S>(listingName, offset, sorting);
		execute();
	}

	/**
	 * Clear the listing.
	 * @param retainListingState Retain the listing state on the server?
	 */
	private void clear(boolean retainListingState) {
		listingRequest = new ListingRequest<S>(listingName, retainListingState);
		execute();
	}

	@Override
	protected int getPageSize() {
		return listingDef.getPageSize();
	}

	public void refresh() {
		fetch(offset, sorting, true);
	}

	public void clear() {
		clear(true);
	}

	@Override
	public void onRpcEvent(RpcEvent<ListingPayload<R>> event) {
		assert listingRequest != null;
		final ListingPayload<R> result = event.getPayload();
		assert result.getListingName() != null && listingName != null && result.getListingName().equals(listingName);

		final ListingOp op = listingRequest.getListingOp();

		listingGenerated = result.getListingStatus() == ListingStatus.CACHED;

		if(!listingGenerated && op.isQuery()) {
			// we need to re-create the listing on the server - the cache has expired
			fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
		}
		else {
			// update client-side listing state
			offset = result.getOffset();
			sorting = result.getSorting();
			listSize = result.getListSize();
			// reset
			listingRequest = null;
			// fire the listing event
			sourcingWidget.fireEvent(new ListingEvent<R>(op, result, listingDef.getPageSize()));
		}
	}
}
