/**
 * The Logic Lab
 * @author jpk
 * @since Aug 27, 2009
 */
package com.tll.dao;


/**
 * @author jpk
 */
public class EntityExistsException extends RuntimeException {

	private static final long serialVersionUID = -7233687566073361502L;

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public EntityExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public EntityExistsException(String message) {
		super(message);
	}

}
