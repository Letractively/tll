package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * IPrimaryKey - Definition for primary keys.
 * @author jpk
 */
public interface IPrimaryKey<E extends IEntity> extends IEntityKey<E> {

	/**
	 * @return the id
	 */
	Integer getId();

	/**
	 * Sets the id
	 * @param id
	 */
	void setId(Integer id);

}
