/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.field;

import com.tll.client.model.IPropertyNameProvider;
import com.tll.client.model.Model;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;

/**
 * IField - Abstraction for managing the display editing of model bound data.
 * @author jpk
 */
public interface IField extends IPropertyNameProvider {

	/**
	 * LabelMode
	 * @author jpk
	 */
	enum LabelMode {
		NONE,
		ABOVE,
		LEFT;
	}

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
	 * Renders the UI control.
	 */
	// TODO eliminate and rely on Widget.onLoad() method for the rendering hook
	void render();

	/**
	 * Resets the field's UI value to the default.
	 */
	void reset();

	/**
	 * Clears the value setting it to any set default.
	 */
	void clear();

	/**
	 * Binds the model data to this field.
	 * @param model The model to bind.
	 */
	void bindModel(Model model);

	/**
	 * Updates the given model.
	 * @param model The model to update.
	 * @return <code>true</code> if the model was successfully altered.
	 */
	boolean updateModel(Model model);

	/**
	 * Adds a validator to the field.
	 * @param validator The validator to add
	 */
	void addValidator(IValidator validator);

	/**
	 * @return The validator or validators bound to this field aggregated through
	 *         the {@link IValidator} interface.
	 */
	IValidator getValidators();

	/**
	 * Handles validation feedback.
	 * @param feedback The validation feedback
	 */
	void handleValidationFeedback(IValidationFeedback feedback);
}
