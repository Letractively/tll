/**
 * The Logic Lab
 * @author jpk
 * @since May 19, 2009
 */
package com.tll.server.marshal;

import com.tll.common.model.IEntityType;


/**
 * IMarshalOptionsResolver
 * @author jpk
 */
public interface IMarshalOptionsResolver {

	/**
	 * Resolves marshal options for a given entity type
	 * @param entityType
	 * @return the appropriate marshal options
	 * @throws IllegalArgumentException when the options can't be resolved for the
	 *         given entity type
	 */
	MarshalOptions resolve(IEntityType entityType) throws IllegalArgumentException;
}
