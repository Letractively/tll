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
	 * @param fg The field group from which fields are retrieved to render
	 * @param parentPropPath Optional parent property path that serves to resolve
	 *        target fields in the given field group.
	 */
	void render(Panel panel, FieldGroup fg, String parentPropPath);
}
