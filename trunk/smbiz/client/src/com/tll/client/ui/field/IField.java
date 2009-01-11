/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.model.IPropertyNameProvider;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.IHasHelpText;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IField - Abstraction for managing the display and editing of data.
 * <p>
 * <em><b>NOTE: </b>fields are considered equal only if their property names are the same.</em>
 * @param <V> The native field value type (usu. a String but not limited to it)
 * @author jpk
 */
public interface IField<V> extends IPropertyNameProvider, SourcesChangeEvents, HasName, HasText, IHasHelpText,
		IBindable, IValidator {

	/**
	 * Style indicating a UI artifact is a field or that its children are.
	 */
	static final String STYLE_FIELD = "fld";

	/**
	 * Style indicating a field label.
	 */
	public static final String STYLE_FIELD_LABEL = "lbl";

	/**
	 * Style indicating a field's requiredness.
	 */
	public static final String STYLE_FIELD_REQUIRED_TOKEN = "rqd";

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
	 * @return The field value.
	 */
	V getValue();

	/**
	 * Sets the field's value.
	 * @param value
	 */
	void setValue(Object value);

	/**
	 * Clears the field's value.
	 */
	void clear();

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
	 * @return The field Widget.
	 */
	Widget getWidget();

	/**
	 * @return The associated {@link FieldLabel} which may be <code>null</code>.
	 */
	FieldLabel getFieldLabel();

	/**
	 * Sets the ancestor Widget that contains this field.
	 * @param fieldContainer The desired ancestor {@link Widget}
	 */
	void setFieldContainer(Widget fieldContainer);

	/**
	 * Sets the ancestor Widget for this field's label {@link Widget}.
	 * @param fieldLabelContainer The desired ancestor {@link Widget}
	 */
	void setFieldLabelContainer(Widget fieldLabelContainer);

	/**
	 * Applies property metadata to this field.
	 * @param provider The property metadata provider.
	 */
	void applyPropertyMetadata(IPropertyMetadataProvider provider);

	/**
	 * Adds a validator.
	 * @param validator The validtor to add
	 */
	void addValidator(IValidator validator);

	/**
	 * Removes a validator.
	 * @param validator The validtor to remove
	 */
	void removeValidator(IValidator validator);

	/**
	 * Validates the field's held value.
	 * @throws ValidationException When invalid
	 */
	void validate() throws ValidationException;

	/**
	 * Sets the <em>aggregated</em> {@link PropertyChangeSupport} instance
	 * relative to a designated <em>root</em> {@link IField}.
	 * <p>
	 * <em><b>IMPT: </b>This method <em>must</em> be called prior to employing any
	 * methods on {@link ISourcesPropertyChangeEvents}!</em>
	 * @param changeSupport The change support ref
	 */
	// void setPropertyChangeSupport(PropertyChangeSupport changeSupport);
}
