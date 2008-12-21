package com.tll.client.validate;

import java.util.ArrayList;

/**
 * CompositeValidator
 * @author jkirton
 */
public class CompositeValidator implements IValidator {

	private final ArrayList<IValidator> validators = new ArrayList<IValidator>();

	/**
	 * Constructor
	 */
	public CompositeValidator() {
	}

	/**
	 * Adds a validator.
	 * @param validator The validator to add
	 * @return this
	 */
	public CompositeValidator add(IValidator validator) {
		validators.add(validator);
		return this;
	}

	/**
	 * Removes a validator by reference only.
	 * @param validator The validator to remove
	 * @return <code>true</code> if the validator was successfully remvoed.
	 */
	public boolean remove(IValidator validator) {
		return validators.remove(validator);
	}

	public Object validate(Object value) throws ValidationException {
		Object retValue = value;
		for(IValidator v : validators) {
			retValue = v.validate(retValue);
		}
		return retValue;
	}
}
