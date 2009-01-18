/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao;

import com.tll.model.IEntity;

/**
 * IDaoFactory - Used primarily for testing.
 * @author jpk
 */
public interface IDaoFactory {

	/**
	 * @param <D>
	 * @param type
	 * @return the matching dao
	 */
	<D extends IDao> D instance(Class<D> type);

	/**
	 * @param <E>
	 * @param entityType
	 * @return the matching entity dao
	 */
	<E extends IEntity> IEntityDao<E> instanceByEntityType(Class<E> entityType);
}