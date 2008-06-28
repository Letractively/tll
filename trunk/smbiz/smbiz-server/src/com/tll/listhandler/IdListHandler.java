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
	 * @param dataProvider The data provider used to fetch the list elements with
	 *        the given criteria.
	 * @param criteria The criteria used to generate the underlying list
	 */
	IdListHandler(IListHandlerDataProvider<E> dataProvider, ICriteria<? extends E> criteria, Sorting sorting) {
		super(dataProvider, criteria, sorting);
	}

	public ListHandlerType getListHandlerType() {
		return ListHandlerType.IDLIST;
	}

	public int size() {
		return (ids == null) ? 0 : ids.size();
	}

	@Override
	public List<SearchResult<E>> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException,
			EmptyListException, ListHandlerException {

		assert this.sorting != null;

		// if sorting differs, re-execute search
		if(sorting != null && !sorting.equals(this.sorting) || (sorting == null && this.sorting != null)) {
			try {
				ids = dataProvider.getIds(criteria, sorting);
			}
			catch(InvalidCriteriaException e) {
				throw new ListHandlerException(e.getMessage());
			}
		}

		if(ids == null || ids.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}

		final int size = ids.size();
		int ei = offset + pageSize;

		// adjust the end index if it exceeds the bounds of the id list
		if(ei > size - 1) ei = size - 1;

		List<Integer> subids = ids.subList(offset, ei);

		final List<E> list = dataProvider.getEntitiesFromIds(criteria.getEntityClass(), subids, sorting);
		if(list == null || list.size() != subids.size()) {
			throw new ListHandlerException("id and entity count mismatch");
		}
		final List<SearchResult<E>> slist = new ArrayList<SearchResult<E>>(list.size());
		for(final E e : list) {
			slist.add(new SearchResult<E>(e));
		}
		return slist;
	}
}
