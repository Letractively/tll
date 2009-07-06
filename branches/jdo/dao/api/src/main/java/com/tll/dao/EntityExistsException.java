/**
 * The Logic Lab
 * @author jpk
 * @since Jul 4, 2009
 */
package com.tll.dao;


/**
 * EntityExistsException
 * @author jpk
 */
public class EntityExistsException extends PersistenceException {
	private static final long serialVersionUID = -7307257969021309406L;

	/**
	 * Constructor
	 */
	public EntityExistsException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 */
	public EntityExistsException(String message) {
		super(message);
	}

}
