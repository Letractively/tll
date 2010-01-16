package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * IPrimaryKeyGenerator - Contract for entity primary key generation.
 * @author jpk
 */
public interface IPrimaryKeyGenerator {

	/**
	 * Creates a unique primary key for the given entity instance.
	 * <p>
	 * <em><b>NOTE</b>: the primary key is not required to be set on the given entity.</em>
	 * @param entity the entity instance for which to generate a primary key
	 * @return A new unique primary key
	 */
	long generateIdentifier(IEntity entity);
}
