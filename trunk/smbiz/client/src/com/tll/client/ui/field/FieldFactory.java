/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.ui.field;

import java.util.Collection;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.RefDataMapConverter;
import com.tll.client.model.Model;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.CreditCardValidator;
import com.tll.client.validate.EmailAddressValidator;

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
	public static final TextField ftext(String name, String propName, String labelText, String helpText, int visibleLength) {
		return new TextField(name, propName, labelText, helpText, visibleLength);
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
		TextField f = new TextField(name, propName, labelText, helpText, visibleLength);
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
		TextField f = new TextField(name, propName, labelText, helpText, visibleLength);
		f.addValidator(CreditCardValidator.INSTANCE);
		return f;
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
	 * @param format
	 * @return new field
	 */
	public static final DateField fdate(String name, String propName, String labelText, String helpText,
			GlobalFormat format) {
		return new DateField(name, propName, labelText, helpText, format);
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
	 * @param <I> The option "item" (element) type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @return new field
	 */
	public static final <I> SelectField<I> fselect(String name, String propName, String labelText, String helpText,
			Collection<I> options) {
		return new SelectField<I>(name, propName, labelText, helpText, options);
	}

	/**
	 * Creates a new {@link MultiSelectField} instance.
	 * @param <I> The option "item" (element) type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @return new field
	 */
	public static final <I> MultiSelectField<I> fmultiselect(String name, String propName, String labelText,
			String helpText, Collection<I> options) {
		return new MultiSelectField<I>(name, propName, labelText, helpText, options);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param suggestions
	 * @return new field
	 */
	public static final SuggestField fsuggest(String name, String propName, String labelText, String helpText,
			Collection<? extends Object> suggestions) {
		return new SuggestField(name, propName, labelText, helpText, suggestions);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param options
	 * @param renderHorizontal
	 * @return new field
	 */
	public static final RadioGroupField fradiogroup(String name, String propName, String labelText, String helpText,
			Collection<? extends Object> options, boolean renderHorizontal) {
		return new RadioGroupField(name, propName, labelText, helpText, options, renderHorizontal);
	}

	/**
	 * Creates a new {@link SelectField} of app recognized currencies.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @return select field containing the app currencies
	 */
	public static final SelectField<String> fcurrency(String name, String propName, String labelText, String helpText) {
		Map<String, String> cm = AuxDataCache.instance().getCurrencyDataMap();
		SelectField<String> sf = FieldFactory.fselect(name, propName, labelText, helpText, cm.keySet());
		sf.setConverter(new RefDataMapConverter(cm));
		return sf;
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	public static final DateField[] entityTimestampFields() {
		DateField dateCreated =
				fdate(Model.DATE_CREATED_PROPERTY, Model.DATE_CREATED_PROPERTY, "Created", "Date Created", GlobalFormat.DATE);
		DateField dateModified =
				fdate(Model.DATE_MODIFIED_PROPERTY, Model.DATE_MODIFIED_PROPERTY, "Modified", "Date Modified",
						GlobalFormat.DATE);
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
		return ftext(Model.NAME_PROPERTY, Model.NAME_PROPERTY, "Name", "Name", 30);
	}

}
