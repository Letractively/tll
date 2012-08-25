/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.client.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.RpcCommand;
import com.tll.common.data.ListingOp;
import com.tll.common.data.Page;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.IListingServiceAsync;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingPayload.ListingStatus;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.dao.Sorting;

/**
 * RemoteListingOperator
 * @param <R> row data type
 * @param <S> search type
 * @author jpk
 */
public final class RemoteListingOperator<R extends IMarshalable, S extends IMarshalable> extends AbstractListingOperator<R> {

	/**
	 * Factory method that creates a listing command to control acccess to a remote listing.
	 * @param <R> row data type
	 * @param <S> search criteria type
	 * @param target required target widget that will recieve listing events
	 * @param listingId the unique listing id
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param pageSize The desired paging page size.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link RemoteListingOperator}
	 */
	public static <R extends IMarshalable, S extends IMarshalable> RemoteListingOperator<R, S> create(Widget target,
			String listingId, S searchCriteria, String[] propKeys, int pageSize, Sorting initialSorting) {

		final RemoteListingDefinition<S> rld =
			new RemoteListingDefinition<S>(searchCriteria, propKeys, pageSize, initialSorting);
		return new RemoteListingOperator<R, S>(target, listingId, rld);
	}

	private static final IListingServiceAsync svc;
	static {
		svc = GWT.create(IListingService.class);
	}

	/**
	 * ListingCommand
	 * @author jpk
	 */
	@SuppressWarnings("synthetic-access")
	class ListingCommand extends RpcCommand<ListingPayload<R>> {

		@SuppressWarnings({
			"unchecked", "rawtypes" })
		@Override
		protected void doExecute() {
			if(listingRequest == null) {
				throw new IllegalStateException("Null listing command");
			}
			if(target == null) {
				throw new IllegalStateException("Null sourcing widget");
			}
			svc.process((ListingRequest) listingRequest, (AsyncCallback) getAsyncCallback());
		}

		@Override
		protected void handleSuccess(ListingPayload<R> payload) {
			super.handleSuccess(payload);
			assert payload.getListingId() != null && listingId != null && payload.getListingId().equals(listingId);

			final ListingOp op = listingRequest.getListingOp();

			listingGenerated = payload.getListingStatus() == ListingStatus.CACHED;

			if(!listingGenerated && op.isQuery()) {
				if(!payload.hasErrors()) {
					// we need to re-create the listing on the server - the cache has
					// expired
					fetch(listingRequest.getOffset().intValue(), listingRequest.getSorting(), true);
				}
			}
			else {
				// update client-side listing state
				Page<R> page = payload.getPage();
				offset = page == null ? -1 : page.getOffset();
				sorting = page == null ? null : page.getSorting();
				listSize = page == null ? -1 : page.getTotalSize();
				// reset
				listingRequest = null;
				fireListingEvent(op, page == null ? null : page.getElements());
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

	private transient ListingRequest<S> listingRequest;

	/**
	 * Constructor
	 * @param target required target widget to recieve listing events
	 * @param listingId unique listing id
	 * @param listingDef the remote listing definition
	 */
	public RemoteListingOperator(Widget target, String listingId, RemoteListingDefinition<S> listingDef) {
		super(target, listingDef.getInitialSorting());
		if(listingId == null) throw new IllegalArgumentException();
		this.listingId = listingId;
		this.listingDef = listingDef;
	}

	private void execute() {
		assert listingRequest != null;
		final ListingCommand cmd = new ListingCommand();
		cmd.setSource(target);
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
			new ListingRequest<S>(listingId, listingDef, refresh ? ListingOp.REFRESH : ListingOp.FETCH, Integer.valueOf(ofst), srtg);
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
		listingRequest = new ListingRequest<S>(listingId, Integer.valueOf(ofst), srtg);
		execute();
	}

	/**
	 * Clear the listing.
	 * @param retainListingState Retain the listing state on the server?
	 */
	private void clear(boolean retainListingState) {
		listingRequest = new ListingRequest<S>(listingId, retainListingState);
		execute();
	}

	@Override
	protected int getPageSize() {
		return listingDef.getPageSize();
	}

	@Override
	public void refresh() {
		fetch(0, defaultSorting, true);
	}

	@Override
	public void clear() {
		clear(true);
	}
}
