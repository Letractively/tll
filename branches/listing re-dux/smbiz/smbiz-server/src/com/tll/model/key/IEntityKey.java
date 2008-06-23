package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.EntityType;
import com.tll.model.IEntity;

/**
 * Abstraction representing a unique identifier of an entity.
 * @author jpk
 */
public interface IEntityKey<E extends IEntity> extends IKey<E> {

	/**
	 * @return the {@link EntityType} this key identifies
	 */
	EntityType getEntityType();
}
