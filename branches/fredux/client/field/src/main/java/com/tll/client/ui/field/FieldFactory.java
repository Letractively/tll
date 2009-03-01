/**
 * The Logic Lab
 * @author jpk Dec 28, 2008
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.EnumToDataMapConverter;
import com.tll.client.ui.IWidgetRenderer;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.CreditCardValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.EmailAddressValidator;
import com.tll.client.validate.EnumValidator;
import com.tll.common.model.Model;
import com.tll.refdata.RefDataType;

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
		final DateField f = new DateField(name, propName, labelText, helpText, format);
		f.addValidator(DateValidator.get(format));
		return f;
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

	/**
	 * Creates a new {@link TextField} instance with email address validation.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	public static final TextField femail(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		final TextField f = ftext(name, propName, labelText, helpText, visibleLength);
		f.addValidator(EmailAddressValidator.INSTANCE);
		return f;
	}

	/**
	 * Creates a new {@link TextField} instance with credit card number
	 * validation.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	public static final TextField fcreditcard(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		final TextField f = ftext(name, propName, labelText, helpText, visibleLength);
		f.addValidator(CreditCardValidator.INSTANCE);
		return f;
	}

	/**
	 * Creates a new {@link SelectField} whose options are enumeration elements.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param type
	 * @return select field containing String-wise enum values of the given type
	 */
	public static final SelectField fenumselect(String name, String propName, String labelText, String helpText,
			final Class<? extends Enum<?>> type) {
		final SelectField f = fselect(name, propName, labelText, helpText, EnumToDataMapConverter.INSTANCE.convert(type));
		f.addValidator(new EnumValidator(type));
		return f;
	}

	/**
	 * Creates a new {@link SuggestField} whose suggestions are defined by the
	 * given {@link RefDataType}.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param refDataType
	 * @return select field containing the app currencies
	 */
	public static final SuggestField frefdata(String name, String propName, String labelText, String helpText,
			RefDataType refDataType) {
		return fsuggest(name, propName, labelText, helpText, AuxDataCache.instance().getRefDataMap(refDataType));
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	public static final TextField entityNameField() {
		return ftext(Model.NAME_PROPERTY, Model.NAME_PROPERTY, "Name", "Name", 30);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	public static final DateField[] entityTimestampFields() {
		final DateField dateCreated =
				fdate(Model.DATE_CREATED_PROPERTY, Model.DATE_CREATED_PROPERTY, "Created", "Date Created");
		final DateField dateModified =
				fdate(Model.DATE_MODIFIED_PROPERTY, Model.DATE_MODIFIED_PROPERTY, "Modified", "Date Modified");
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated, dateModified };
	}
}
