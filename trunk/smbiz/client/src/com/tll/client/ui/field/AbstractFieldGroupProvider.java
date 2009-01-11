/**
 * The Logic Lab
 * @author jpk
 * Jan 5, 2009
 */
package com.tll.client.ui.field;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.BooleanPassThroughConverter;
import com.tll.client.convert.DatePassThroughConverter;
import com.tll.client.convert.EnumToStringConverter;
import com.tll.client.convert.IFormattedConverter;
import com.tll.client.convert.NoFormatStringConverter;
import com.tll.client.convert.RefDataMapConverter;
import com.tll.client.model.Model;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.util.SimpleComparator;
import com.tll.client.validate.CreditCardValidator;
import com.tll.client.validate.EmailAddressValidator;
import com.tll.service.app.RefDataType;

/**
 * AbstractFieldGroupProvider - Common base class for all
 * {@link IFieldGroupProvider} impls.
 * @author jpk
 */
public abstract class AbstractFieldGroupProvider implements IFieldGroupProvider {

	/**
	 * Converts a {@link Number} to a {@link String} instance with currency
	 * formatting.
	 */
	private static final IFormattedConverter<String, Number> currencyToStringConverter =
			new IFormattedConverter<String, Number>() {

				public String convert(Number o) throws IllegalArgumentException {
					return Fmt.currency(o.doubleValue());
				}

				public GlobalFormat getFormat() {
					return GlobalFormat.CURRENCY;
				}
			};

	/**
	 * Creates a new {@link TextField} instance whose binding type is
	 * {@link String}.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	protected static final TextField<String> fstext(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		return FieldFactory.ftext(name, propName, labelText, helpText, visibleLength, NoFormatStringConverter.INSTANCE);
	}

	/**
	 * Creates a new {@link CheckboxField} instance whose binding type is
	 * {@link String}.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @return
	 */
	protected static final CheckboxField<Boolean> fbool(String name, String propName, String labelText, String helpText) {
		return FieldFactory.fcheckbox(name, propName, labelText, helpText, BooleanPassThroughConverter.INSTANCE);
	}

	/**
	 * Creates a new {@link TextField} instance whose binding type is {@link Long}
	 * with currency formatting.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param visibleLen
	 * @return new {@link TextField}
	 */
	protected static final TextField<Number> fmoney(String name, String propName, String labelText, String helpText,
			int visibleLen) {
		return FieldFactory.ftext(name, propName, labelText, helpText, visibleLen, currencyToStringConverter);
	}

	/**
	 * Creates a {@link DateField} whose binding type is {@link Date}.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @return new instance
	 */
	protected static final DateField<Date> fddate(String name, String propName, String labelText, String helpText) {
		return FieldFactory.fdate(name, propName, labelText, helpText, DatePassThroughConverter.INSTANCE);
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
	protected static final TextField<String> femail(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		TextField<String> f = fstext(name, propName, labelText, helpText, visibleLength);
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
	protected static final TextField<String> fcreditcard(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		TextField<String> f = fstext(name, propName, labelText, helpText, visibleLength);
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
	protected static final <E extends Enum<?>> SelectField<E> fenumselect(String name, String propName, String labelText,
			String helpText, Class<E> type) {
		return FieldFactory.fselect(name, propName, labelText, helpText, Arrays.asList(type.getEnumConstants()),
				SimpleComparator.INSTANCE, new EnumToStringConverter<E>());
	}

	/**
	 * Creates a new {@link SelectField} of app recognized currencies.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @return select field containing the app currencies
	 */
	protected static final SelectField<String> fcurrencies(String name, String propName, String labelText, String helpText) {
		Map<String, String> cm = AuxDataCache.instance().getCurrencyDataMap();
		return FieldFactory.fselect(name, propName, labelText, helpText, cm.keySet(), SimpleComparator.INSTANCE,
				new RefDataMapConverter(cm));
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
	protected static final SuggestField<String> frefdata(String name, String propName, String labelText, String helpText,
			RefDataType refDataType) {
		Map<String, String> cm = AuxDataCache.instance().getRefDataMap(refDataType);
		return FieldFactory.fsuggest(name, propName, labelText, helpText, cm.keySet(), new RefDataMapConverter(cm));
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	private static final TextField<String> entityNameField() {
		return fstext(Model.NAME_PROPERTY, Model.NAME_PROPERTY, "Name", "Name", 30);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	@SuppressWarnings("unchecked")
	private static final DateField<Date>[] entityTimestampFields() {
		DateField<Date> dateCreated =
				fddate(Model.DATE_CREATED_PROPERTY, Model.DATE_CREATED_PROPERTY, "Created", "Date Created");
		DateField<Date> dateModified =
				fddate(Model.DATE_MODIFIED_PROPERTY, Model.DATE_MODIFIED_PROPERTY, "Modified", "Date Modified");
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated, dateModified };
	}

	public final FieldGroup getFieldGroup() {
		FieldGroup fg = new FieldGroup();
		populateFieldGroup(fg);
		return fg;
	}

	/**
	 * Populates the given field group.
	 * @param fg The field group
	 */
	protected abstract void populateFieldGroup(FieldGroup fg);

	/**
	 * Helper method that adds commonly employed fields corresponding to common
	 * model properties.
	 * @param fg The field group to which fields are added
	 * @param name Add the common model name field?
	 * @param timestamping Add the commoon model timestamping (date created, date
	 *        modified) fields?
	 */
	protected void addModelCommon(FieldGroup fg, boolean name, boolean timestamping) {
		if(name) {
			fg.addField(entityNameField());
		}
		if(timestamping) {
			fg.addFields(entityTimestampFields());
		}
	}
}
