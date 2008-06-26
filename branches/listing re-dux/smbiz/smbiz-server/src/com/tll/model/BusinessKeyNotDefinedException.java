package com.tll.model;

import com.tll.ApplicationException;

/**
 * BusinessKeyNotDefinedException
 * @author jpk
 */
public class BusinessKeyNotDefinedException extends ApplicationException {

	private static final long serialVersionUID = -695847771421395166L;

	/**
	 * @param entityClass either a entity class or a business key class
	 */
	@SuppressWarnings("unchecked")
	public BusinessKeyNotDefinedException(Class<? extends IEntity> entityClass) {
		super("No business keys exist for  " + EntityUtil.typeName(entityClass));
	}

}
