/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.search.ISearch;
import com.tll.model.IEntity;


/**
 * IMEntityServiceImplResolver
 * @author jpk
 */
public interface IMEntityServiceImplResolver {

	/**
	 * Resolves an entity type to a supporting {@link IMEntityServiceImpl} type.
	 * @param entityClass The entity type
	 * @return The resolved {@link IMEntityServiceImpl} type.
	 * @throws IllegalArgumentException When the given entity class can't be
	 *         resolved to a service impl type
	 */
	Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> resolveMEntityServiceImpl(
			Class<? extends IEntity> entityClass) throws IllegalArgumentException;
}
