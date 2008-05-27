/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.IField;

/**
 * IFieldCanvas - Abstraction for handling differing layout styles for
 * {@link Panel}s containing {@link IField}s. <br>
 * Fields are "drawn" onto a "canvas".
 * @author jpk
 */
public interface IFieldCanvas {

	/**
	 * Adds a field label and Widget to the canvas. If the label text is
	 * <code>null</code>, no label is added. If the Widget is an IField,
	 * {@link #addField(AbstractField)} should be called instead.
	 * @param label The label text
	 * @param w The widget to add
	 */
	void addWidget(String label, Widget w);

	/**
	 * Adds a field to the canvas. The field label is extracted from the given
	 * field and if non-<code>null</code>, is added as well.
	 * @param field The field to add
	 */
	void addField(AbstractField field);
}
