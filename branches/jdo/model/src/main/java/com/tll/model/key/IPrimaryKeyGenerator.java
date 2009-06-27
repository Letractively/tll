package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * IPrimaryKeyGenerator - Interface for primary key generation strategy that
 * takes an entity class and returns a new unique primary key value.
 * @author jpk
 */
public interface IPrimaryKeyGenerator {

	/**
	 * Given the class object, this method returns a unique primary key for that
	 * entity.
	 * @param entityClass the entity class
	 * @return unique primary key
	 */
	Integer generateIdentifier(Class<? extends IEntity> entityClass);
}
