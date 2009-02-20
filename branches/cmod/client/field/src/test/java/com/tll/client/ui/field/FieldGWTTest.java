/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.ui.field;

import java.util.Arrays;
import java.util.Date;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.convert.IConverter;
import com.tll.client.convert.NoFormatStringConverter;
import com.tll.client.validate.ValidationException;

/**
 * FieldGWTTest - Tests the core {@link IField} methods for the {@link IField}
 * implementations.
 * @author jpk
 */
public class FieldGWTTest extends GWTTestCase {

	static final String PROP_NAME = "value";
	static final String EMPTY_STRING_VALUE = "";
	static final String STRING_VALUE = "value";
	static final String LABEL_TEXT = "Label";
	static final String HELP_TEXT = "Help Text";
	static final int VISIBLE_LEN = 10;

	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	protected void validateFieldCommon(IField<?, ?> f) throws Exception {
		assert PROP_NAME.equals(f.getName());
		assert PROP_NAME.equals(f.getPropertyName());

		// verify help text
		assert HELP_TEXT.equals(f.getHelpText());

		// test requiredness validation (except for checkboxes)
		if(f instanceof CheckboxField == false) {
			boolean or = f.isRequired();
			f.setRequired(true);
			try {
				f.validate();
				Assert.fail("Requiredness validation failed");
			}
			catch(ValidationException e) {
				// expected
			}
			f.setRequired(or);
		}
	}

	protected void validateStringField(IField<String, String> f) throws Exception {
		validateFieldCommon(f);

		assert null == f.getValue();
		assert null == f.getProperty(PROP_NAME);

		f.setProperty(PROP_NAME, STRING_VALUE);
		assert STRING_VALUE.equals(f.getValue());
		assert STRING_VALUE.equals(f.getProperty(PROP_NAME));

		assert STRING_VALUE.equals(f.getText());

		// test max length validation
		if(f instanceof IHasMaxLength) {
			int oml = ((IHasMaxLength) f).getMaxLen();
			String ov = f.getValue();
			f.setValue(STRING_VALUE);
			((IHasMaxLength) f).setMaxLen(2);
			try {
				f.validate();
				Assert.fail("IHasMaxLength validation failed");
			}
			catch(ValidationException e) {
				// expected
			}
			// restore state
			((IHasMaxLength) f).setMaxLen(oml);
			f.setValue(ov);
		}
	}

	/**
	 * Tests the {@link IField} impls whose native value is {@link String}.
	 * @throws Exception
	 */
	public void testStringFields() throws Exception {
		validateStringField(FieldFactory.ftext(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN,
				NoFormatStringConverter.INSTANCE));
		validateStringField(FieldFactory.fpassword(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN));
		validateStringField(FieldFactory.ftextarea(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN, VISIBLE_LEN));
	}

	/**
	 * Date to Date pass through converter.
	 */
	private static final IConverter<Date, Date> datePassThroughConverter = new IConverter<Date, Date>() {

		public Date convert(Date o) throws IllegalArgumentException {
			return o;
		}
	};

	/**
	 * Tests {@link DateField}.
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void testDateField() throws Exception {
		DateField<Date> f = FieldFactory.fdate(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, datePassThroughConverter);
		validateFieldCommon(f);

		Date now = new Date();
		now.setSeconds(0); // GWT short date format doesn't do seconds (fine)

		f.setValue(now);
		assert now.equals(f.getValue());

		f.setValue(now);
		Date fdate = f.getValue();
		// NOTE: we compare the dates as *Strings* to get around "micro-time"
		// difference that Date.setSeconds() doesn't handle
		assert now.toString().equals(fdate.toString());
	}

	/**
	 * Test {@link CheckboxField}.
	 * @throws Exception
	 */
	public void testCheckboxField() throws Exception {
		CheckboxField<Boolean> f =
				FieldFactory.fcheckbox(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, new IConverter<Boolean, Boolean>() {

					public Boolean convert(Boolean o) throws IllegalArgumentException {
						return o;
					}
				});
		validateFieldCommon(f);

		f.setValue(Boolean.TRUE);
		assert f.getValue() == Boolean.TRUE;
		assert f.getProperty(PROP_NAME).equals(Boolean.TRUE);
		assert Boolean.TRUE.toString().equals(f.getText());
		assert f.isChecked() == true;
	}

	public void testSuggestField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		SuggestField<String> f =
				FieldFactory.fsuggest(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as),
						NoFormatStringConverter.INSTANCE);
		validateFieldCommon(f);

		f.setText(STRING_VALUE);
		assert STRING_VALUE.equals(f.getText());
	}

	public void testRadioGroupField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		RadioGroupField<String> f =
				FieldFactory.fradiogroup(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as),
						NoFormatStringConverter.INSTANCE, true);
		validateFieldCommon(f);

		// TODO finish
	}

	public void testSelectField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		SelectField<String> f =
				FieldFactory.fselect(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as), SimpleComparator.INSTANCE,
						NoFormatStringConverter.INSTANCE);
		validateFieldCommon(f);

		// TODO finish
	}

	public void testMultiSelectField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		MultiSelectField<String> f =
				FieldFactory.fmultiselect(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as),
						SimpleComparator.INSTANCE, NoFormatStringConverter.INSTANCE);
		validateFieldCommon(f);

		// TODO finish
	}
}
