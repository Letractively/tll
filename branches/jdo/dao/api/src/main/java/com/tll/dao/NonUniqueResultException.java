/**
 * The Logic Lab
 * @author jpk
 * @since Jul 4, 2009
 */
package com.tll.dao;


/**
 * NonUniqueResultException
 * @author jpk
 */
public class NonUniqueResultException extends PersistenceException {
	private static final long serialVersionUID = 222469170992615348L;

	/**
	 * Constructor
	 */
	public NonUniqueResultException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 */
	public NonUniqueResultException(String message) {
		super(message);
	}

}
