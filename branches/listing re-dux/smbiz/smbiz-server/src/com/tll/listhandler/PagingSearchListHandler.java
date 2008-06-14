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
	 * The page size.
	 */
	private final int pageSize;

	/**
	 * The current index relative to the underlying result set.
	 */
	private int offset = 0;

	/**
	 * The current page of results
	 */
	private IPageResult<SearchResult<E>> page;

	/**
	 * Constructor
	 * @param dataProvider
	 * @param pageSize The page size. Must be at least <code>1</code>.
	 */
	PagingSearchListHandler(IListHandlerDataProvider<E> dataProvider, int pageSize) {
		super(dataProvider);
		if(pageSize < 1) {
			throw new IllegalArgumentException("The page size must be at least 1");
		}
		this.pageSize = pageSize;
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.PAGE;
	}

	@Override
	protected void doSearch(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException,
			NoMatchingResultsException {
		page = dataProvider.getPage(criteria, sorting, offset, pageSize);
		if(page.getResultCount() < 1) {
			throw new NoMatchingResultsException();
		}
	}

	@Override
	public List<SearchResult<E>> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {
		if(size() < 1) {
			throw new EmptyListException("No list elements exist");
		}
		try {
			doSearch(criteria, sorting);
		}
		catch(InvalidCriteriaException e) {
			throw new ListHandlerException(e.getMessage());
		}
		catch(NoMatchingResultsException e) {
			throw new EmptyListException(e.getMessage());
		}
		return page.getPageList();
	}

	public int size() {
		return page == null ? 0 : page.getResultCount();
	}
}
