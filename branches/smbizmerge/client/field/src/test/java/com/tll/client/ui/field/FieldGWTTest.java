/**
 * The Logic Lab
 * @author jpk Jan 6, 2009
 */
package com.tll.client.ui.field;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.ui.GridRenderer;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.ValidationException;
import com.tll.util.StringUtil;

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
		return "com.tll.FieldTest";
	}

	protected static void validateFieldCommon(IFieldWidget<?> f) throws Exception {
		assert PROP_NAME.equals(f.getName());
		assert PROP_NAME.equals(f.getPropertyName());

		// verify help text
		assert HELP_TEXT.equals(f.getHelpText());

		// test requiredness validation (except for checkboxes)
		if(f instanceof CheckboxField == false) {
			final boolean or = f.isRequired();
			f.setRequired(true);
			try {
				f.validate();
				Assert.fail("Requiredness validation failed");
			}
			catch(final ValidationException e) {
				// expected
			}
			f.setRequired(or);
		}
	}

	protected static void validateStringField(IFieldWidget<String> f) throws Exception {
		validateFieldCommon(f);

		assert StringUtil.isEmpty(f.getValue());

		f.setValue(STRING_VALUE);
		assert STRING_VALUE.equals(f.getValue());

		assert STRING_VALUE.equals(f.getText());

		// test max length validation
		if(f instanceof IHasMaxLength) {
			final int oml = ((IHasMaxLength) f).getMaxLen();
			final String ov = f.getValue();
			f.setValue(STRING_VALUE);
			((IHasMaxLength) f).setMaxLen(2);
			try {
				f.validate();
				Assert.fail("IHasMaxLength validation failed");
			}
			catch(final ValidationException e) {
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
		validateStringField(FieldFactory.ftext(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN));
		validateStringField(FieldFactory.fpassword(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN));
		validateStringField(FieldFactory.ftextarea(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, VISIBLE_LEN, VISIBLE_LEN));
	}

	/**
	 * Tests {@link DateField}.
	 * @throws Exception
	 */
	//@SuppressWarnings("deprecation")
	public void testDateField() throws Exception {
		final DateField f = FieldFactory.fdate(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, GlobalFormat.DATE);
		validateFieldCommon(f);

		// TODO get a handle on how the f*** DateBox handles dates.
		/*
		final Date now = new Date();
		now.setSeconds(0); // GWT short date format doesn't do seconds (fine)

		f.setValue(now);
		Date fdate = f.getValue();
		fdate.setSeconds(0);
		assert now.equals(fdate);

		f.setValue(now);
		fdate = f.getValue();
		// NOTE: we compare the dates as *Strings* to get around "micro-time"
		// difference that Date.setSeconds() doesn't handle
		assert now.toString().equals(fdate.toString());
		 */
	}

	/**
	 * Test {@link CheckboxField}.
	 * @throws Exception
	 */
	public void testCheckboxField() throws Exception {
		final CheckboxField f = FieldFactory.fcheckbox(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT);
		validateFieldCommon(f);
		f.setValue(Boolean.TRUE);
		assert f.getValue() == Boolean.TRUE;
		assert Boolean.TRUE.toString().equals(f.getText());
		assert f.isChecked() == true;
	}

	public void testSuggestField() throws Exception {
		final Map<String, String> data = new HashMap<String, String>();
		data.put("s1", "S1");
		data.put("s2", "S2");
		data.put("s3", "S3");
		final SuggestField f = FieldFactory.fsuggest(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, data);
		validateFieldCommon(f);
		f.setText(STRING_VALUE);
		assert STRING_VALUE.equals(f.getText());
	}

	public void testRadioGroupField() throws Exception {
		final Map<String, String> data = new HashMap<String, String>();
		data.put("s1", "S1");
		data.put("s2", "S2");
		data.put("s3", "S3");
		final RadioGroupField<String> f =
			FieldFactory.fradiogroup(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, data, new GridRenderer(1, null));
		validateFieldCommon(f);

		// TODO finish
	}

	public void testSelectField() throws Exception {
		final Map<String, String> data = new HashMap<String, String>();
		data.put(null, "");
		data.put("s1", "S1");
		data.put("s2", "S2");
		data.put("s3", "S3");
		final SelectField<String> f = FieldFactory.fselect(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, data);
		validateFieldCommon(f);

		// TODO finish
	}

	public void testMultiSelectField() throws Exception {
		final Map<String, String> data = new HashMap<String, String>();
		data.put("s1", "S1");
		data.put("s2", "S2");
		data.put("s3", "S3");
		final MultiSelectField<String> f = FieldFactory.fmultiselect(PROP_NAME, PROP_NAME, LABEL_TEXT, HELP_TEXT, data);
		validateFieldCommon(f);

		// TODO finish
	}
}
