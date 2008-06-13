package com.tll.server.rpc.listing;

import java.util.Iterator;
import java.util.List;

import com.tll.client.model.IData;
import com.tll.client.model.Model;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IPage;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Page;
import com.tll.listhandler.PageUtil;
import com.tll.listhandler.Sorting;

/**
 * The {@link IListingHandler} implementation.
 * @author jpk
 */
public final class ModelListingHandler implements IListingHandler {

	private final IListHandler<Model> listHandler;
	private final boolean pageable;
	private final int pageSize;
	private final String listingName;
	/**
	 * The page of current elements and is never <code>null</code>. <br>
	 * NOTE: {@link IPage}s have 0-based page numbering!
	 */
	private final Page<Model> page;

	/**
	 * Constructor
	 * @param listHandler
	 * @param listingName
	 * @param config
	 * @throws IllegalArgumentException upon null or invalid arguments
	 */
	ModelListingHandler(IListHandler<Model> listHandler, String listingName, boolean pageable, int pageSize)
			throws IllegalArgumentException {
		super();

		if(listHandler == null) {
			throw new IllegalArgumentException("Unable to instantiate table model handler: No list handler specified");
		}

		if(listingName == null) {
			throw new IllegalArgumentException("Unable to instantiate table model handler: No listing name specified");
		}

		if(pageable && pageSize < 1) {
			throw new IllegalArgumentException("The page size must be greater than zero.");
		}

		this.listHandler = listHandler;
		this.pageable = pageable;
		this.pageSize = pageable ? pageSize : listHandler.size();
		this.listingName = listingName;
		// page should never be null
		this.page = new Page<Model>(pageSize, listHandler.size());
	}

	public String getListingName() {
		return listingName;
	}

	public int getPageNumber() {
		return page.getPageNumber();
	}

	public int getNumPages() {
		return PageUtil.calculateNumPages(pageSize, listHandler.size());
	}

	/**
	 * @param pageNum 0-based page number.
	 * @param bForce Force the wrapped list handler to re-retrieve elements even
	 *        if the page number is the same as the one provided?
	 * @param adjustPageNum Attempt to adjust the page number if it is found to be
	 *        out of bounds?
	 * @throws EmptyListException
	 * @throws PageNumOutOfBoundsException Thrown when the given page number
	 *         exceeds the actual number of pages
	 * @throws ListHandlerException Thrown when the given page number is less than
	 *         <code>0</code>,
	 */
	@SuppressWarnings("unchecked")
	private final void setCurrentPageInternal(int pageNum, boolean bForce, boolean adjustPageNum)
			throws EmptyListException, PageNumOutOfBoundsException, ListHandlerException {

		if(!listHandler.hasElements()) {
			throw new EmptyListException("No list elements exist");
		}

		final int size = listHandler.size();
		assert size > 0;
		final int numPages = pageable ? PageUtil.calculateNumPages(pageSize, size) : (size > 0 ? 1 : 0);

		if(pageNum < 0) {
			throw new ListHandlerException("Negative page number: " + pageNum);
		}

		if(pageNum >= numPages) {
			if(!adjustPageNum) {
				throw new PageNumOutOfBoundsException(listingName, pageNum, numPages);
			}
		}
		// NOTE: at this point, we may adjust the page number

		assert page != null;
		if(!bForce && page.getNumPageElements() > 0 && page.getPageNumber() == pageNum) return; // no-op

		int startIndex, endIndex;
		List<Model> rows;

		if(pageable) {
			startIndex = pageNum * pageSize; // (0-based index)
			// adjust start index if necessary
			while(startIndex >= size) {
				startIndex = --pageNum * pageSize;
			}
			// adjust end index if necessary
			endIndex = startIndex + pageSize; // 0-based exclusive
			if(endIndex > size) {
				endIndex = size;
			}
		}
		else {
			startIndex = 0;
			endIndex = size; // (exclusive)
		}

		rows = listHandler.getElements(startIndex, endIndex);
		page.setPage(rows, pageNum);
		page.setTotalSize(size); // we reset this at it may have changed
	}

	public int setCurrentPage(int pageNum, boolean adjustPageNum) throws EmptyListException, PageNumOutOfBoundsException,
			ListingException {
		try {
			// NOTE: we force re-retrieval of the same page when we are allowed to
			// adjust the page number
			setCurrentPageInternal(pageNum, adjustPageNum, adjustPageNum);
		}
		catch(EmptyListException e) {
			throw e;
		}
		catch(PageNumOutOfBoundsException e) {
			throw e;
		}
		catch(ListHandlerException e) {
			throw new ListingException(listingName, "An unexpected list handling error occurred: " + e.getMessage(), e);
		}
		return page.getPageNumber();
	}

	public Sorting getSorting() {
		return listHandler.getSorting();
	}

	public void sort(Sorting sorting) throws ListHandlerException {

		listHandler.sort(sorting);

		assert page != null;
		// key off of the existing page first index for re-retrieval of the page
		// rows.
		int oldFirstIndex = page.getFirstIndex();

		if(!hasElements()) throw new EmptyListException("Upon sorting, no rows were found.");

		final int size = size();

		// adjust the index if necessary
		if(oldFirstIndex > size - 1) {
			oldFirstIndex = size - 1;
		}
		else if(oldFirstIndex < 0) {
			oldFirstIndex = 0;
		}

		// (re-)retrieve the page rows
		setCurrentPage(PageUtil.getPageNumberFromListIndex(oldFirstIndex, size, pageSize), true);
	}

	public Model getElement(int index) throws EmptyListException, ListHandlerException {
		return listHandler.getElement(index);
	}

	public List<Model> getElements(int start, int end) throws ListHandlerException {
		return listHandler.getElements(start, end);
	}

	public boolean hasElements() {
		return listHandler.hasElements();
	}

	public int size() {
		return listHandler.size();
	}

	public ListHandlerType getListHandlerType() {
		return listHandler.getListHandlerType();
	}

	public Iterator<Model> iterator() {
		return listHandler.iterator();
	}

	public IPage<? extends IData> getPage() {
		return page;
	}

	public ListingState getState() {
		return new ListingState(page == null ? null : page.getPageNumber(), listHandler.getSorting());
	}
}
