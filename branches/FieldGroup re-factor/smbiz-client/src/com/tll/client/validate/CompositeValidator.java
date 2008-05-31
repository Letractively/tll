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
	 * Adds a validator
	 * @param validator The validator to add
	 * @return this
	 */
	public CompositeValidator add(IValidator validator) {
		// ensure distinct
		if(validators.indexOf(validator) < 0) validators.add(validator);
		return this;
	}

	/**
	 * Removes a validator
	 * @param validator The validator to remove
	 * @return this
	 */
	public CompositeValidator remove(IValidator validator) {
		validators.remove(validator);
		return this;
	}

	public Object validate(Object value) throws ValidationException {
		Object retValue = value;
		for(IValidator v : validators) {
			retValue = v.validate(retValue);
		}
		return retValue;
	}
}
