/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.tll.client.ui.IWidgetRenderer;
import com.tll.client.util.GlobalFormat;

/**
 * FieldFactory
 * @author jpk
 */
public abstract class FieldFactory {

	/**
	 * Creates a new {@link TextField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	public static final TextField ftext(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		return new TextField(name, propName, labelText, helpText, visibleLength);
	}

	/**
	 * Creates a new {@link PasswordField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	public static final PasswordField fpassword(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		return new PasswordField(name, propName, labelText, helpText, visibleLength);
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @return new field
	 */
	public static final DateField fdate(String name, String propName, String labelText, String helpText) {
		return new DateField(name, propName, labelText, helpText, null);
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param format the date format which defaults to {@link GlobalFormat#DATE}.
	 * @return new field
	 */
	public static final DateField fdate(String name, String propName, String labelText, String helpText,
			GlobalFormat format) {
		return new DateField(name, propName, labelText, helpText, (format == null ? GlobalFormat.DATE : format));
	}

	/**
	 * Creates a Check box field that is designed to be bound to a boolean type
	 * using String-wise constants "true" and "false" to indicate the boolean
	 * value respectively.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @return new field
	 */
	public static final CheckboxField fcheckbox(String name, String propName, String labelText, String helpText) {
		return new CheckboxField(name, propName, labelText, helpText);
	}

	/**
	 * Creates a new {@link TextAreaField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param numRows
	 * @param numCols
	 * @return new field
	 */
	public static final TextAreaField ftextarea(String name, String propName, String labelText, String helpText,
			int numRows, int numCols) {
		return new TextAreaField(name, propName, labelText, helpText, numRows, numCols);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param data
	 * @return new field
	 */
	public static final SelectField fselect(String name, String propName, String labelText, String helpText,
			Map<String, String> data) {
		return new SelectField(name, propName, labelText, helpText, data);
	}

	/**
	 * Creates a new {@link MultiSelectField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param data
	 * @return new field
	 */
	public static final MultiSelectField fmultiselect(String name, String propName, String labelText, String helpText,
			Map<String, String> data) {
		return new MultiSelectField(name, propName, labelText, helpText, data);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param data
	 * @return new field
	 */
	public static final SuggestField fsuggest(String name, String propName, String labelText, String helpText,
			Map<String, String> data) {
		return new SuggestField(name, propName, labelText, helpText, data);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param data name/value pairs where the map is keyed by the name
	 * @param renderer
	 * @return new field
	 */
	public static final RadioGroupField fradiogroup(String name, String propName, String labelText, String helpText,
			Map<String, String> data, IWidgetRenderer renderer) {
		return new RadioGroupField(name, propName, labelText, helpText, renderer, data);
	}

}
