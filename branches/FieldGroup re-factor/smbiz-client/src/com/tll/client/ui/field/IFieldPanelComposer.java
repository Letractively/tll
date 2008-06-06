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
	static final String CSS_FIELD = "fld";

	/**
	 * Composes the field panel
	 * @param group The field grop from which {@link IField}s that will be added
	 *        to the given Panel are gotten.
	 * @param attachPanel The Panel onto which {@link IField}s are added.
	 */
	// void composeFieldPanel(FieldGroup group, Panel attachPanel);
}
