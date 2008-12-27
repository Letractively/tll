/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * PasswordField
 * @author jpk
 */
public final class PasswordField extends AbstractField implements HasText, HasMaxLength {

	private int visibleLen = -1, maxLen = -1;
	private PasswordTextBox tb;
	private String old;

	private final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 */
	public PasswordField(String propName, String lblText) {
		super(propName, lblText);
	}

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public int getVisibleLen() {
		return visibleLen;
	}

	public void setVisibleLen(int visibleLength) {
		this.visibleLen = visibleLength;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public String getText() {
		return getFieldValue();
	}

	public void setText(String text) {
		setFieldValue(text);
	}

	public PasswordTextBox getPasswordTextBox() {
		if(tb == null) {
			tb = new PasswordTextBox();
			// tb.addFocusListener(this);
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

	public void setValue(Object value) {
		String old = this.getValue();
		setText(this.getRenderer() != null ? getRenderer().render(value) : "" + value);
		if(this.getValue() != old && this.getValue() != null && this.getValue().equals(old)) {
			changeSupport.firePropertyChange("value", old, this.getValue());
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		changeSupport.firePropertyChange("value", old, getValue());
		old = getValue();
		changeListeners.fireChange(this);
	}
}
