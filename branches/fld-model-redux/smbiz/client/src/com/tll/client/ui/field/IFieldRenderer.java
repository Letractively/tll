/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;

/**
 * IFieldRenderer - Renders fields onto a {@link Panel}.
 * @author jpk
 */
public interface IFieldRenderer {

	/**
	 * Renders fields onto the given panel.
	 * @param panel The panel onto which the fields are rendered
	 * @param parentPropPath The parent property path that resolves the fields in
	 *        the field group. May be <code>null<code>
	 * @param fg The field group from which fields are retrieved to render
	 */
	void render(Panel panel, String parentPropPath, FieldGroup fg);
}
