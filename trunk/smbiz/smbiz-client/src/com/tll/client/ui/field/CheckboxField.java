/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.IField;
import com.tll.client.ui.Br;

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

	// TODO figure out how to eliminate this property
	private boolean alignBottom;

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt The checkbox [label] text
	 * @param checkedValue
	 * @param uncheckedValue
	 */
	public CheckboxField(String propName, String lblTxt, String checkedValue, String uncheckedValue) {
		super(propName, null, LabelMode.NONE);
		this.cbLblTxt = lblTxt;
		this.checkedValue = checkedValue;
		this.uncheckedValue = uncheckedValue;
	}

	/**
	 * @return the alignBottom
	 */
	public boolean isAlignBottom() {
		return alignBottom;
	}

	/**
	 * @param alignBottom the alignBottom to set
	 */
	public void setAlignBottom(boolean alignBottom) {
		this.alignBottom = alignBottom;
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

	@Override
	public String getLabelText() {
		return cbLblTxt;
	}

	@Override
	public void setLabelText(String lblTxt) {
		if(lblTxt != null && lblTxt.equals(cbLblTxt)) return;
		this.cbLblTxt = lblTxt == null ? "" : lblTxt;
		if(cb != null) {
			cb.setText(cbLblTxt);
		}
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
	protected String getReadOnlyRenderValue() {
		return isCheckedValue(getValue()) ? checkedValue : uncheckedValue;
	}

	@Override
	public void render() {
		fp.clear();
		if(isReadOnly()) {
			fp.add(new FieldLabel(cbLblTxt));
			fp.add(new Label(getReadOnlyRenderValue()));
		}
		else {
			if(alignBottom) {
				fp.add(new Br());
			}
			final Widget ef = (Widget) getEditable(value);
			assert ef != null;
			ef.setVisible(true);
			ef.getElement().setPropertyString("id", domId);
			ef.getElement().setPropertyBoolean("disabled", !isEnabled());
			fp.add(ef);
		}
	}

	public IField copy() {
		return new CheckboxField(propName, /*propType, */lblTxt, checkedValue, uncheckedValue);
	}

	@Override
	public void onClick(Widget sender) {
		if(sender == cb) {
			changed = true;
		}
		else {
			super.onClick(sender);
		}
	}
}
