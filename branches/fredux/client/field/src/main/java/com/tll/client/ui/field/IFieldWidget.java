/**
 * The Logic Lab
 * @author jpk
 * Feb 27, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.IHasHelpText;

/**
 * IFieldWidget - A physical non-group field capable of display in the ui.
 * @param <T> the value type
 * @author jpk
 */
public interface IFieldWidget<T> extends IField<T>, HasText, IHasHelpText {

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
}
