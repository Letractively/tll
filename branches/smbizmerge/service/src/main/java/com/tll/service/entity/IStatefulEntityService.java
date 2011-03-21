package com.tll.service.entity;

import java.util.Collection;

import com.tll.model.IEntity;

/**
 * Entity service interface for entities that support the notion of deletion.
 * @author jpk
 * @param <E>
 */
public interface IStatefulEntityService<E extends IEntity> extends IEntityService<E> {

	/**
	 * mark an entity as deleted
	 * @param entity
	 */
	void delete(E entity);

	/**
	 * mark a collection of entities as deleted
	 * @param entities
	 */
	void deleteAll(Collection<E> entities);
}
