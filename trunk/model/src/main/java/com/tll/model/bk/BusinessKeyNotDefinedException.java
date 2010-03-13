package com.tll.model.bk;

import com.tll.ApplicationException;
import com.tll.model.IEntity;

/**
 * BusinessKeyNotDefinedException
 * @author jpk
 */
public class BusinessKeyNotDefinedException extends ApplicationException {

	private static final long serialVersionUID = -695847771421395166L;

	/**
	 * Constructor - Use when <em>no</em> business keys are defined for the
	 * given entity type.
	 * @param entityClass The entity type
	 */
	public BusinessKeyNotDefinedException(Class<? extends IEntity> entityClass) {
		super("No business keys exist for  " + entityClass.getName());
	}

	/**
	 * Constructor - Use when no business key is found for the given entity type
	 * and business key name.
	 * @param entityClass The entity type
	 * @param businessKeyName The business key name
	 */
	public BusinessKeyNotDefinedException(Class<? extends IEntity> entityClass, String businessKeyName) {
		super("No business key of name '" + businessKeyName + "' is defined for " + entityClass.getName());
	}
}
