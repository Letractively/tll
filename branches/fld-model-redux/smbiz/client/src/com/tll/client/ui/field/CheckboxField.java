/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasFocus;

/**
 * CheckboxField
 * @author jpk
 */
public class CheckboxField extends AbstractField {

	protected String checkedValue;
	protected String uncheckedValue;

	private CheckBox cb;

	/**
	 * This text overrides the base field label text mechanism in order to have
	 * the text appear to the right of the form control.
	 */
	protected String cbLblTxt;

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt The checkbox [label] text
	 * @param checkedValue
	 * @param uncheckedValue
	 */
	public CheckboxField(String propName, String lblTxt, String checkedValue, String uncheckedValue) {
		super(propName, null);
		this.cbLblTxt = lblTxt;
		this.checkedValue = checkedValue;
		this.uncheckedValue = uncheckedValue;
	}

	public CheckBox getCheckBox() {
		if(cb == null) {
			cb = new CheckBox(cbLblTxt);
			cb.setStyleName(FieldLabel.CSS_FIELD_LABEL);
			cb.addFocusListener(this);
			cb.addClickListener(this);
		}
		return cb;
	}

	public final void setChecked(boolean checked) {
		this.value = checked ? checkedValue : uncheckedValue;
		getCheckBox().setChecked(checked);
	}

	public final void setCheckedValue(String checkedValue) {
		this.checkedValue = checkedValue;
	}

	public final void setUncheckedValue(String uncheckedValue) {
		this.uncheckedValue = uncheckedValue;
	}

	private boolean isCheckedValue(String value) {
		return value == null ? false : value.equals(checkedValue);
	}

	@Override
	protected final HasFocus getEditable(String value) {
		getCheckBox();
		if(value != null) {
			cb.setChecked(isCheckedValue(value));
		}
		return cb;
	}

	@Override
	public final String getEditableValue() {
		return cb == null ? null : cb.isChecked() ? checkedValue : uncheckedValue;
	}

	@Override
	protected String getReadOnlyHtml() {
		return (cbLblTxt == null ? "" : "<label>" + cbLblTxt + "</label><br/> ")
				+ (isCheckedValue(getValue()) ? checkedValue : uncheckedValue);
	}
}
