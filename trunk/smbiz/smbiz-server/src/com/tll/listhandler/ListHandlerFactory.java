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
	 */
	public static <T> IListHandler<T> create(Collection<T> c, Sorting sorting) throws EmptyListException {
		try {
			return new CollectionListHandler<T>(CollectionUtil.listFromCollection(c), sorting);
		}
		catch(final EmptyListException ele) {
			throw ele;
		}
		catch(final ListHandlerException lhe) {
			throw new SystemError("Unable to create collection based list handler: " + lhe.getMessage(), lhe);
		}
	}

	/**
	 * Creates a criteria based list handler.
	 * @param <E>
	 * @param criteria
	 * @param type
	 * @param dataProvider
	 * @return The generated search based {@link IListHandler}
	 * @throws InvalidCriteriaException
	 * @throws EmptyListException
	 */
	public static <E extends IEntity> IListHandler<SearchResult<E>> create(ICriteria<? extends E> criteria,
			ListHandlerType type, IListHandlerDataProvider<E> dataProvider) throws InvalidCriteriaException,
			EmptyListException {

		SearchListHandler<E> slh = null;

		switch(type) {

			case COLLECTION:
				return create(dataProvider.find(criteria), criteria.getSorting());

			case IDLIST:
				slh = new IdListHandler<E>(dataProvider);
				break;

			case PAGE:
				slh = new PagingSearchListHandler<E>(dataProvider);
				break;

			default:
				throw new SystemError("Unhandled list handler type: " + type);
		}

		try {
			slh.executeSearch(criteria);
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