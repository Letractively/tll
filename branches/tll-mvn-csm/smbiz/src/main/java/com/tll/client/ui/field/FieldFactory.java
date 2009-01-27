/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.ui.field;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import com.tll.client.convert.IConverter;
import com.tll.client.convert.NoFormatStringConverter;

/**
 * FieldFactory
 * @author jpk
 */
public abstract class FieldFactory {

	/**
	 * Creates a new {@link TextField} instance.
	 * @param <B> the bound type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @param converter
	 * @return new field
	 */
	public static final <B> TextField<B> ftext(String name, String propName, String labelText, String helpText,
			int visibleLength, IConverter<String, B> converter) {
		return new TextField<B>(name, propName, labelText, helpText, visibleLength, converter);
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
	public static final PasswordField<String> fpassword(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		return new PasswordField<String>(name, propName, labelText, helpText, visibleLength,
				NoFormatStringConverter.INSTANCE);
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param <B> The bound type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param converter
	 * @return new field
	 */
	public static final <B> DateField<B> fdate(String name, String propName, String labelText, String helpText,
			IConverter<Date, B> converter) {
		return new DateField<B>(name, propName, labelText, helpText, converter);
	}

	/**
	 * Creates a Check box field that is designed to be bound to a boolean type
	 * using String-wise constants "true" and "false" to indicate the boolean
	 * value respectively.
	 * @param <B> The bound type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param converter
	 * @return new field
	 */
	public static final <B> CheckboxField<B> fcheckbox(String name, String propName, String labelText, String helpText,
			IConverter<Boolean, B> converter) {
		return new CheckboxField<B>(name, propName, labelText, helpText, converter);
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
	public static final TextAreaField<String> ftextarea(String name, String propName, String labelText, String helpText,
			int numRows, int numCols) {
		return new TextAreaField<String>(name, propName, labelText, helpText, numRows, numCols,
				NoFormatStringConverter.INSTANCE);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param <I> The option "item" (element) type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @param comparator
	 * @param itemConverter
	 * @return new field
	 */
	public static final <I> SelectField<I> fselect(String name, String propName, String labelText, String helpText,
			Collection<I> options, Comparator<Object> comparator, IConverter<String, I> itemConverter) {
		return new SelectField<I>(name, propName, labelText, helpText, options, comparator, itemConverter);
	}

	/**
	 * Creates a new {@link MultiSelectField} instance.
	 * @param <I> The option "item" (element) type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param options
	 * @param itemComparator
	 * @param itemConverter
	 * @return new field
	 */
	public static final <I> MultiSelectField<I> fmultiselect(String name, String propName, String labelText,
			String helpText, Collection<I> options, Comparator<Object> itemComparator, IConverter<String, I> itemConverter) {
		return new MultiSelectField<I>(name, propName, labelText, helpText, options, itemComparator, itemConverter);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param <B> The bound type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param suggestions
	 * @param converter
	 * @return new field
	 */
	public static final <B> SuggestField<B> fsuggest(String name, String propName, String labelText, String helpText,
			Collection<B> suggestions, IConverter<String, B> converter) {
		return new SuggestField<B>(name, propName, labelText, helpText, suggestions, converter);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param <B> The bound type
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param options
	 * @param converter
	 * @param renderHorizontal
	 * @return new field
	 */
	public static final <B> RadioGroupField<B> fradiogroup(String name, String propName, String labelText,
			String helpText, Collection<B> options, IConverter<String, B> converter, boolean renderHorizontal) {
		return new RadioGroupField<B>(name, propName, labelText, helpText, options, converter, renderHorizontal);
	}

}
