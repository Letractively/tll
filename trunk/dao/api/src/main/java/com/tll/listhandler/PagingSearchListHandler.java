package com.tll.listhandler;

import java.util.List;

import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.sort.Sorting;

/**
 * Search supporting list handler implementation for pageable result sets.
 * @author jpk
 */
public final class PagingSearchListHandler extends SearchListHandler {

	/**
	 * The current page of results
	 */
	private IPageResult<SearchResult> page;

	/**
	 * Constructor
	 * @param dataProvider The data provider used to fetch the list elements with
	 *        the given criteria.
	 * @param criteria The criteria used to generate the underlying list
	 * @param sorting The required sorting directive.
	 */
	PagingSearchListHandler(IListingDataProvider dataProvider, Criteria<?> criteria, Sorting sorting) {
		super(dataProvider, criteria, sorting);
	}

	@Override
	public List<SearchResult> getElements(int offset, int pageSize, Sorting sort) throws IndexOutOfBoundsException,
	EmptyListException, ListHandlerException {

		try {
			page = dataProvider.getPage(criteria, sort, offset, pageSize);
			if(page.getResultCount() < 1) {
				throw new EmptyListException("No matching page results found.");
			}
		}
		catch(final InvalidCriteriaException e) {
			throw new ListHandlerException(e.getMessage());
		}
		return page.getPageList();
	}

	@Override
	public int size() {
		return page == null ? 0 : page.getResultCount();
	}
}
