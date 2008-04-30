package com.tll.listhandler;

import java.util.Collection;
import java.util.List;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;

/**
 * IListHandlerDataProvider - Definition for providing data to
 * {@link IListHandler}s. This definition supports in memory collection paging,
 * result set paging and id list based paging.
 * @see ListHandlerType
 * @author jpk
 */
public interface IListHandlerDataProvider<E extends IEntity> {

	/**
	 * Retrieves a list of matching results for the given criteria.
	 * @param criteria
	 * @return list of result elements or an empty list if no matches are found.
	 * @throws InvalidCriteriaException
	 */
	List<SearchResult<E>> find(ICriteria<? extends E> criteria) throws InvalidCriteriaException;

	/**
	 * Retrieves the ids of the entities that match the given criteria.
	 * @param criteria
	 * @return list of ids of matching elements or an empty list if no matching
	 *         results are found.
	 * @throws InvalidCriteriaException
	 */
	List<Integer> getIds(ICriteria<? extends E> criteria) throws InvalidCriteriaException;

	/**
	 * Retrieves entities from a collection of ids.
	 * @param entityClass The entity class the ids represent.
	 * @param ids List of ids of the entities to retrieve.
	 * @param sorting the sorting directive May be null in which case the sorting
	 *        of the results is "undefined".
	 * @return list of matching entities.
	 */
	List<E> getEntitiesFromIds(Class<? extends E> entityClass, Collection<Integer> ids, Sorting sorting);

	/**
	 * Returns a page of matching results for the given criteria.
	 * @param criteria
	 * @param page
	 * @param pageSize
	 * @throws InvalidCriteriaException
	 */
	IPage<SearchResult<E>> getPage(ICriteria<? extends E> criteria, int page, int pageSize)
			throws InvalidCriteriaException;

	/**
	 * Returns a subsequent page of matching results.
	 * @param currentPage The current page
	 * @param newPageNum The desired page to query for
	 *        {@link #getPage(ICriteria, int, int)} must be called first!
	 * @see #getPage(ICriteria, int, int)
	 */
	IPage<SearchResult<E>> getPage(IPage<SearchResult<E>> currentPage, int newPageNum);
}
