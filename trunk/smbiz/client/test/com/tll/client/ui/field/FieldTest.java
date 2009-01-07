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
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.ValidationException;

/**
 * FieldTest - Tests the core {@link IField} methods for the {@link IField}
 * implementations.
 * @author jpk
 */
public class FieldTest extends GWTTestCase {

	static final String PROP_NAME = "propName";
	static final String EMPTY_STRING_VALUE = "value";
	static final String STRING_VALUE = "value";
	static final String LABEL_TEXT = "Label";
	static final String HELP_TEXT = "Help Text";
	static final int VISIBLE_LEN = 10;

	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	protected void validateFieldCommon(IField<?> f) throws Exception {
		assert PROP_NAME.equals(f.getName());
		assert PROP_NAME.equals(f.getPropertyName());

		// verify help text
		assert HELP_TEXT.equals(f.getHelpText());

		// test requiredness validation
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

		// test max length validation
		if(f instanceof HasMaxLength) {
			int oml = ((HasMaxLength) f).getMaxLen();
			((HasMaxLength) f).setMaxLen(2);
			try {
				f.validate();
				Assert.fail("HasMaxLength validation failed");
			}
			catch(ValidationException e) {
				// expected
			}
			((HasMaxLength) f).setMaxLen(oml);
		}
	}

	protected void validateStringField(IField<String> f) throws Exception {
		validateFieldCommon(f);

		assert EMPTY_STRING_VALUE.equals(f.getValue());
		assert EMPTY_STRING_VALUE.equals(f.getProperty(PROP_NAME));

		f.setProperty(PROP_NAME, STRING_VALUE);
		assert STRING_VALUE.equals(f.getValue());
		assert STRING_VALUE.equals(f.getProperty(PROP_NAME));

		assert STRING_VALUE.equals(f.getText());
	}

	/**
	 * Tests the {@link IField} impls whose native value is {@link String}.
	 * @throws Exception
	 */
	public void testStringFields() throws Exception {
		validateStringField(FieldFactory.ftext(PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN));
		validateStringField(FieldFactory.fpassword(PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN));
		validateStringField(FieldFactory.ftextarea(PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN, VISIBLE_LEN));
	}

	/**
	 * Tests {@link DateField}.
	 * @throws Exception
	 */
	public void testDateField() throws Exception {
		DateField f = FieldFactory.fdate(PROP_NAME, LABEL_TEXT, HELP_TEXT, GlobalFormat.DATE);
		validateFieldCommon(f);

		Date now = new Date();

		f.setValue(now);
		assert now.equals(f.getValue());

		f.setValue(Fmt.format(now, GlobalFormat.DATE));
		assert now.equals(f.getValue());
	}

	/**
	 * Test {@link CheckboxField}.
	 * @throws Exception
	 */
	public void testCheckboxField() throws Exception {
		CheckboxField f = FieldFactory.fcheckbox(PROP_NAME, LABEL_TEXT, HELP_TEXT);
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
		SuggestField f = FieldFactory.fsuggest(PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as));
		validateFieldCommon(f);

		f.setText(STRING_VALUE);
		assert STRING_VALUE.equals(f.getText());
	}

	public void testRadioGroupField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		RadioGroupField f = FieldFactory.fradiogroup(PROP_NAME, LABEL_TEXT, HELP_TEXT, Arrays.asList(as), true);
		validateFieldCommon(f);

		// TODO finish
	}

	public void testSelectField() throws Exception {
		String[] as = new String[] {
			"s1", "s2", "s3" };
		SelectField f = FieldFactory.fselect(PROP_NAME, LABEL_TEXT, HELP_TEXT, true, Arrays.asList(as));
		validateFieldCommon(f);

		// TODO finish
	}
}
