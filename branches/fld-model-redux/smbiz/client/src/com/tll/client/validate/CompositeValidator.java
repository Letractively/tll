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
		validators.add(validator);
		return this;
	}

	public Object validate(Object value) throws ValidationException {
		Object retValue = value;
		for(IValidator v : validators) {
			retValue = v.validate(retValue);
		}
		return retValue;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		CompositeValidator other = (CompositeValidator) obj;
		if(validators == null) {
			if(other.validators != null) return false;
		}
		else if(validators.size() != other.validators.size()) {
			return false;
		}
		for(int i = 0; i < validators.size(); i++) {
			if(!validators.get(i).equals(other.validators.get(i))) return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((validators == null) ? 0 : validators.hashCode());
		return result;
	}
}
