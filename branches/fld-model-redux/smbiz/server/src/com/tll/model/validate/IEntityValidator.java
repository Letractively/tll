/**
 * The Logic Lab
 * @author jpk
 * @date Dec 22, 2007
 */
package com.tll.model.validate;

import org.hibernate.validator.InvalidStateException;

import com.tll.model.IEntity;

/**
 * IEntityValidator - Generalized entity validation defintion. TODO sub-class
 * InvalidStateException to isolate from org.hibernate import refs.
 * @author jpk
 */
public interface IEntityValidator<E extends IEntity> {

	/**
	 * The single validation method.
	 * @param e The entity to be validated.
	 * @throws InvalidStateException When the entity is found to be invalid.
	 */
	void validate(E e) throws InvalidStateException;
}
