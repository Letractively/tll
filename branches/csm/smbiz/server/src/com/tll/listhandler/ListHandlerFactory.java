package com.tll.listhandler;

import java.util.Collection;

import com.tll.SystemError;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.util.CollectionUtil;

/**
 * Factory for creating concrete {@link IListHandler}s.
 * @author jpk
 */
public abstract class ListHandlerFactory {

	/**
	 * Creates a collection based list handler.
	 * @param c a collection
	 * @param sorting the sorting directive. May be <code>null</code>.
	 * @return IListHandler instance
	 * @throws EmptyListException
	 * @throws ListHandlerException When a sorting related occurrs.
	 */
	public static <T> IListHandler<T> create(Collection<T> c, Sorting sorting) throws EmptyListException,
			ListHandlerException {
		try {
			CollectionListHandler<T> listHandler = new CollectionListHandler<T>(CollectionUtil.listFromCollection(c));
			if(sorting != null) {
				listHandler.sort(sorting);
			}
			return listHandler;
		}
		catch(final EmptyListException ele) {
			throw ele;
		}
	}

	/**
	 * Creates a criteria based list handler.
	 * @param <E>
	 * @param criteria
	 * @param sorting
	 * @param type
	 * @param dataProvider
	 * @return The generated search based {@link IListHandler}
	 * @throws InvalidCriteriaException When the criteria or the sorting directive
	 *         is not specified.
	 * @throws EmptyListException When the list handler type is
	 *         {@link ListHandlerType#COLLECTION} and no matching results exist.
	 * @throws ListHandlerException When the list handler type is
	 *         {@link ListHandlerType#COLLECTION} and the sorting directive is
	 *         specified but mal-formed.
	 */
	public static <E extends IEntity> IListHandler<SearchResult<E>> create(ICriteria<? extends E> criteria,
			Sorting sorting, ListHandlerType type, IListHandlerDataProvider<E> dataProvider) throws InvalidCriteriaException,
			EmptyListException, ListHandlerException {

		SearchListHandler<E> slh = null;

		switch(type) {

			case COLLECTION:
				return create(dataProvider.find(criteria, null), sorting);

			case IDLIST:
				if(criteria.getCriteriaType().isQuery()) {
					throw new InvalidCriteriaException("Id list handling does not support query based criteria");
				}
				slh = new IdListHandler<E>(dataProvider, criteria, sorting);
				break;

			case PAGE:
				slh = new PagingSearchListHandler<E>(dataProvider, criteria, sorting);
				break;

			default:
				throw new SystemError("Unhandled list handler type: " + type);
		}

		return slh;
	}
}