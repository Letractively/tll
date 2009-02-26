/**
 * The Logic Lab
 * @author jpk
 * Jan 5, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.EnumToDataMapConverter;
import com.tll.client.validate.CreditCardValidator;
import com.tll.client.validate.EmailAddressValidator;
import com.tll.common.model.Model;
import com.tll.refdata.RefDataType;

/**
 * AbstractFieldGroupProvider - Common base class for all
 * {@link IFieldGroupProvider} impls.
 * @author jpk
 */
public abstract class AbstractFieldGroupProvider implements IFieldGroupProvider {

	/**
	 * Creates a new {@link TextField} instance with email address validation.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText The on hover tool tip text
	 * @param visibleLength
	 * @return new field
	 */
	protected static final TextField femail(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		final TextField f = FieldFactory.ftext(name, propName, labelText, helpText, visibleLength);
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
	protected static final TextField fcreditcard(String name, String propName, String labelText, String helpText,
			int visibleLength) {
		final TextField f = FieldFactory.ftext(name, propName, labelText, helpText, visibleLength);
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
	protected static final SelectField fenumselect(String name, String propName, String labelText, String helpText,
			Class<? extends Enum<?>> type) {
		return FieldFactory.fselect(name, propName, labelText, helpText, EnumToDataMapConverter.INSTANCE
				.convert(type));
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
	protected static final SuggestField frefdata(String name, String propName, String labelText, String helpText,
			RefDataType refDataType) {
		return FieldFactory.fsuggest(name, propName, labelText, helpText, AuxDataCache.instance()
				.getRefDataMap(refDataType));
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	private static final TextField entityNameField() {
		return FieldFactory.ftext(Model.NAME_PROPERTY, Model.NAME_PROPERTY, "Name", "Name", 30);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	private static final DateField[] entityTimestampFields() {
		final DateField dateCreated =
			FieldFactory.fdate(Model.DATE_CREATED_PROPERTY, Model.DATE_CREATED_PROPERTY, "Created", "Date Created");
		final DateField dateModified =
			FieldFactory.fdate(Model.DATE_MODIFIED_PROPERTY, Model.DATE_MODIFIED_PROPERTY, "Modified", "Date Modified");
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated, dateModified };
	}

	public final FieldGroup getFieldGroup() {
		final FieldGroup fg = new FieldGroup(getFieldGroupName());
		populateFieldGroup(fg);
		return fg;
	}

	/**
	 * @return The name to ascribe to the provided field group.
	 */
	protected abstract String getFieldGroupName();

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
