/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.tll.client.field.IField;

/**
 * PasswordField
 * @author jpk
 */
public class PasswordField extends AbstractField {

	protected PasswordTextBox tb;

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 * @param lblMode
	 */
	public PasswordField(String propName, String lblText, LabelMode lblMode) {
		super(propName, lblText, lblMode);
	}

	public PasswordTextBox getPasswordTextBox() {
		if(tb == null) {
			tb = new PasswordTextBox();
			tb.addFocusListener(this);
			tb.addChangeListener(this);
		}
		return tb;
	}

	@Override
	protected HasFocus getEditable(String value) {
		getPasswordTextBox();
		if(value != null) {
			tb.setText(value);
		}
		return tb;
	}

	@Override
	public String getEditableValue() {
		return tb == null ? null : tb.getText();
	}

	public IField copy() {
		return new PasswordField(propName, lblTxt, lblMode);
	}
}
