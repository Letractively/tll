package com.tll.server.rpc.listing;

import java.util.List;

import com.tll.client.model.IData;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.Sorting;

/**
 * The {@link IListingHandler} implementation.
 * @author jpk
 */
public final class ListingHandler<R extends IData> implements IListingHandler<R> {

	private final int pageSize;
	private final String listingName;

	private final IListHandler<R> listHandler;

	/**
	 * The current list index.
	 */
	private int offset;

	/**
	 * The current list of elements.
	 */
	private List<R> page;

	/**
	 * The sorting directive.
	 */
	private Sorting sorting;

	/**
	 * Constructor
	 * @param listHandler
	 * @param listingName
	 * @param pageSize
	 * @throws IllegalArgumentException upon null or invalid arguments
	 */
	ListingHandler(IListHandler<R> listHandler, String listingName, int pageSize) throws IllegalArgumentException {
		super();

		if(listHandler == null) {
			throw new IllegalArgumentException("Unable to instantiate table model handler: No list handler specified");
		}

		if(listingName == null) {
			throw new IllegalArgumentException("Unable to instantiate table model handler: No listing name specified");
		}

		if(pageSize < 1) {
			throw new IllegalArgumentException("The page size must be greater than zero.");
		}

		this.listHandler = listHandler;
		this.listingName = listingName;
		this.pageSize = pageSize;
	}

	public String getListingName() {
		return listingName;
	}

	@Override
	public List<R> getElements() {
		return page;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	public int size() {
		return listHandler.size();
	}

	@Override
	public Sorting getSorting() {
		return sorting;
	}

	@Override
	public void query(int offset, Sorting sorting, boolean force) throws EmptyListException, IndexOutOfBoundsException,
			ListingException {

		final int size = listHandler.size();
		if(size < 1) {
			throw new EmptyListException("No list elements exist");
		}

		if(offset < 0) {
			throw new IndexOutOfBoundsException("Negative offset: " + offset);
		}

		// do we need to actually re-query?
		if(!force && page != null && this.offset == offset && this.sorting != null && this.sorting.equals(sorting)) {
			return;
		}

		// query
		try {
			page = listHandler.getElements(offset, pageSize, sorting);
		}
		catch(ListHandlerException e) {
			throw new ListingException(listingName, e.getMessage());
		}
	}
}
