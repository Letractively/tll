package com.tll.service.entity;

import com.tll.dao.EntityNotFoundException;
import com.tll.model.INamedEntity;
import com.tll.model.NameKey;

/**
 * Interface for services that manage named Entities (implement INamedEntity
 * interface)
 * @author jpk
 * @param <N>
 * @see INamedEntity
 */
public interface INamedEntityService<N extends INamedEntity> extends IEntityService<N> {

	/**
	 * Load by name key.
	 * @param key
	 * @return the loaded named entity
	 * @throws EntityNotFoundException
	 */
	N load(NameKey key) throws EntityNotFoundException;
}