/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.ui.field;

import java.util.Collection;

import com.tll.client.model.Model;
import com.tll.client.util.GlobalFormat;

/**
 * FieldFactory
 * @author jpk
 */
public abstract class FieldFactory {

	/**
	 * Creates a new {@link TextField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 */
	public static final TextField ftext(String propName, String labelText, String helpText, int visibleLength) {
		return new TextField(propName, labelText, helpText, visibleLength);
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param format
	 */
	public static final DateField fdate(String propName, String labelText, String helpText, GlobalFormat format) {
		return new DateField(propName, labelText, helpText, format);
	}

	/**
	 * Creates a Check box field that is designed to be bound to a boolean type
	 * using String-wise constants "true" and "false" to indicate the boolean
	 * value respectively.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 */
	public static final CheckboxField fcheckbox(String propName, String labelText, String helpText) {
		return new CheckboxField(propName, labelText, helpText);
	}

	/**
	 * Creates a new {@link TextAreaField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param numRows
	 * @param numCols
	 */
	public static final TextAreaField ftextarea(String propName, String labelText, String helpText, int numRows,
			int numCols) {
		return new TextAreaField(propName, labelText, helpText, numRows, numCols);
	}

	/**
	 * Creates a new {@link PasswordField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 */
	public static final PasswordField fpassword(String propName, String labelText, String helpText) {
		return new PasswordField(propName, labelText, helpText);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param multipleSelect
	 * @param options
	 */
	public static final SelectField fselect(String propName, String labelText, String helpText, boolean multipleSelect,
			Collection<? extends Object> options) {
		return new SelectField(propName, labelText, helpText, multipleSelect, options);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param suggestions
	 */
	public static final SuggestField fsuggest(String propName, String labelText, String helpText,
			Collection<? extends Object> suggestions) {
		return new SuggestField(propName, labelText, helpText, suggestions);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param options
	 * @param renderHorizontal
	 */
	public static final RadioGroupField fradiogroup(String propName, String labelText, String helpText,
			Collection<? extends Object> options, boolean renderHorizontal) {
		return new RadioGroupField(propName, labelText, helpText, options, renderHorizontal);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	public static final DateField[] entityTimestampFields() {
		DateField dateCreated = fdate(Model.DATE_CREATED_PROPERTY, "Created", "Date Created", GlobalFormat.DATE);
		DateField dateModified = fdate(Model.DATE_MODIFIED_PROPERTY, "Modified", "Date Modified", GlobalFormat.DATE);
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated, dateModified };
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	public static final TextField entityNameField() {
		return ftext(Model.NAME_PROPERTY, "Name", "Name", 30);
	}

}
