/**
 * The Logic Lab
 * @author jpk
 * @since Jan 23, 2010
 */
package com.tll.model.key;

import com.google.appengine.api.datastore.Key;
import com.tll.model.IEntity;

/**
 * IGaePrimaryKeyGeneratorImpl - Responsible for creating a {@link Key}
 * instances for application specific entities.
 * @author jpk
 */
public interface IGaePrimaryKeyGeneratorImpl {

	/**
	 * Creates a {@link Key} based on the given entity instance.
	 * @param entity The required entity instance
	 * @return The generated key
	 */
	Key generateKey(IEntity entity);
}
