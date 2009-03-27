/**
 * The Logic Lab
 * @author jpk Feb 27, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.IHasHelpText;
import com.tll.client.validate.IValidator;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.criteria.IPropertyNameProvider;

/**
 * IFieldWidget - A physical non-group field capable of display in the ui.
 * @param <V> the value type
 * @author jpk
 */
public interface IFieldWidget<V> extends IField, IBindableWidget<V>, IPropertyNameProvider, HasText, IHasHelpText,
		IValidator {

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
		 * Style indicating a form input element of: input[type="text"],
		 * input[type="password"], select, textarea.
		 */
		public static final String TBOX = "tbox";

		/**
		 * Specific field style applied to checkboxes and radio buttons.
		 */
		public static final String CBRB = "cbrb";

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
	 * Sets the property name for this field.
	 * @param propName The property name
	 */
	void setPropertyName(String propName);

	/**
	 * @return the editable interface.
	 */
	IEditable<?> getEditable();

	/**
	 * @return The associated {@link FieldLabel} which may be <code>null</code>.
	 */
	FieldLabel getFieldLabel();
	
	/**
	 * @return The label text.
	 */
	String getLabelText();

	/**
	 * Sets the label text.
	 * @param text the label text
	 */
	void setLabelText(String text);

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
	 * @return the current validity state.
	 */
	boolean isValid();
}
