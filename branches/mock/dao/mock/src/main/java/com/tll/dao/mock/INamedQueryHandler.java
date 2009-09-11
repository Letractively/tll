/**
 * The Logic Lab
 * @author jpk
 * @since Sep 8, 2009
 */
package com.tll.dao.mock;

import java.util.Collection;
import java.util.List;

import com.tll.criteria.IQueryParam;

/**
 * INamedQueryHandler
 * @author jpk
 */
public interface INamedQueryHandler {

	/**
	 * Executes a select type named query.
	 * @param <T> the result type
	 * @param queryName
	 * @param params
	 * @return the select query results
	 */
	<T> List<T> doSelectNamedQuery(String queryName, Collection<IQueryParam> params);

	/**
	 * Executes a mutation (update/delete) type named query.
	 * @param queryName
	 * @param params
	 */
	void doMutationNamedQuery(String queryName, Collection<IQueryParam> params);
}
