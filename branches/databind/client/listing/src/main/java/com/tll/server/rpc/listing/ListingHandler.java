package com.tll.server.rpc.listing;

import java.util.List;

import com.tll.dao.Sorting;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerException;

/**
 * ListingHandler - Cachable listing construct.
 * @param <R> The row data type.
 * @author jpk
 */
public final class ListingHandler<R> {

	private final int pageSize;
	private final String listingId;

	private final IListHandler<R> listHandler;

	/**
	 * The current list index.
	 */
	private int offset;

	/**
	 * The current list of elements.
	 */
	private List<R> elements;

	/**
	 * The sorting directive.
	 */
	private Sorting sorting;

	/**
	 * Constructor
	 * @param listHandler The wrapped list handler
	 * @param listingId The unique listing name
	 * @param pageSize The desired page size or <code>-1</code> if not paging is
	 *        desired.
	 * @throws IllegalArgumentException When one of the arguments is
	 *         <code>null</code>.
	 */
	ListingHandler(IListHandler<R> listHandler, String listingId, int pageSize) throws IllegalArgumentException {
		super();

		if(listHandler == null) {
			throw new IllegalArgumentException("Unable to instantiate listing handler: No list handler specified");
		}

		if(listingId == null) {
			throw new IllegalArgumentException("Unable to instantiate listing handler: No listing name specified");
		}

		this.listHandler = listHandler;
		this.listingId = listingId;
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getListingId() {
		return listingId;
	}

	public List<R> getElements() {
		return elements;
	}

	public int getOffset() {
		return offset;
	}

	public int size() {
		return listHandler.size();
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void query(int ofst, Sorting srtg, boolean force) throws EmptyListException, IndexOutOfBoundsException,
			ListingException {

		if(!force && listHandler.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}

		if(ofst < 0 || (!force && ofst > listHandler.size() - 1)) {
			throw new IndexOutOfBoundsException("Listing offset " + ofst + " is out of bounds");
		}

		// do we need to actually re-query?
		if(!force && elements != null && this.offset == ofst && this.sorting != null && this.sorting.equals(srtg)) {
			return;
		}

		// update sorting (irregardless of whether or not we have resultant
		// elements)
		this.sorting = srtg;

		// query
		final int psize = pageSize == -1 ? listHandler.size() : pageSize;
		try {
			elements = listHandler.getElements(ofst, psize, srtg);
		}
		catch(final EmptyListException e) {
			throw e;
		}
		catch(final ListHandlerException e) {
			throw new ListingException(listingId, e.getMessage());
		}

		// update offset
		this.offset = ofst;
	}
}
