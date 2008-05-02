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
	protected List<Integer> ids;

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
	protected void refresh(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException,
			NoMatchingResultsException {
		ids = dataProvider.getIds(criteria, sorting);
		this.criteria = criteria;
		if(ids == null || ids.size() < 1) {
			throw new NoMatchingResultsException("No results found anymore upon list refresh.");
		}
	}

	private final List<Integer> getIds(int start, int end) throws EmptyListException, ListHandlerException {
		if(!hasElements()) {
			throw new EmptyListException("Unable to retrieve id elements: no ids exist");
		}
		try {
			return this.ids.subList(start, end);
		}
		catch(final IndexOutOfBoundsException iobe) {
			throw new ListHandlerException("Invalid list index range: start(" + start + "), end(" + end + ")");
		}
	}

	@SuppressWarnings("unchecked")
	public SearchResult<E> getElement(int index) throws EmptyListException, ListHandlerException {

		if(index < 0 || index > size() - 1) {
			throw new ListHandlerException("Unable to retreive list elements: invalid index: " + index);
		}
		final List<E> list = dataProvider.getEntitiesFromIds(criteria.getEntityClass(), getIds(index, index + 1), sorting);

		if(list == null || list.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}

		return new SearchResult(list.get(0));
	}

	@SuppressWarnings("unchecked")
	public List<SearchResult<E>> getElements(int start, int end) throws EmptyListException, ListHandlerException {
		if(start < 0 || end < 0 || start > end) {
			throw new ListHandlerException("Unable to retreive list elements: invalid range - start(" + start + "), end("
					+ end + ")");
		}
		final List<E> list = dataProvider.getEntitiesFromIds(criteria.getEntityClass(), getIds(start, end), sorting);

		if(list == null || list.size() < 1) {
			throw new EmptyListException("No list elements exist");
		}

		final List<SearchResult<E>> slist = new ArrayList<SearchResult<E>>(list.size());
		for(final E e : list) {
			slist.add(new SearchResult<E>(e));
		}
		return slist;
	}

}
