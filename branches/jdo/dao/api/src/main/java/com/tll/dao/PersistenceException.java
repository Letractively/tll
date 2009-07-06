/**
 * The Logic Lab
 * @author jpk
 * @since Jul 4, 2009
 */
package com.tll.dao;

/**
 * PersistenceException - Common base class for persistence related exceptions.
 * @author jpk
 */
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = 3153916858214597814L;

	/**
	 * Constructor
	 */
	public PersistenceException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public PersistenceException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public PersistenceException(Throwable cause) {
		super(cause);
	}

}
