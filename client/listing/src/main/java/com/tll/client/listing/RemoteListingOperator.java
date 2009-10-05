/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.client.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.RpcCommand;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.IListingServiceAsync;
import com.tll.common.model.Model;
import com.tll.common.search.IListingSearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.ListHandlerType;

/**
 * RemoteListingOperator
 * @author jpk
 * @param <S> the search type
 */
@SuppressWarnings("unchecked")
public final class RemoteListingOperator<S extends IListingSearch> extends AbstractListingOperator<Model> {

	/**
	 * Factory method that creates a listing command to control acccess to a remote listing.
	 * @param <S> The search type
	 * @param listingId the unique listing id
	 * @param listHandlerType The remote list handler type
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param pageSize The desired paging page size.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link RemoteListingOperator}
	 */
	public static <S extends IListingSearch> RemoteListingOperator<S> create(
			String listingId, ListHandlerType listHandlerType, S searchCriteria, String[] propKeys, int pageSize, Sorting initialSorting) {

		final RemoteListingDefinition<S> rld =
			new RemoteListingDefinition<S>(listHandlerType, searchCriteria, propKeys, pageSize, initialSorting);
		return new RemoteListingOperator<S>(listingId, rld);
	}

	private static final IListingServiceAsync<IListingSearch, IMarshalable> svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
	}

	/**
	 * ListingCommand
	 * @author jpk
	 */
	@SuppressWarnings("synthetic-access")
	class ListingCommand extends RpcCommand<ListingPayload<Model>> {

		@Override
		protected void doExecute() {
			if(listingRequest == null) {
				throw new IllegalStateException("Null listing command");
			}
			if(sourcingWidget == null) {
				throw new IllegalStateException("Null sourcing widget");
			}
			svc.process(listingRequest, (AsyncCallback) getAsyncCallback());
		}

		@Override
		protected void handleSuccess(ListingPayload<Model> payload) {
			super.handleSuccess(payload);
			assert payload.getListingId() != null && listingId != null && payload.getListingId().equals(listingId);

			final ListingOp op = listingRequest.getListingOp();

			listingGenerated = payload.getListingStatus() == ListingStatus.CACHED;

			if(!listingGenerated && op.isQuery()) {
				if(!payload.hasErrors()) {
					// we need to re-create the listing on the server - the cache has
					// expired
					fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
				}
			}
			else {
				// update client-side listing state
				offset = payload.getOffset();
				sorting = payload.getSorting();
				listSize = payload.getListSize();
				// reset
				listingRequest = null;
				// fire the listing event
				sourcingWidget.fireEvent(new ListingEvent<Model>(payload.getListingId(), op, payload
						.getListSize(), payload.getPageElements(), payload.getOffset(), payload.getSorting(), listingDef
						.getPageSize()));
			}
		}
	}

	/**
	 * The unique name that identifies the listing this command targets on the
	 * server.
	 */
	private final String listingId;

	/**
	 * The server-side listing definition.
	 */
	private final RemoteListingDefinition<S> listingDef;

	private transient ListingRequest listingRequest;

	/**
	 * Constructor
	 * @param listingId unique listing id
	 * @param listingDef the remote listing definition
	 */
	public RemoteListingOperator(String listingId, RemoteListingDefinition<S> listingDef) {
		if(listingId == null || listingDef == null) throw new IllegalArgumentException();
		this.listingId = listingId;
		this.listingDef = listingDef;
	}

	private void execute() {
		assert listingRequest != null;
		final ListingCommand cmd = new ListingCommand();
		cmd.setSource(sourcingWidget);
		cmd.execute();
	}

	/**
	 * Fetches listing data sending the listing definition in case it isn't cached
	 * server-side.
	 * @param ofst The listing index offset
	 * @param srtg The sorting directive
	 * @param refresh Force the listing to be re-queried on the server if it is
	 *        cached?
	 */
	private void fetch(int ofst, Sorting srtg, boolean refresh) {
		this.listingRequest =
			new ListingRequest(listingId, listingDef, refresh ? ListingOp.REFRESH : ListingOp.FETCH, ofst, srtg);
		execute();
	}

	/**
	 * Fetches listing data against a listing that is presumed to be cached
	 * server-side. If the listing is found not to be cached server-side, the
	 * listing response will indicate this and a subsequent fetch containing the
	 * listing definition will be issued. This is intended to save on network
	 * bandwidth as the case when the server-side listing cache is expired is
	 * assumed to not occur frequently.
	 * @param ofst The listing index offset
	 * @param srtg The sorting directive
	 */
	@Override
	protected void doFetch(int ofst, Sorting srtg) {
		listingRequest = new ListingRequest(listingId, ofst, srtg);
		execute();
	}

	/**
	 * Clear the listing.
	 * @param retainListingState Retain the listing state on the server?
	 */
	private void clear(boolean retainListingState) {
		listingRequest = new ListingRequest(listingId, retainListingState);
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
}
