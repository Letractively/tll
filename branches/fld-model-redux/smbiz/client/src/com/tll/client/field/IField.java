/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.field;

import java.util.List;

import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.tll.client.model.IPropertyNameProvider;
import com.tll.client.msg.Msg;
import com.tll.client.msg.Msg.MsgLevel;

/**
 * IField - Abstraction for managing the display editing of model bound data.
 * @author jpk
 */
public interface IField extends IPropertyNameProvider, SourcesFocusEvents, SourcesChangeEvents {

	/**
	 * Style indicating a UI artifact is a field or that its children are.
	 */
	static final String STYLE_FIELD = "fld";

	/**
	 * Style indicating the field's value is dirty (changed).
	 */
	static final String STYLE_DIRTY = "dirty";

	/**
	 * Style indicating the field's value is invalid.
	 */
	static final String STYLE_INVALID = MsgLevel.ERROR.getName().toLowerCase();

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
	 * @return <code>true</code> if this field is not disabled.
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
	 * @param value The value to set
	 */
	void setValue(String value);

	/**
	 * Gets the reset value.
	 */
	String getResetValue();

	/**
	 * Sets the reset value.
	 * @param resetValue The reset value to set
	 */
	void setResetValue(String resetValue);

	/**
	 * Resets the field's UI value to the last set reset value clearing out dirty
	 * styling, validation styling and messages.
	 */
	void reset();

	/**
	 * Draws or re-draws the field in the UI.
	 */
	void draw();

	/**
	 * Does the current field value differ from the reset value?
	 */
	boolean isDirty();

	/**
	 * Styles the field as dirty or removes this styling.
	 * @param dirty Mark or unmark as dirty?
	 */
	void markDirty(boolean dirty);

	/**
	 * Styles the field as either valid or invalid.
	 * @param invalid Mark or unmark as invalid
	 * @param msgs Optional collection of messages to show near the field when
	 *        marking as invalid. May be <code>null</code>. This param is ignored
	 *        when the <code>invalid</code> param is <code>false</code>.
	 */
	void markInvalid(boolean invalid, List<Msg> msgs);
}
