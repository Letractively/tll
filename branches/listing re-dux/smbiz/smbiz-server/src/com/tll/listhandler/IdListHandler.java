package com.tll.listhandler;

import java.util.ArrayList;
import java.util.List;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;

/**
 * Search supporting list handler implementation based on an id list.
 * @author jpk
 */
public final class IdListHandler<E extends IEntity> extends SearchListHandler<E> {

	/**
	 * The id list - list of entity ids matching the search criteria.
	 */
	private List<Integer> ids;

	/**
	 * Constructor
	 * @param dataProvider
	 */
	IdListHandler(IListHandlerDataProvider<E> dataProvider) {
		super(dataProvider);
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.IDLIST;
	}

	public int size() {
		return (ids == null) ? 0 : ids.size();
	}

	@Override
	protected void doSearch(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException,
			NoMatchingResultsException {
		ids = dataProvider.getIds(criteria, sorting);
		if(ids == null || ids.size() < 1) {
			throw new NoMatchingResultsException();
		}
	}

	private List<Integer> getIds(int start, int end) throws EmptyListException, ListHandlerException {
		if(size() < 1) {
			throw new EmptyListException("Unable to retrieve id elements: no ids exist");
		}
		try {
			return this.ids.subList(start, end);
		}
		catch(final IndexOutOfBoundsException iobe) {
			throw new ListHandlerException("Invalid list index range: start(" + start + "), end(" + end + ")");
		}
	}

	@Override
	public List<SearchResult<E>> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {
		final List<E> list =
				dataProvider.getEntitiesFromIds(getEntityClass(), getIds(offset, offset + pageSize), getSorting());
		if(list == null || list.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}
		sort(sorting);
		final List<SearchResult<E>> slist = new ArrayList<SearchResult<E>>(list.size());
		for(final E e : list) {
			slist.add(new SearchResult<E>(e));
		}
		return slist;
	}
}
