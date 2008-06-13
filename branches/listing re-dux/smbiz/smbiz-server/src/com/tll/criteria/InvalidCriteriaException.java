package com.tll.criteria;

import com.tll.model.IEntity;

/**
 * Throw when {@link ICriteria} derived instances are found to be invalid due to
 * either invalid contained values or when the criteria is null or empty
 * (contains no {@link ICriterion} objects.
 * @author jpk
 */
public class InvalidCriteriaException extends CriteriaException {

	private static final long serialVersionUID = 5508051537602537427L;

	/**
	 * Constructor
	 */
	public InvalidCriteriaException() {
		super("Invalid or empty criteria");
	}

	/**
	 * Constructor
	 * @param message
	 */
	public InvalidCriteriaException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param entityClass the entity class
	 */
	public InvalidCriteriaException(Class<? extends IEntity> entityClass) {
		super("Invalid or empty criteria for %1", entityClass.getSimpleName());
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public InvalidCriteriaException(String message, Throwable cause) {
		super(message, cause);
	}
}