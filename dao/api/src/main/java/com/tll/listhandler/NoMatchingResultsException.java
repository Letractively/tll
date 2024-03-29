package com.tll.listhandler;

import com.tll.criteria.CriteriaException;

/**
 * NoMatchingResultsException
 * @author jpk
 */
public class NoMatchingResultsException extends CriteriaException {

	private static final long serialVersionUID = -6289618568027114307L;

	/**
	 * Constructor
	 */
	public NoMatchingResultsException() {
		super("No matching results");
	}

	/**
	 * Constructor
	 * @param message
	 */
	public NoMatchingResultsException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param entityClass the entity class
	 */
	public NoMatchingResultsException(Class<?> entityClass) {
		super("No matching results found for %1", entityClass.getSimpleName());
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public NoMatchingResultsException(String message, Throwable cause) {
		super(message, cause);
	}
}
