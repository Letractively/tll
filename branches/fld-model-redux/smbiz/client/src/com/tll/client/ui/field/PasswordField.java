/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.util.StringUtil;

/**
 * PasswordField
 * @author jpk
 */
public final class PasswordField extends AbstractField<String> implements HasText, HasMaxLength {

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
		getPasswordTextBox().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getPasswordTextBox().removeChangeListener(listener);
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
		if(!isReadOnly()) {
			getPasswordTextBox().setText(text);
		}
		else {
			setFieldValue(text);
		}
	}

	public PasswordTextBox getPasswordTextBox() {
		if(tb == null) {
			tb = new PasswordTextBox();
			// tb.addFocusListener(this);
			tb.addChangeListener(this);
			addKeyboardListener(new KeyboardListener() {

				public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				}

				public void onKeyPress(Widget sender, char keyCode, int modifiers) {
					if(keyCode == KeyboardListener.KEY_ENTER) {
						setFocus(false);
						setFocus(true);
					}
				}

				public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				}

			});
		}
		if(visibleLen > -1) {
			tb.setVisibleLength(visibleLen);
		}
		if(maxLen > -1) {
			tb.setMaxLength(maxLen);
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
	protected String getEditableValue() {
		return tb == null ? null : tb.getText();
	}

	@Override
	protected String getReadOnlyHtml() {
		return StringUtil.abbr(super.getReadOnlyHtml(), visibleLen);
	}

	public String getValue() {
		final TextBox tb = getPasswordTextBox();
		try {
			return tb.getText().length() == 0 ? null : tb.getText();
		}
		catch(RuntimeException re) {
			GWT.log("" + tb, re);
			return null;
		}
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getRenderer() != null ? getRenderer().render(value) : "" + value);
		if(getValue() != old && getValue() != null && getValue().equals(old)) {
			changeSupport.firePropertyChange("value", old, getValue());
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
