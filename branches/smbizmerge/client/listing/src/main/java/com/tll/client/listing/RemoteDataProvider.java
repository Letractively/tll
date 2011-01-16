package com.tll.client.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.tll.IMarshalable;
import com.tll.common.data.ListingOp;
import com.tll.common.data.Page;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.data.rpc.IListingServiceAsync;
import com.tll.common.data.rpc.ListingPayload;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.dao.Sorting;

/**
 * @author jpk
 * <R> row data type
 * <S> search type
 */
public class RemoteDataProvider<T extends IMarshalable, S extends IMarshalable> extends AsyncDataProvider<T> {

	/**
	 * Factory method that creates a listing command to control acccess to a remote listing.
	 * @param <R> row data type
	 * @param <S> search criteria type
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
	public static <R extends IMarshalable, S extends IMarshalable> RemoteDataProvider<R, S> create(
			String listingId, S searchCriteria, String[] propKeys, int pageSize, Sorting initialSorting) {

		final RemoteListingDefinition<S> rld =
			new RemoteListingDefinition<S>(searchCriteria, propKeys, pageSize, initialSorting);
		return new RemoteDataProvider<R, S>(listingId, rld);
	}

	private static final IListingServiceAsync svc;
	static {
		svc = GWT.create(IListingService.class);
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

	//private transient ListingRequest<S> listingRequest;
	
	/**
	 * Constructor
	 * @param listingId unique listing id
	 * @param listingDef the remote listing definition
	 */
	public RemoteDataProvider(String listingId, RemoteListingDefinition<S> listingDef) {
		if(listingId == null || listingDef == null) throw new IllegalArgumentException();
		this.listingId = listingId;
		this.listingDef = listingDef;
	}

	@Override
	protected void onRangeChanged(final HasData<T> display) {
    Range range = display.getVisibleRange();
		Sorting sorting = null; // TODO get sorting from display
    final ListingRequest<S> request = new ListingRequest<S>(listingId, listingDef, ListingOp.FETCH, Integer.valueOf(range.getStart()), sorting);
		svc.process(request, new AsyncCallback<ListingPayload<T>>() {

			@Override
			public void onSuccess(ListingPayload<T> payload) {
				assert payload.getListingId() != null && listingId != null && payload.getListingId().equals(listingId);
				// update client-side listing state
				Page<T> page = payload.getPage();
				display.setRowData(page.getOffset(), page.getElements());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error in listing payload retrieval", caught);
			}

		});
	}

}
