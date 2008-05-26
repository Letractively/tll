/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.IField;

/**
 * IFieldCanvas - Abstraction for handling differing layout styles for
 * {@link Panel}s containing {@link IField}s. <br>
 * Fields are "drawn" onto a "canvas".
 * @author jpk
 */
public interface IFieldCanvas {

	/**
	 * Shows a field. Relevant when the field was previously hidden.
	 * @param field The field to show
	 */
	void showField(AbstractField field);

	/**
	 * Hides a field.
	 * @param field The field to hide
	 */
	void hideField(AbstractField field);
}
