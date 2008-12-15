/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * PasswordField
 * @author jpk
 */
public class PasswordField extends AbstractField {

	protected PasswordTextBox tb;

	public void addChangeListener(ChangeListener listener) {
		getPasswordTextBox().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getPasswordTextBox().removeChangeListener(listener);
	}

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 */
	public PasswordField(String propName, String lblText) {
		super(propName, lblText);
	}

	public PasswordTextBox getPasswordTextBox() {
		if(tb == null) {
			tb = new PasswordTextBox();
			// tb.addFocusListener(this);
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
}
