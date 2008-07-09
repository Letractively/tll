/**
 * The Logic Lab
 * @author jkirton
 * Jul 7, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;

/**
 * IFieldRenderer - Renders fields onto a Panel.
 * @author jpk
 */
public interface IFieldRenderer {

	/**
	 * Draws the fields onto the UI "canvas".
	 * @param canvas The UI canvas
	 * @param fieldGroup The field group holding the fields to be drawn.
	 * @param parentPropertyPath If non-<code>null</code>, this path serves as a
	 *        filter by which the target fields are identified in the given field
	 *        group.
	 */
	void draw(Panel canvas, FieldGroup fieldGroup, String parentPropertyPath);
}
