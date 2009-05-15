/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.IModelRelatedRequest;
import com.tll.model.IEntity;


/**
 * IMEntityServiceImplResolver
 * @author jpk
 */
public interface IMEntityServiceImplResolver {

	/**
	 * Resolves a model related request to a supporting
	 * {@link IMEntityServiceImpl} type.
	 * @param request The model related request
	 * @return The resolved {@link IMEntityServiceImpl} type.
	 * @throws IllegalArgumentException When the model related requesst class
	 *         can't be resolved to a service impl type
	 */
	Class<? extends IMEntityServiceImpl<? extends IEntity>> resolve(
			IModelRelatedRequest request)
			throws IllegalArgumentException;
}
