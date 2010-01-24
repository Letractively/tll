package com.tll.model;

/**
 * ObjectGenerator - Contract for entity primary key generation
 * <em>independent</em> of an entity.
 * @param <K> The primary key type
 * @author jpk
 */
public interface IPrimaryKeyGenerator<K> {

	/**
	 * Creates new primary key.
	 * @param entity The required entity instance
	 * @return the generated primary key
	 */
	K generateIdentifier(IEntity entity);
}
