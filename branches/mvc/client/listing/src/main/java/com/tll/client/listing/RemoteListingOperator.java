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
import com.tll.common.data.ListingPayload;
import com.tll.common.data.ListingRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.IListingServiceAsync;
import com.tll.common.model.Model;
import com.tll.common.search.ISearch;
import com.tll.dao.Sorting;

/**
 * RemoteListingOperator
 * @author jpk
 * @param <S> the search type
 */
@SuppressWarnings("unchecked")
public final class RemoteListingOperator<S extends ISearch> extends AbstractListingOperator<Model> {

	private static final IListingServiceAsync<ISearch, IMarshalable> svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
	}

	/**
	 * ListingCommand
	 * @author jpk
	 */
	@SuppressWarnings("synthetic-access")
	class ListingCommand extends RpcCommand<ListingPayload<Model>> {

		/**
		 * Constructor
		 * @param sourcingWidget
		 */
		public ListingCommand(Widget sourcingWidget) {
			super(sourcingWidget);
		}

		@Override
		protected void doExecute() {
			if(listingRequest == null) {
				throw new IllegalStateException("No listing command set!");
			}
			svc.process((ListingRequest) listingRequest, (AsyncCallback) getAsyncCallback());
		}

		@Override
		protected void handleFailure(Throwable caught) {
			super.handleFailure(caught);
		}

		@Override
		protected void handleSuccess(ListingPayload<Model> payload) {
			super.handleSuccess(payload);
			assert payload.getListingName() != null && listingName != null && payload.getListingName().equals(listingName);

			final ListingOp op = listingRequest.getListingOp();

			listingGenerated = payload.getListingStatus() == ListingStatus.CACHED;

			if(!listingGenerated && op.isQuery()) {
				// we need to re-create the listing on the server - the cache has expired
				fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
			}
			else {
				// update client-side listing state
				offset = payload.getOffset();
				sorting = payload.getSorting();
				listSize = payload.getListSize();
				// reset
				listingRequest = null;
				// fire the listing event
				sourcingWidget.fireEvent(new ListingEvent<Model>(op, payload, listingDef.getPageSize()));
			}
		}
	}
	
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
	 * @param listingName the identifying listing name for client and server. this
	 *        name <em>must</em> be unique among the others that are presently
	 *        cached server side.
	 * @param listingDef the remote listing definition
	 */
	public RemoteListingOperator(String listingName, RemoteListingDefinition<S> listingDef) {
		if(listingName == null || listingDef == null) throw new IllegalArgumentException();
		this.listingName = listingName;
		this.listingDef = listingDef;
	}

	private void execute() {
		assert listingRequest != null;
		final ListingCommand cmd = new ListingCommand(sourcingWidget);
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
}
