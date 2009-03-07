/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasName;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IField - Abstraction for managing the display and editing of data.
 * <p>
 * <em><b>NOTE: </b>fields are considered equal only if their names are the same.</em>
 * @author jpk
 */
public interface IField extends HasName, IWidgetRef {

	/**
	 * Styles - (field.css)
	 * @author jpk
	 */
	static final class Styles {

		/**
		 * Style indicating a UI artifact is a field.
		 */
		public static final String FIELD = "fld";

		/**
		 * Style for field labels.
		 */
		public static final String LABEL = "lbl";

		/**
		 * Style indicating a field's requiredness.
		 */
		public static final String REQUIRED = "rqd";

		/**
		 * Style indicating the field's value is dirty (changed).
		 */
		public static final String DIRTY = "dirty";

		/**
		 * Style indicating the field's value is invalid.
		 */
		public static final String INVALID = MsgLevel.ERROR.getName().toLowerCase();

		/**
		 * Style for disabling a field.
		 */
		public static final String DISABLED = "disabled";

	} // Styles

	/**
	 * Sets the validation handler.
	 * @param errorHandler
	 */
	void setErrorHandler(IErrorHandler errorHandler);

	/**
	 * Clears the field's value.
	 */
	void clearValue();

	/**
	 * Resets the field's value to that which was when originally set.
	 */
	void reset();

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
	 * Validates the field's state.
	 * @throws ValidationException When invalid
	 */
	void validate() throws ValidationException;
}
