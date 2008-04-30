package com.tll.model;

import org.apache.commons.lang.ClassUtils;

import com.tll.ApplicationException;

/**
 * BusinessKeyNotDefinedException
 * @author jpk
 */
public class BusinessKeyNotDefinedException extends ApplicationException {

	private static final long serialVersionUID = -695847771421395166L;

	/**
	 * @param claz either a entity class or a business key class
	 * @param isBizKeyClass true if claz argument represents a entity, false if
	 *        claz argument represents a business key
	 */
	@SuppressWarnings("unchecked")
	public BusinessKeyNotDefinedException(Class claz, boolean isBizKeyClass) {
		super(isBizKeyClass ? ("No business keys exist for entity class: " + ClassUtils.getShortClassName(claz))
				: ("No business key of class: " + ClassUtils.getShortClassName(claz)) + " exists");
	}

}
