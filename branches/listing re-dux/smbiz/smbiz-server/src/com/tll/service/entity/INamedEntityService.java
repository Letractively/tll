package com.tll.service.entity;

import javax.persistence.EntityNotFoundException;

import com.tll.model.INamedEntity;
import com.tll.model.key.INameKey;

/**
 * Interface for services that manage named Entities (implement INamedEntity
 * interface)
 * @author jpk
 * @see INamedEntity
 */
public interface INamedEntityService<E extends INamedEntity> extends IEntityService<E> {

	/**
	 * Load by name key.
	 * @param key
	 * @throws EntityNotFoundException
	 */
	E load(INameKey<? extends E> key) throws EntityNotFoundException;
}