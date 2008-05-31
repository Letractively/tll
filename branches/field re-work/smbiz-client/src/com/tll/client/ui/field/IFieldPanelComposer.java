/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.IField;

/**
 * IFieldPanelComposer - Implementations are responsible for:
 * <ol>
 * <li>Adding {@link IField}s to the UI
 * <li>Dictating the layout style for the {@link FieldModelBinding}'s UI
 * </ol>
 * @author jpk
 */
public interface IFieldPanelComposer {

	/**
	 * Common style for {@link IField}s.
	 */
	static final String CSS_FIELD = "fld";

	/**
	 * Gets the Panel on which the bound {@link IField}s have been drawn.
	 */
	Panel getFieldPanel();
}
