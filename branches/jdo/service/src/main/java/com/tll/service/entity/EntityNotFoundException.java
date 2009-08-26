/**
 * The Logic Lab
 * @author jpk
 * @since Aug 14, 2009
 */
package com.tll.service.entity;

/**
 * EntityNotFoundException
 * @author jpk
 */
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -5413016803346743016L;

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}

}
