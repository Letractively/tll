package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.IEntity;
import com.tll.model.impl.EntityType;

/**
 * Abstraction representing a unique identifier of an entity.
 * @author jpk
 */
public interface IEntityKey<E extends IEntity> extends IKey<E> {

	/**
	 * @return the {@link EntityType} this key identifies
	 */
	EntityType getEntityType();

	/**
	 * @param entity
	 */
	void setEntity(E entity);
}
