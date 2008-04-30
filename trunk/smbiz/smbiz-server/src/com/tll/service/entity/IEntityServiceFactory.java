package com.tll.service.entity;

import com.tll.model.IEntity;

public interface IEntityServiceFactory {

	/**
	 * Get the entity service by its class.
	 * @param <S>
	 * @param type
	 */
	<S extends IEntityService<? extends IEntity>> S instance(Class<S> type);

	/**
	 * Get the entity service by its supported entity type.
	 * @param <E>
	 * @param entityType
	 */
	<E extends IEntity> IEntityService<E> instanceByEntityType(Class<E> entityType);

}