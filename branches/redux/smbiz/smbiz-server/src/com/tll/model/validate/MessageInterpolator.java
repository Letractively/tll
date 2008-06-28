/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import org.hibernate.validator.Validator;
import org.hibernate.validator.interpolator.DefaultMessageInterpolator;

/**
 * PropRefInterpolator
 * @author jpk
 */
class MessageInterpolator extends DefaultMessageInterpolator {

	private static final long serialVersionUID = -4048836282138254734L;

	@Override
	@SuppressWarnings("unchecked")
	public String interpolate(String message, Validator validator,
			org.hibernate.validator.MessageInterpolator defaultInterpolator) {
		String msg = super.interpolate(message, validator, defaultInterpolator);
		if(validator instanceof IPropertyReference) {
			IPropertyReference pr = (IPropertyReference) validator;
			msg = pr.getPropertyReference() + "|" + msg;
		}
		return msg;
	}

}
