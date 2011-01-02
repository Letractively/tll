/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.IModelRelatedRequest;

/**
 * IPersistServiceImplResolver
 * @author jpk
 */
public interface IPersistServiceImplResolver {

	/**
	 * Resolves a model related request to a supporting
	 * {@link IPersistServiceImpl} type.
	 * @param request The model related request
	 * @return The resolved {@link IPersistServiceImpl} type.
	 * @throws IllegalArgumentException When the model related requesst class
	 *         can't be resolved to a service impl type
	 */
	Class<? extends IPersistServiceImpl> resolve(IModelRelatedRequest request) throws IllegalArgumentException;
}
