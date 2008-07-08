/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.field;

import com.tll.client.model.IData;
import com.tll.client.model.IPropertyBinding;
import com.tll.client.model.IPropertyNameProvider;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;

/**
 * IField - Abstraction for managing the display editing of model bound data.
 * @author jpk
 */
public interface IField extends IPropertyNameProvider, IData {

	/**
	 * Common style for {@link IField}s.
	 */
	static final String CSS_FIELD = "fld";

	/**
	 * Sets the property name for this field.
	 * @param propName The property name
	 */
	void setPropertyName(String propName);

	/**
	 * @return <code>true</code> if this field is required.
	 */
	boolean isRequired();

	/**
	 * Sets the field's required property.
	 * @param required
	 */
	void setRequired(boolean required);

	/**
	 * @return <code>true</code> if this field is read only.
	 */
	boolean isReadOnly();

	/**
	 * Sets the field's read-only property.
	 * @param readOnly
	 */
	void setReadOnly(boolean readOnly);

	/**
	 * @return <code>true</code> if this field is able to be edited.
	 */
	boolean isEnabled();

	/**
	 * Sets the field's enabled property.
	 * @param enabled
	 */
	void setEnabled(boolean enabled);

	/**
	 * Is the field visible?
	 * @return true/false
	 */
	boolean isVisible();

	/**
	 * Sets the field's visibility.
	 * @param visible true/false
	 */
	void setVisible(boolean visible);

	/**
	 * @return The field's value.
	 */
	String getValue();

	/**
	 * Sets the field's value.
	 * <p>
	 * NOTE: To clear the field, pass <code>null</code> as the value.
	 * @param value
	 */
	void setValue(String value);

	/**
	 * Resets the field's UI value to the default.
	 */
	void reset();

	/**
	 * Updates the field's current reset value and removes any edit styling that
	 * would indicate the field is in a "changed" state.
	 */
	void markReset();

	/**
	 * Clears the value setting it to any set default.
	 */
	void clear();

	/**
	 * Binds model data to this field.
	 * @param binding The model to bind.
	 */
	void bindModel(IPropertyBinding binding);

	/**
	 * Updates the given model.
	 * @param binding The model to update.
	 * @return <code>true</code> if the model was successfully altered.
	 */
	boolean updateModel(IPropertyBinding binding);

	/**
	 * Adds a validator to the field.
	 * @param validator The validator to add
	 */
	void addValidator(IValidator validator);

	/**
	 * Validates the field.
	 * @throws ValidationException When found invalid.
	 */
	void validate() throws ValidationException;

	/**
	 * Handles validation feedback.
	 * @param feedback The validation feedback
	 */
	void handleValidationFeedback(IValidationFeedback feedback);
}
