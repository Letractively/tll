package com.tll.server.listing;

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
	
	private final IListHandler<R> listHandler;

	private final String listingId;

	/**
	 * The current list index.
	 */
	private int offset;

	/**
	 * The current list of elements.
	 */
	private List<R> elements;

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
		return listHandler.getSorting();
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
		final Sorting sorting = getSorting();
		if(!force && elements != null && this.offset == ofst && sorting != null && sorting.equals(srtg)) {
			return;
		}

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
