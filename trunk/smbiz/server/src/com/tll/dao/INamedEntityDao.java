package com.tll.dao;

import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;

/**
 * Interface for data access objects that manage named entities (implement
 * INamedEntity interface).
 * @author jpk
 * @param <N>
 * @see INamedEntity
 */
public interface INamedEntityDao<N extends INamedEntity> extends IEntityDao<N> {

	/**
	 * Loads the named entity by a given name.
	 * @param nameKey the name key
	 * @return the named entity
	 */
	N load(NameKey<? extends N> nameKey);
}