/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

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
	public static final String CSS_FIELD = "fld";

	/**
	 * Adds a field to the canvas. The field label is extracted from the given
	 * field and if non-<code>null</code>, is added as well.
	 * @param field The field to add
	 */
	void addField(AbstractField field);
}
