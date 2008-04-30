package com.tll.listhandler;

import java.util.List;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;

/**
 * Search supporting list handler implementation for pageable result sets.
 * @author jpk
 */
public final class PagingSearchListHandler<E extends IEntity> extends SearchListHandler<E> {

	/**
	 * The current page of results
	 */
	private IPage<SearchResult<E>> page;

	/**
	 * Constructor
	 * @param dataProvider
	 */
	public PagingSearchListHandler(IListHandlerDataProvider<E> dataProvider) {
		super(dataProvider);
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.PAGE;
	}

	@Override
	protected void refresh(ICriteria<? extends E> criteria) throws InvalidCriteriaException, NoMatchingResultsException {
		final int pageNumber = page == null ? 0 : page.getPageNumber();
		page = dataProvider.getPage(criteria, pageNumber, criteria.getPageSize());
		this.criteria = criteria;
		if(page.getTotalSize() < 1) {
			throw new NoMatchingResultsException("No results found.");
		}
	}

	/**
	 * Re-sets the page property if necessary based on the given index.
	 * <p>
	 * <b>NOTE:</b>{@link #page} is presumed to not be <code>null</code>.
	 * @param index The 0-based index spanning the entire result set.
	 */
	@SuppressWarnings("unchecked")
	protected void changePage(int index) {
		assert page != null : "Unable to changePage(): page property was null";
		final int pageNum = PageUtil.getPageNumberFromListIndex(index, size(), getPageSize());
		if(pageNum != page.getPageNumber()) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("Changing page " + page.getPageNumber() + " to " + pageNum);
			}
			try {
				this.page = dataProvider.getPage(page, pageNum);
			}
			catch(final RuntimeException rt) {
				LOG.error("Change page failed: " + rt.getMessage());
				throw rt;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public SearchResult<E> getElement(int index) throws EmptyListException, ListHandlerException {
		if(!hasElements()) {
			throw new EmptyListException("No list elements exist");
		}
		if(!PageUtil.isValidListIndex(index, size())) {
			throw new ListHandlerException("Invalid index: " + index);
		}
		changePage(index);
		final int pageIndex = PageUtil.getPageIndexFromListIndex(index, size(), getPageSize());
		return page.getPageElements().get(pageIndex);
	}

	// NOTE: this impl only allows the list indexes to be confined to a single
	// page range!
	// Otherwise, the logic would be intense.
	@SuppressWarnings("unchecked")
	public List<SearchResult<E>> getElements(int start, int end) throws EmptyListException, ListHandlerException {
		if(!hasElements()) {
			throw new EmptyListException("No list elements exist");
		}
		if(!PageUtil.isValidPageIndexRange(start, end, size(), getPageSize())) {
			throw new ListHandlerException("Invalid list index range: [" + start + "] - [" + end + "]");
		}
		changePage(start);
		final int pageStart = PageUtil.getPageIndexFromListIndex(start, size(), getPageSize());
		final int pageEnd = pageStart + (end - start);
		return page.getPageElements().subList(pageStart, pageEnd);
	}

	public int size() {
		return page == null ? 0 : page.getTotalSize();
	}

	private final int getPageSize() {
		return page == null ? 0 : page.getPageSize();
	}
}
