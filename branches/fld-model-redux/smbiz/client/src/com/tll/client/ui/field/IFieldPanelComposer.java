/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;

/**
 * IFieldPanelComposer - Marker indicating the ability to render fields onto a
 * UI "canvas". Composers of this type are responsible for:
 * <ol>
 * <li>Adding {@link IField}s to the UI
 * <li>Dictating the layout style for the {@link FieldPanel}'s UI
 * </ol>
 * @author jpk
 */
public interface IFieldPanelComposer {

	/**
	 * Sets the "canvas" on which fields are drawn.
	 * @param canvas The field UI "canvas" Panel.
	 */
	void setCanvas(Panel canvas);

	/**
	 * Adds a field to the canvas.
	 * @param field
	 */
	void addField(IField field);
}
