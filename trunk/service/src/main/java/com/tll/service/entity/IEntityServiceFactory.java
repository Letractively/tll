package com.tll.service.entity;

import com.tll.model.IEntity;

public interface IEntityServiceFactory {

	/**
	 * Get the entity service by its class.
	 * @param <S> The entity service type
	 * @param type The entity service type
	 * @return the associated entity service
	 * @throws IllegalArgumentException When the service can't be resolved
	 */
	<S extends IEntityService<? extends IEntity>> S instance(Class<S> type) throws IllegalArgumentException;

	/**
	 * Get the entity service by its supported entity type.
	 * <p>
	 * NOTE: This is a generic access method and as such, the client will not have
	 * access to any entity type specific service methods unless a cast is
	 * performed.
	 * @param <E> The entity type
	 * @param entityType The entity type
	 * @return the entity service
	 * @throws IllegalArgumentException When the service can't be resolved
	 */
	<E extends IEntity> IEntityService<E> instanceByEntityType(Class<E> entityType) throws IllegalArgumentException;

}