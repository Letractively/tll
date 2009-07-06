/**
 * The Logic Lab
 * @author jpk
 * @since Jul 4, 2009
 */
package com.tll.dao;

/**
 * EntityNotFoundException
 * @author jpk
 */
public class EntityNotFoundException extends PersistenceException {
	private static final long serialVersionUID = -4916173451536387997L;

	/**
	 * Constructor
	 */
	public EntityNotFoundException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}
}
