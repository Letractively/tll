package com.tll.model;


/**
 * IPrimaryKeyGenerator - Contract for entity primary key generation.
 * @author jpk
 * @param <I> The primay key impl type
 */
public interface IPrimaryKeyGenerator<I extends IPrimaryKey> {

	/**
	 * Creates a unique primary key for the given entity instance.
	 * <p>
	 * <em><b>NOTE</b>: the primary key is not required to be set on the given entity.</em>
	 * @param entityType The entity type for which to generate a primary key
	 * @return A new unique primary key
	 */
	I generateIdentifier(Class<?> entityType);
}
