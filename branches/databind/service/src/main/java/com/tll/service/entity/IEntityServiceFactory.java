package com.tll.service.entity;

import com.tll.model.IEntity;
import com.tll.service.IService;

public interface IEntityServiceFactory {

	/**
	 * Gets a service by its class.
	 * @param <S> The entity service type
	 * @param type The service type
	 * @return the associated service
	 * @throws IllegalArgumentException When the service can't be resolved
	 */
	<S extends IService> S instance(Class<S> type) throws IllegalArgumentException;

	/**
	 * Get the entity service by its supported entity type.
	 * <p>
	 * NOTE: This is a generic access method and as such, the client will not have
	 * access to any entity type specific service methods unless a cast is
	 * performed.
	 * @param <E> 
	 * @param entityType The entity type
	 * @return the entity service
	 * @throws IllegalArgumentException When the service can't be resolved
	 */
	<E extends IEntity> IEntityService<E> instanceByEntityType(Class<E> entityType) throws IllegalArgumentException;
}