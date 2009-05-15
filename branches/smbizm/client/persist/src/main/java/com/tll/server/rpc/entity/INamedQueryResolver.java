/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.criteria.ISelectNamedQueryDef;


/**
 * INamedQueryResolver
 * @author jpk
 */
public interface INamedQueryResolver {

	/**
	 * Resolves a query name to an {@link ISelectNamedQueryDef} implementation.
	 * @param queryName The query name
	 * @return The resolved {@link ISelectNamedQueryDef} implementation.
	 * @throws IllegalArgumentException When the query name can't be resolved.
	 */
	ISelectNamedQueryDef resolveNamedQuery(String queryName) throws IllegalArgumentException;
}
