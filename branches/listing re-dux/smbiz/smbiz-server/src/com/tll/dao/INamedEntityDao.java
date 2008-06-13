package com.tll.dao;

import com.tll.model.INamedEntity;
import com.tll.model.key.INameKey;

/**
 * Interface for data access objects that manage named entities (implement
 * INamedEntity interface).
 * @author jpk
 * @see INamedEntity
 */
public interface INamedEntityDao<N extends INamedEntity> extends IEntityDao<N> {

	/**
	 * Loads the named entity by a given name.
	 * @param name the name key
	 * @return the named entity
	 */
	N load(INameKey<? extends N> nameKey);
}