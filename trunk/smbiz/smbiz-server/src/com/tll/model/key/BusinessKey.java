package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Abstract base class for all business keys in the application.
 * @author jpk
 */
public abstract class BusinessKey<E extends IEntity> extends EntityKey<E> implements IBusinessKey<E> {

	public final String[] getFieldNames() {
		return getFields();
	}

	public final String descriptor() {
		return getEntityType().getName() + " " + keyDescriptor();
	}

	/**
	 * @return The key specific descriptor declaring the constituent field names.
	 *         Used to provide the descriptor string.
	 * @see #descriptor()
	 */
	protected abstract String keyDescriptor();

}
