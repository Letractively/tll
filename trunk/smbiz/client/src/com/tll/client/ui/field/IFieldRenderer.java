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
	 * @param fieldGroup
	 * @param parentPropertyPath
	 */
	void draw(Panel canvas, FieldGroup fieldGroup, String parentPropertyPath);
}
