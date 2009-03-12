/**
 * The Logic Lab
 * @author jpk
 * Feb 27, 2009
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
	 * @return the editable interface.
	 */
	IEditable<?> getEditable();

	/**
	 * Sets the property name for this field.
	 * @param propName The property name
	 */
	void setPropertyName(String propName);

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
	 * @return the current validity state.
	 */
	boolean isValid();

	/**
	 * This property is a generalized switch. When a field widget is on, it is
	 * available in the ui for full life-cycle interaction. When off, it is not
	 * visible in the ui and does <em>not</em> participate in any field related
	 * life-cycles. In other words, it serves as only as a reference when off.
	 * @return <code>true</code> if the field widget is on.
	 */
	//boolean isOn();

	/**
	 * Turn the field widget on or off.
	 * @param on
	 */
	//void turnOn(boolean on);
}
