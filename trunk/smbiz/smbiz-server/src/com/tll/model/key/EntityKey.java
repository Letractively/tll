package com.tll.model.key;

import com.tll.key.Key;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.impl.EntityType;

/**
 * Abstract key class for entity related keys.
 * @author jpk
 */
public abstract class EntityKey<E extends IEntity> extends Key<E> implements IEntityKey<E> {

	public final EntityType getEntityType() {
		return EntityUtil.entityTypeFromClass(getType());
	}

}
