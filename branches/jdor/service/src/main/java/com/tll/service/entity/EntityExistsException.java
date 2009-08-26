/**
 * The Logic Lab
 * @author jpk
 * @since Aug 14, 2009
 */
package com.tll.service.entity;


/**
 * EntityExistsException
 * @author jpk
 */
public class EntityExistsException extends Exception {

	private static final long serialVersionUID = 6350078999714964830L;

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
