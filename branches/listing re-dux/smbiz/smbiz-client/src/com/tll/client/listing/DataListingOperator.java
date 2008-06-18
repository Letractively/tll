/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.tll.client.data.ListingOp;
import com.tll.client.event.IListingListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.model.IData;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.listhandler.Sorting;

/**
 * DataCollectionListingOperator - {@link IListingOperator} based on an existing
 * collection of listing elements of arbitrary type.
 * @author jpk
 */
// TODO implemenet sorting!!!
public class DataListingOperator<R extends IData> implements IListingOperator<R> {

	/**
	 * The listing event listeners.
	 */
	private final ListingListenerCollection<R> listeners = new ListingListenerCollection<R>();

	/**
	 * The listing widget
	 */
	private ListingWidget<R> listingWidget;

	/**
	 * The data provider.
	 */
	private final IDataProvider<R> dataProvider;

	private final int pageSize;

	private int offset = 0;

	// TODO make private when sorting is implemented
	/*private*/Sorting sorting;

	private final int numPages = -1;

	private final int pageNum = -1;

	/**
	 * Constructor
	 * @param pageSize
	 * @param dataProvider
	 * @param sorting
	 */
	public DataListingOperator(int pageSize, IDataProvider<R> dataProvider, Sorting sorting) {
		super();
		this.pageSize = pageSize;
		this.dataProvider = dataProvider;
		this.sorting = sorting;
	}

	public ListingWidget<R> getListingWidget() {
		return listingWidget;
	}

	public void setListingWidget(ListingWidget<R> listingWidget) {
		if(listingWidget == null) throw new IllegalArgumentException();
		if(this.listingWidget != null) {
			removeListingListener(this.listingWidget);
		}
		this.listingWidget = listingWidget;
		addListingListener(listingWidget);
	}

	public void addListingListener(IListingListener<R> listener) {
		listeners.add(listener);
	}

	public void removeListingListener(IListingListener<R> listener) {
		listeners.remove(listener);
	}

	/**
	 * Extracts a sub-array from the dataProvider
	 * @param startIndex 0-based inclusive
	 * @param endIndex 0-based EXCLUSIVE
	 * @return Array of data list elements
	 */
	@SuppressWarnings("unchecked")
	private R[] subArray(int startIndex, int endIndex) {
		final R[] data = dataProvider.getData();
		IData[] array = new IData[endIndex - startIndex];
		for(int i = startIndex; i < endIndex; i++) {
			array[i] = data[i];
		}
		return (R[]) array;
	}

	/**
	 * Assembles a listing event ready for firing.
	 * @param pageElements
	 * @param listingOp
	 * @return A new ListingEvent
	 */
	private ListingEvent<R> assembleListingEvent(R[] pageElements, ListingOp listingOp) {
		final int listSize = pageElements == null ? 0 : pageElements.length;
		return new ListingEvent<R>(listingWidget, true, null, ListingOp.REFRESH, listSize, pageElements, offset, sorting,
				pageSize);
	}

	public void refresh() {
		listeners.fireListingEvent(assembleListingEvent(dataProvider.getData(), ListingOp.REFRESH));
	}

	public void display() {
		listeners.fireListingEvent(assembleListingEvent(dataProvider.getData(), ListingOp.FETCH));
	}

	public void firstPage() {
		if(pageNum != 0) {
			listeners.fireListingEvent(assembleListingEvent(subArray(0, pageSize - 1), ListingOp.FETCH));
		}
	}

	public void lastPage() {
		if(pageNum == numPages - 1) {
			listeners.fireListingEvent(assembleListingEvent(subArray(0, numPages - 1), ListingOp.FETCH));
		}
	}

	public void nextPage() {
		// TODO impl
		throw new UnsupportedOperationException();
	}

	public void previousPage() {
		// TODO impl
		throw new UnsupportedOperationException();
	}

	public void gotoPage(int pageNum) {
		if(this.pageNum != pageNum) {

			listeners.fireListingEvent(assembleListingEvent(subArray(0, pageSize - 1), ListingOp.FETCH));
		}
	}

	public void sort(Sorting sorting) {
		// TODO impl
		throw new UnsupportedOperationException();
	}

	public void clear() {
		offset = 0;
		sorting = null;
	}
}
