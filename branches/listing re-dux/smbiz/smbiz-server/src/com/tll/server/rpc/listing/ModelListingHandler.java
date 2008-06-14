package com.tll.server.rpc.listing;

import java.util.List;

import com.tll.client.model.Model;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.Sorting;

/**
 * The {@link IListingHandler} implementation.
 * @author jpk
 */
public final class ModelListingHandler implements IListingHandler<Model> {

	private final int pageSize;
	private final String listingName;

	private final IListHandler<Model> listHandler;

	/**
	 * The current list index.
	 */
	private int offset;

	/**
	 * The current list of elements.
	 */
	private List<Model> page;

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
	ModelListingHandler(IListHandler<Model> listHandler, String listingName, int pageSize)
			throws IllegalArgumentException {
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
	public List<Model> getElements() {
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
	public void query(int offset, int pageSize, Sorting sorting) throws EmptyListException, IndexOutOfBoundsException,
			ListingException {
		if(listHandler.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}

		final int size = listHandler.size();
		assert size > 0;

		if(offset < 0) {
			throw new IndexOutOfBoundsException("Negative offset: " + offset);
		}

		try {
			page = listHandler.getElements(offset, pageSize, sorting);
		}
		catch(ListHandlerException e) {
			throw new ListingException(listingName, e.getMessage());
		}
	}
}
