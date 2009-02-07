/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.Collection;

import org.hibernate.validator.Validator;

/**
 * AtLeastOneValidator - Ensures at least one elements exists in a
 * {@link Collection}.
 * @see AtLeastOne
 * @author jpk
 */
public class AtLeastOneValidator implements Validator<AtLeastOne> {

	public void initialize(AtLeastOne parameters) {
		// no-op
	}

	@SuppressWarnings("unchecked")
	public boolean isValid(Object value) {
		if(value == null || value instanceof Collection == false) return true;
		Collection clc = (Collection) value;
		return clc.size() > 0;
	}
}
