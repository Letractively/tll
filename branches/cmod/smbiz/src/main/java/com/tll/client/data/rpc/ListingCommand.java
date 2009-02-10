/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.IListingListener;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.listing.PagingUtil;
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
 * ListingRequest - Issues listing commands to the server.
 * @author jpk
 * @param <S>
 */
@SuppressWarnings("unchecked")
public final class ListingCommand<S extends ISearch> extends RpcCommand<ListingPayload> implements IListingOperator<Model> {

	private static final IListingServiceAsync<ISearch, Model> svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
	}

	/**
	 * The listing event listeners.
	 */
	private final ListingListenerCollection<Model> listeners = new ListingListenerCollection<Model>();

	/**
	 * The Widget that will be passed in dispatched {@link ListingEvent}s.
	 */
	private final Widget sourcingWidget;

	/**
	 * The unique name that identifies the listing this command targets on the
	 * server.
	 */
	private final String listingName;

	/**
	 * The server-side listing definition.
	 */
	private final RemoteListingDefinition<S> listingDef;

	/**
	 * The current list index offset.
	 */
	private int offset = 0;

	/**
	 * The current sorting directive.
	 */
	private Sorting sorting;

	/**
	 * The current list size.
	 */
	private int listSize = -1;

	/**
	 * Has the listing been generated?
	 */
	private boolean listingGenerated;

	/**
	 * The listing request issued to the server.
	 */
	private ListingRequest<S> listingRequest;

	/**
	 * Constructor
	 * @param sourcingWidget
	 * @param listingName The unique listing name
	 * @param listingDef The remote listing definition
	 */
	public ListingCommand(Widget sourcingWidget, String listingName, RemoteListingDefinition listingDef) {
		super();
		this.sourcingWidget = sourcingWidget;
		this.listingName = listingName;
		this.listingDef = listingDef;
	}

	@Override
	protected Widget getSourcingWidget() {
		if(sourcingWidget == null) throw new IllegalStateException("No sourcing widget set!");
		return sourcingWidget;
	}

	public void addListingListener(IListingListener<Model> listener) {
		listeners.add(listener);
	}

	public void removeListingListener(IListingListener<Model> listener) {
		listeners.remove(listener);
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
	 * assumed to not occurr frequently.
	 * @param offset The listing index offset
	 * @param sorting The sorting directive
	 */
	private void fetch(int offset, Sorting sorting) {
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
	protected void doExecute() {
		if(listingRequest == null) {
			throw new IllegalStateException("No listing command set!");
		}
		svc.process((ListingRequest) listingRequest, (AsyncCallback) getAsyncCallback());
	}

	@Override
	public void handleSuccess(ListingPayload result) {
		assert listingRequest != null;
		assert result.getListingName() != null && listingName != null && result.getListingName().equals(listingName);

		final ListingOp op = listingRequest.getListingOp();

		listingGenerated = result.getListingStatus() == ListingStatus.CACHED;

		if(!listingGenerated && op.isQuery()) {
			// we need to re-create the listing on the server - the cache has expired
			fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
		}
		else {
			super.handleSuccess(result);
			// update client-side listing state
			offset = result.getOffset();
			sorting = result.getSorting();
			listSize = result.getListSize();
			// reset
			listingRequest = null;
			// fire the listing event
			listeners.fireListingEvent(new ListingEvent<Model>(sourcingWidget, op, result, listingDef.getPageSize()));
		}
	}

	public void refresh() {
		fetch(offset, sorting, true);
	}

	public void display() {
		fetch(offset, sorting);
	}

	public void sort(Sorting sorting) {
		if(!listingGenerated || (this.sorting != null && !this.sorting.equals(sorting))) {
			fetch(offset, sorting);
		}
	}

	public void firstPage() {
		if(!listingGenerated || offset != 0) fetch(0, sorting);
	}

	public void gotoPage(int pageNum) {
		final int offset = PagingUtil.listIndexFromPageNum(pageNum, listingDef.getPageSize());
		if(!listingGenerated || this.offset != offset) fetch(offset, sorting);
	}

	public void lastPage() {
		final int pageSize = listingDef.getPageSize();
		final int numPages = PagingUtil.numPages(listSize, pageSize);
		final int offset = PagingUtil.listIndexFromPageNum(numPages - 1, pageSize);
		if(listingGenerated && this.offset == offset) {
			return;
		}
		fetch(offset, sorting);
	}

	public void nextPage() {
		final int offset = this.offset + listingDef.getPageSize();
		if(offset < listSize) fetch(offset, sorting);
	}

	public void previousPage() {
		final int offset = this.offset - listingDef.getPageSize();
		if(offset >= 0) fetch(offset, sorting);
	}

	public void clear() {
		clear(true);
	}
}
