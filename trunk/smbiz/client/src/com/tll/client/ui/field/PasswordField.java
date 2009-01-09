/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.ToStringConverter;
import com.tll.client.util.ObjectUtil;
import com.tll.client.util.StringUtil;

/**
 * PasswordField
 * @author jpk
 */
public final class PasswordField extends AbstractField<String> implements HasMaxLength {

	private final PasswordTextBox tb;
	private String old;

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 * @param helpText
	 * @param visibleLength
	 */
	public PasswordField(String propName, String lblText, String helpText, int visibleLength) {
		super(propName, lblText, helpText);
		tb = new PasswordTextBox();
		setVisibleLen(visibleLength);
		setConverter(ToStringConverter.INSTANCE);
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

	public int getVisibleLen() {
		return tb.getVisibleLength();
	}

	public void setVisibleLen(int visibleLength) {
		tb.setVisibleLength(visibleLength < 0 ? 256 : visibleLength);
	}

	public int getMaxLen() {
		return tb.getMaxLength();
	}

	public void setMaxLen(int maxLen) {
		tb.setMaxLength(maxLen < 0 ? 256 : maxLen);
	}

	public String getText() {
		return tb.getText();
	}

	public void setText(String text) {
		tb.setText(text);
	}

	@Override
	protected HasFocus getEditable() {
		return tb;
	}

	public String getValue() {
		String t = tb.getText();
		return StringUtil.isEmpty(t) ? null : t;
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getConverter().convert(value));
		String newval = getValue();
		if(changeSupport != null && !ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
