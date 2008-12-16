/**
 * The Logic Lab
 * @author jpk
 * Dec 13, 2008
 */
package com.tll.client.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.IPropertyValue;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.IntegerValidator;
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
 * </ol>
 * <p>
 * To fully support data transfer ("binding"), the following conventions are
 * established:
 * <ol>
 * <li>Non-FieldGroup {@link IField}s contained in a FieldGroup are expected to
 * have a standard OGNL compliant property name to support binding to/from the
 * underlying Model.
 * <li>Newly created IFields that map to a non-existant related many Model are
 * expected to have a property name that indicates as much:
 * <code>indexableProp{index}.propertyName</code> as opposed to the standard:
 * <code>indexableProp[index].propertyName</code>. In other words, curly braces
 * (<code>{}</code>) are used instead of square braces (<code>[]</code>).
 * </ol>
 * @author jpk
 */
public final class FieldBinding implements IFieldBinding/*, ChangeListener, FocusListener*/{

	/**
	 * The bound field.
	 */
	private final IField field;

	/**
	 * The bound model property.
	 */
	private IPropertyValue prop;

	/**
	 * Internal flag indicating whether this binding is bound.
	 */
	private boolean bound;

	/**
	 * Ref to the field validator added as a result of binding. This ref is
	 * retained so it may be removed when unbinding occurrs.
	 */
	private IValidator bindingValidator;

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

	/**
	 * Constructor
	 * @param field
	 * @param prop
	 */
	public FieldBinding(IField field, IPropertyValue prop) {
		this(field);
		setModelProperty(prop);
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

			// set the binding (type coercion) validator
			assert bindingValidator == null;
			switch(metadata.getPropertyType()) {
				case BOOL:
					bindingValidator = BooleanValidator.INSTANCE;
					break;
				case CHAR:
					bindingValidator = CharacterValidator.INSTANCE;
					break;
				case DATE: {
					switch(getFieldFormat()) {
						case DATE:
							bindingValidator = DateValidator.DATE_VALIDATOR;
							break;
						case TIME:
							bindingValidator = DateValidator.TIME_VALIDATOR;
							break;
						default:
						case TIMESTAMP:
							bindingValidator = DateValidator.TIMESTAMP_VALIDATOR;
							break;
					}
					break;
				}
				case FLOAT:
				case DOUBLE: {
					switch(getFieldFormat()) {
						case CURRENCY:
							bindingValidator = DecimalValidator.CURRENCY_VALIDATOR;
							break;
						case PERCENT:
							bindingValidator = DecimalValidator.PERCENT_VALIDATOR;
							break;
						case DECIMAL:
							bindingValidator = DecimalValidator.DECIMAL_VALIDATOR;
							break;
					}
					break;
				}
				case INT:
				case LONG:
					bindingValidator = IntegerValidator.INSTANCE;
					break;

				case ENUM:
				case STRING:
					// no type coercion validator needed
					break;

				case STRING_MAP:
					// TODO impl
					throw new UnsupportedOperationException();

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

		if(bindingValidator != null) {
			field.addValidator(bindingValidator);
		}

		bound = true;
	}

	public void unbind() {
		if(!bound) return;

		// remove the binding validator if specified
		if(bindingValidator != null) {
			field.removeValidator(bindingValidator);
			bindingValidator = null;
		}

		bound = false;
	}

	private void ensureBound() {
		if(!bound) throw new IllegalStateException("Field binding not bound.");
	}

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

	public void pull() throws ValidationException {
		ensureBound();
		prop.setValue(field.validate());
	}
}