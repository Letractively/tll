/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.IField;

/**
 * IFieldPanelComposer - Implementations are responsible for:
 * <ol>
 * <li>Adding {@link IField}s to the UI
 * <li>Dictating the layout style for the {@link FieldGroupPanel}'s UI
 * </ol>
 * @author jpk
 */
public interface IFieldPanelComposer {

	/**
	 * Common style for {@link IField}s.
	 */
	static final String CSS_FIELD = "fld";

	/**
	 * @return The Widget on which {@link IField}s and supporting {@link Widget}s
	 *         are drawn.
	 */
	Widget getCanvasWidget();
}
