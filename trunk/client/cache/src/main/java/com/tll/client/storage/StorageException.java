/**
 * The Logic Lab
 * @author jpk
 * Jul 11, 2008
 */
package com.tll.client.storage;

/**
 * StorageException
 * @author jpk
 */
@SuppressWarnings("serial")
public class StorageException extends Exception {

	/**
	 * Constructor
	 */
	public StorageException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public StorageException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public StorageException(Throwable cause) {
		super(cause);
	}

}
