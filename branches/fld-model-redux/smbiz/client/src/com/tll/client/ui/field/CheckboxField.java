/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Widget;

/**
 * CheckboxField
 * @author jpk
 */
public final class CheckboxField extends AbstractField {

	private static final String DEFAULT_CHECKED_VALUE = Boolean.toString(true);
	private static final String DEFAULT_UNCHECKED_VALUE = Boolean.toString(false);

	private final String checkedValue;
	private final String uncheckedValue;

	private CheckBox cb;

	/**
	 * This text overrides the base field label text mechanism in order to have
	 * the text appear to the right of the form control.
	 */
	protected String cbLblTxt;

	/**
	 * The change listeners.
	 */
	private ChangeListenerCollection changeListeners;

	public void addChangeListener(ChangeListener listener) {
		if(changeListeners == null) {
			changeListeners = new ChangeListenerCollection();
		}
		changeListeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		if(changeListeners != null) {
			changeListeners.remove(listener);
		}
	}

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 */
	public CheckboxField(String propName, String lblTxt) {
		this(propName, lblTxt, DEFAULT_CHECKED_VALUE, DEFAULT_UNCHECKED_VALUE);
	}

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt The checkbox [label] text
	 * @param checkedValue The value to provide when this checkbox is in the
	 *        checked state
	 * @param uncheckedValue The value to provide when this checkbox is in the
	 *        un-checked state
	 */
	public CheckboxField(String propName, String lblTxt, String checkedValue, String uncheckedValue) {
		super(propName, null);
		this.cbLblTxt = lblTxt;
		if(checkedValue == null || uncheckedValue == null) {
			throw new IllegalArgumentException("Both the checked value and unchecked values must be specified.");
		}
		this.checkedValue = checkedValue;
		this.uncheckedValue = uncheckedValue;
	}

	public CheckBox getCheckBox() {
		if(cb == null) {
			cb = new CheckBox(cbLblTxt);
			cb.setStyleName(FieldLabel.CSS_FIELD_LABEL);
			// cb.addFocusListener(this);
			cb.addClickListener(this);
			addChangeListener(this);
		}
		return cb;
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
		return (cbLblTxt == null ? "" : "<label>" + cbLblTxt + "</label><br/> ") + getFieldValue();
	}

	@Override
	public void onClick(Widget sender) {
		super.onClick(sender);
		assert sender == cb;
		if(changeListeners != null) changeListeners.fireChange(this);
	}

	public boolean isChecked() {
		return checkedValue.equals(getFieldValue());
	}

	public void setChecked(boolean checked) {
		setFieldValue(checked ? checkedValue : uncheckedValue);
		getCheckBox().setChecked(checked);
	}

	@Override
	public String getFieldValue() {
		return isCheckedValue(getFieldValue()) ? checkedValue : uncheckedValue;
	}

	public void setValue(Object value) {
		String old = getValue();
		setChecked(value == null ? false : value.equals(checkedValue));

		if((old != getValue()) && !old.equals(getValue())) {
			changeSupport.firePropertyChange("value", old, this.getValue());
		}
	}
}
