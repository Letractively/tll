package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Abstract base class for all business keys in the application.
 * @author jpk
 */
public abstract class BusinessKey<E extends IEntity> extends EntityKey<E> implements IBusinessKeyDefinition<E> {

	/**
	 * Constructor
	 * @param entityClass
	 */
	protected BusinessKey(Class<E> entityClass) {
		super(entityClass);
	}
}
