/**
 * The Logic Lab
 * @author jkirton
 * Jul 7, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;

/**
 * IFieldRenderer - Renders fields onto a Panel.
 * @author jpk
 */
public interface IFieldRenderer {

	/**
	 * Draws the fields onto the UI "canvas".
	 * @param canvas The UI canvas
	 */
	void draw(Panel canvas);
}
