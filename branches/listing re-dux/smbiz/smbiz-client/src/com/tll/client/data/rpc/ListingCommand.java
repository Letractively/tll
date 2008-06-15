/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingRequest;
import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.ListingPayload.ListingStatus;
import com.tll.client.event.IListingListener;
import com.tll.client.event.ISourcesListingEvents;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;

/**
 * ListingRequest - Issues RPC listing commands to the server.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public final class ListingCommand<S extends ISearch> extends RpcCommand<ListingPayload<Model>> implements IListingOperator, ISourcesListingEvents<Model> {

	private static final IListingServiceAsync<ISearch, Model> svc;
	static {
		svc = (IListingServiceAsync) GWT.create(IListingService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/listing");
	}

	/**
	 * The listing event listeners.
	 */
	private final ListingListenerCollection<Model> listeners = new ListingListenerCollection<Model>();

	/**
	 * The listing widget.
	 */
	private final ListingWidget<Model> listingWidget;

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
	private final Integer offset = 0;

	/**
	 * The current sorting directive.
	 */
	private Sorting sorting;

	/**
	 * The listing request issued to the server.
	 */
	private ListingRequest<S> listingRequest;

	/**
	 * Constructor
	 * @param listingWidget The listing Widget
	 * @param listingName The unique listing name
	 * @param listingDef The remote listing definition
	 * @throws IllegalArgumentException When any of the arguments are
	 *         <code>null</code>.
	 */
	public ListingCommand(ListingWidget listingWidget, String listingName, RemoteListingDefinition listingDef)
			throws IllegalArgumentException {
		super();
		if(listingWidget == null || listingName == null || listingDef == null) {
			throw new IllegalArgumentException();
		}
		this.listingWidget = listingWidget;
		this.listingName = listingName;
		this.listingDef = listingDef;
	}

	@Override
	protected Widget getSourcingWidget() {
		return listingWidget;
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
	private void fetch(Integer offset, Sorting sorting, boolean refresh) {
		this.listingRequest =
				new ListingRequest<S>(listingName, listingDef, refresh ? ListingOp.REFRESH : ListingOp.FETCH, offset, sorting);
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
	private void fetch(Integer offset, Sorting sorting) {
		listingRequest = new ListingRequest<S>(listingName, offset, sorting);
	}

	/**
	 * Clear the listing.
	 * @param retainListingState Retain the listing state on the server?
	 */
	private void clear(boolean retainListingState) {
		listingRequest = new ListingRequest<S>(listingName, retainListingState);
	}

	@Override
	@SuppressWarnings("unchecked")
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
		super.handleSuccess(result);

		final ListingOp op = listingRequest.getListingOp();

		if(result.getListingStatus() == ListingStatus.NOT_CACHED && op.isQuery()) {
			// we need to re-create the listing on the server - the cache has expired
			fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
			DeferredCommand.addCommand(this);
		}
		else {
			// fire the listing event
			listingRequest = null; // reset
			listeners.fireListingEvent(new ListingEvent<Model>(listingWidget, op, result));
		}
	}

	public void refresh() {
		fetch(offset, sorting, true);
	}

	public void display() {
		fetch(offset, sorting);
	}

	public void sort(SortColumn sortColumn) {
		fetch(offset, new Sorting(sortColumn));
	}

	public void navigate(ListingOp navAction, Integer page) {
	}

	public void clear() {
		clear(true);
	}
}
