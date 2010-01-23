package com.tll.model;

/**
 * IPrimaryKeyGenerator - Contract for entity primary key generation
 * <em>independent</em> of an entity.
 * @author jpk
 * @param <I> The primay key impl type
 */
public interface IPrimaryKeyGenerator<I extends IPrimaryKey> {

	/**
	 * Creates a new primary key for the given entity instance.
	 * @param entityType The entity type for which to generate a primary key
	 * @return A new unique primary key
	 */
	I generateIdentifier(Class<?> entityType);
}
