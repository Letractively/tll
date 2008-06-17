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
import com.tll.client.event.type.ListingEvent;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.listhandler.Sorting;

/**
 * ListingRequest - Issues RPC listing commands to the server.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public final class ListingCommand<S extends ISearch> extends RpcCommand<ListingPayload<Model>> implements IListingOperator<Model> {

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
	private ModelListingWidget listingWidget;

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
	 * @param listingName The unique listing name
	 * @param listingDef The remote listing definition
	 */
	public ListingCommand(String listingName, RemoteListingDefinition listingDef) {
		super();
		this.listingName = listingName;
		this.listingDef = listingDef;
	}

	public ListingWidget<Model> getListingWidget() {
		return listingWidget;
	}

	public void setListingWidget(ModelListingWidget listingWidget) {
		if(listingWidget == null) throw new IllegalArgumentException();
		if(this.listingWidget != null) {
			removeListingListener(this.listingWidget);
		}
		this.listingWidget = listingWidget;
		addListingListener(listingWidget);
	}

	@Override
	protected Widget getSourcingWidget() {
		if(listingWidget == null) throw new IllegalStateException();
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
		if(listingWidget == null) {
			throw new IllegalStateException("No listing widget set!");
		}
		svc.process((ListingRequest) listingRequest, (AsyncCallback) getAsyncCallback());
	}

	@Override
	public void handleSuccess(ListingPayload result) {
		assert listingRequest != null && listingWidget != null;
		assert result.getListingName() != null && listingName != null && result.getListingName().equals(listingName);
		super.handleSuccess(result);

		final ListingOp op = listingRequest.getListingOp();

		listingGenerated = result.getListingStatus() == ListingStatus.CACHED;

		if(!listingGenerated && op.isQuery()) {
			// we need to re-create the listing on the server - the cache has expired
			fetch(listingRequest.getOffset(), listingRequest.getSorting(), true);
			DeferredCommand.addCommand(this);
		}
		else {
			// update client-side listing state
			offset = result.getOffset();
			sorting = result.getSorting();
			listSize = result.getListSize();
			// reset
			listingRequest = null;
			// fire the listing event
			listeners.fireListingEvent(new ListingEvent<Model>(listingWidget, op, result, listingDef.getPageSize()));
		}
	}

	public void refresh() {
		fetch(offset, sorting, true);
	}

	public void display() {
		fetch(offset, sorting);
	}

	public void sort(Sorting sorting) {
		if(listingGenerated && this.sorting != null && this.sorting.equals(sorting)) {
			return;
		}
		fetch(offset, sorting);
	}

	public void firstPage() {
		if(listingGenerated && offset == 0) {
			return;
		}
		fetch(offset, sorting);
	}

	public void gotoPage(int pageNum) {
		// calc the offset
		final int offset = pageNum == 0 ? 0 : pageNum * listingDef.getPageSize() - 1;
		if(listingGenerated && this.offset == offset) {
			return;
		}
		fetch(offset, sorting);
	}

	public void lastPage() {
		// calc the offset
		// TODO verify
		final int pageSize = listingDef.getPageSize();
		final int numPages =
				(listSize % pageSize == 0) ? (int) (listSize / pageSize) : Math.round(listSize / pageSize + 0.5f);
		final int offset = (numPages - 1) * pageSize - 1;
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
