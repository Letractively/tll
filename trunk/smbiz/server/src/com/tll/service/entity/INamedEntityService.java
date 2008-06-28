package com.tll.service.entity;

import javax.persistence.EntityNotFoundException;

import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;

/**
 * Interface for services that manage named Entities (implement INamedEntity
 * interface)
 * @author jpk
 * @see INamedEntity
 */
public interface INamedEntityService<N extends INamedEntity> extends IEntityService<N> {

	/**
	 * Load by name key.
	 * @param key
	 * @throws EntityNotFoundException
	 */
	N load(NameKey<? extends N> key) throws EntityNotFoundException;
}