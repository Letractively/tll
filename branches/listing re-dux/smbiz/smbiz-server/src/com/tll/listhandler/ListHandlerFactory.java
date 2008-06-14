package com.tll.listhandler;

import java.util.Collection;

import com.tll.SystemError;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.util.CollectionUtil;

/**
 * Factory for creating concrete {@link IListHandler} instances.
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
	 * @param pageSize The page size. Needed <em>only</em> when the given
	 *        {@link ListHandlerType} is {@link ListHandlerType#PAGE}.
	 * @param dataProvider
	 * @return The generated search based {@link IListHandler}
	 * @throws InvalidCriteriaException
	 * @throws EmptyListException
	 * @throws ListHandlerException When a sorting related error occurrs
	 */
	public static <E extends IEntity> IListHandler<SearchResult<E>> create(ICriteria<? extends E> criteria,
			Sorting sorting, ListHandlerType type, int pageSize, IListHandlerDataProvider<E> dataProvider)
			throws InvalidCriteriaException, EmptyListException, ListHandlerException {

		SearchListHandler<E> slh = null;

		switch(type) {

			case COLLECTION:
				return create(dataProvider.find(criteria, null), sorting);

			case IDLIST:
				slh = new IdListHandler<E>(dataProvider);
				break;

			case PAGE:
				slh = new PagingSearchListHandler<E>(dataProvider, pageSize);
				break;

			default:
				throw new SystemError("Unhandled list handler type: " + type);
		}

		try {
			slh.executeSearch(criteria, sorting);
		}
		catch(final InvalidCriteriaException e) {
			throw e;
		}
		catch(final NoMatchingResultsException e) {
			final String entityTypeName = EntityUtil.typeName(criteria.getEntityClass());
			throw new EmptyListException("No matching " + entityTypeName + " results found.");
		}

		return slh;
	}
}