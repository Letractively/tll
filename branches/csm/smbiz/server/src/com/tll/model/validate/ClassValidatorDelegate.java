/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.hibernate.validator.MessageInterpolator;
import org.hibernate.validator.Validator;

import com.tll.model.IEntity;

/**
 * ClassValidatorDelegate - Necessary wrapper around {@link ClassValidator} in
 * order to facilitate bean level validation annotations that target a single
 * bean property.
 * @author jpk
 */
public class ClassValidatorDelegate<E extends IEntity> implements IEntityValidator<E> {

	private final ClassValidator<E> classValidator;

	/**
	 * PropRefInterpolator - Custom interpolator to facilitate property reference
	 * handling.
	 * @author jpk
	 */
	static class PropRefInterpolator implements MessageInterpolator {

		private static final long serialVersionUID = -4048836282138254734L;

		@SuppressWarnings("unchecked")
		public String interpolate(String message, Validator validator,
				org.hibernate.validator.MessageInterpolator defaultInterpolator) {
			String msg = defaultInterpolator.interpolate(message, validator, null);
			if(validator instanceof IPropertyReference) {
				IPropertyReference pr = (IPropertyReference) validator;
				msg = pr.getPropertyReference() + "|" + msg;
			}
			return msg;
		}

	}

	private static final PropRefInterpolator interpolator = new PropRefInterpolator();

	/**
	 * Constructor
	 * @param entityType
	 */
	public ClassValidatorDelegate(Class<E> entityType) {
		super();
		if(entityType == null) {
			throw new IllegalArgumentException("The entity type argument can not be null");
		}
		this.classValidator = new ClassValidator<E>(entityType, interpolator);
	}

	/**
	 * The single validation method available to the public
	 * @param e The entity to be validated.
	 * @throws InvalidStateException When the entity is invalid.
	 */
	public void validate(E e) throws InvalidStateException {
		InvalidValue[] ivs = classValidator.getInvalidValues(e);
		if(ivs != null && ivs.length > 0) {
			List<InvalidValue> list = new ArrayList<InvalidValue>();
			// extract the property reference from the interpoloated message (if
			// present)
			for(InvalidValue iv : ivs) {
				String msg = iv.getMessage();
				final int indx = msg.indexOf("|");
				if(indx > 0) {
					// assume we have a prefixed property name
					// FORMAT: {property name}|{interpoloated message}
					String imsg = msg.substring(indx + 1);
					String property = msg.substring(0, indx);
					InvalidValue niv = new InvalidValue(imsg, iv.getBeanClass(), property, iv.getValue(), iv.getBean());
					String parentPropPath = iv.getPropertyPath();
					if(parentPropPath != null) {
						parentPropPath = parentPropPath.substring(0, parentPropPath.lastIndexOf('.'));
						niv.addParentBean(iv.getRootBean(), parentPropPath);
					}
					list.add(niv);
				}
				else {
					list.add(iv);
				}
			}
			ivs = list.toArray(new InvalidValue[list.size()]);
		}
		if(ivs != null && ivs.length > 0) {
			throw new InvalidStateException(ivs);
		}
	}
}
