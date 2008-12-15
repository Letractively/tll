/**
 * The Logic Lab
 * @author jpk
 * Dec 13, 2008
 */
package com.tll.client.field;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.IPropertyValue;
import com.tll.client.msg.Msg;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.IntegerValidator;
import com.tll.client.validate.NotEmptyValidator;
import com.tll.client.validate.StringLengthValidator;
import com.tll.client.validate.ValidationException;
import com.tll.model.schema.PropertyMetadata;

/**
 * FieldBinding - IFieldBinding implementation <br>
 * Field bindings enable:
 * <ol>
 * <li>Bi-directional data transfer between UI fields and model properties.
 * <li>Field change tracking. Fields are styled when their onChange event
 * occurrs to indicate whether or not their value is dirty.
 * <li>Field validation tracking. Field's are validated when they loose focus.
 * @author jpk
 */
public final class FieldBinding implements IFieldBinding, ChangeListener, FocusListener {

	/**
	 * The bound field.
	 */
	private final IField field;

	/**
	 * The bound model property.
	 */
	private IPropertyValue prop;

	/**
	 * The optional validator.
	 */
	private IValidator validator;

	/**
	 * Internal flag indicating whether this binding is bound.
	 */
	private boolean bound;

	/**
	 * Constructor
	 * @param field The field for binding. May not be <code>null</code> and must
	 *        be a {@link Widget}.
	 */
	public FieldBinding(IField field) {
		super();
		if(field == null) throw new IllegalArgumentException("A field must be specified.");
		if(field instanceof Widget == false) throw new IllegalArgumentException("The field must be a Widget.");
		this.field = field;
	}

	public IField getField() {
		return field;
	}

	public IPropertyValue getModelProperty() {
		return prop;
	}

	public void setModelProperty(IPropertyValue prop) {
		this.prop = prop;
	}

	/**
	 * Adds a field validator to this binding.
	 * @param validator The validator to add
	 */
	public void addValidator(IValidator validator) {
		assert validator != null;
		if(this.validator == null) {
			this.validator = validator;
		}
		else if(this.validator instanceof CompositeValidator) {
			((CompositeValidator) this.validator).add(validator);
		}
		else {
			CompositeValidator cv = new CompositeValidator();
			cv.add(this.validator);
			cv.add(validator);
			this.validator = cv;
		}
	}

	/**
	 * Removes a validator from this binding.
	 * @param validator The validator to remove
	 */
	public void removeValidator(IValidator validator) {
		assert validator != null;
		if(this.validator == validator) {
			this.validator = null;
		}
		else if(this.validator instanceof CompositeValidator) {
			((CompositeValidator) this.validator).remove(validator);
		}
	}

	private GlobalFormat getFieldFormat() {
		return (field instanceof HasFormat) ? ((HasFormat) field).getFormat() : null;
	}

	public void bind() {
		assert field != null;
		if(prop == null) throw new IllegalStateException("A model property must be specified.");

		// first unbind any previous binding
		unbind();

		boolean required;
		int maxlen;

		// process model prop metadata
		final PropertyMetadata metadata = prop.getMetadata();
		if(metadata != null) {
			required = metadata.isRequired() && !metadata.isManaged();
			maxlen = metadata.getMaxLen();

			// add the type coercion validator
			switch(metadata.getPropertyType()) {
				case BOOL:
					addValidator(BooleanValidator.INSTANCE);
					break;
				case CHAR:
					addValidator(CharacterValidator.INSTANCE);
					break;
				case DATE: {
					switch(getFieldFormat()) {
						case DATE:
							addValidator(DateValidator.DATE_VALIDATOR);
							break;
						case TIME:
							addValidator(DateValidator.TIME_VALIDATOR);
							break;
						default:
						case TIMESTAMP:
							addValidator(DateValidator.TIMESTAMP_VALIDATOR);
							break;
					}
					break;
				}
				case FLOAT:
				case DOUBLE: {
					switch(getFieldFormat()) {
						case CURRENCY:
							addValidator(DecimalValidator.CURRENCY_VALIDATOR);
							break;
						case PERCENT:
							addValidator(DecimalValidator.PERCENT_VALIDATOR);
							break;
						case DECIMAL:
							addValidator(DecimalValidator.DECIMAL_VALIDATOR);
							break;
					}
					break;
				}
				case INT:
				case LONG:
					addValidator(IntegerValidator.INSTANCE);
					break;

				case ENUM:
				case STRING:
					// no type coercion validator needed
					break;

				case STRING_MAP:
					// TODO handle string map type coercion
				default:
					throw new IllegalStateException("Unhandled model property type: " + metadata.getPropertyType().name());
			}
		}
		else {
			required = false;
			maxlen = -1;
		}

		field.setRequired(required);
		if(field instanceof HasMaxLength) {
			((HasMaxLength) field).setMaxLen(maxlen);
		}

		field.addChangeListener(this);
		field.addFocusListener(this);

		bound = true;
	}

	public void unbind() {
		if(!bound) return;

		validator = null;

		field.removeFocusListener(this);
		field.removeChangeListener(this);

		bound = false;
	}

	private void ensureBound() {
		if(!bound) throw new IllegalStateException("Field binding not bound.");
	}

	/**
	 * Transfers model data to the field.
	 */
	public void push() {
		ensureBound();

		// string-ize model prop value
		final String sval = Fmt.format(prop.getValue(), getFieldFormat());
		// assign it to the field
		field.setResetValue(sval);
		field.setValue(sval);

		// TODO don't opt to do field drawing here
		// do a field re-draw (this will set the field's value to the reset value)
		// field.reset();
	}

	/**
	 * Transfers field data to the model property.
	 * @throws ValidationException When the field data is invalid for the bound
	 *         model property.
	 */
	public void pull() throws ValidationException {
		ensureBound();
		prop.setValue(validate(field.getValue()));
	}

	public Object validate(Object value) throws ValidationException {
		ensureBound();

		IValidationFeedback feedback = null;

		try {
			// "intrinsic" validation
			assert field != null;
			if(field.isRequired()) {
				value = NotEmptyValidator.INSTANCE.validate(value);
			}
			if(field instanceof HasMaxLength) {
				final int maxlen = ((HasMaxLength) field).getMaxLen();
				if(maxlen != -1) {
					value = StringLengthValidator.validate(value, -1, maxlen);
				}
			}

			// additional validation
			if(validator != null) {
				value = validator.validate(value);
			}

			return value;
		}
		catch(ValidationException ve) {
			feedback = ve;
			throw ve;
		}
		finally {
			final List<Msg> msgs = feedback == null ? null : feedback.getValidationMessages();
			field.markInvalid(msgs != null, msgs);
		}
	}

	public void onChange(Widget sender) {
		assert sender == field;
		field.markDirty(field.isDirty());
	}

	public void onFocus(Widget sender) {
		// no-op
	}

	public void onLostFocus(Widget sender) {
		assert sender == field;
		// validate the field and adjust its styling accordingly
		try {
			validate(field.getValue());
		}
		catch(ValidationException e) {
			// no-op
		}
	}
}
