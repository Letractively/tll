/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.tll.client.convert.IConverter;
import com.tll.common.util.ObjectUtil;
import com.tll.common.util.StringUtil;

/**
 * PasswordField
 * @param <B> The bound type
 * @author jpk
 */
public final class PasswordField<B> extends AbstractField<B, String> implements IHasMaxLength {

	private final PasswordTextBox tb;
	private String old;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param lblText
	 * @param helpText
	 * @param visibleLength
	 * @param converter
	 */
	PasswordField(String name, String propName, String lblText, String helpText, int visibleLength,
			IConverter<String, B> converter) {
		super(name, propName, lblText, helpText);
		tb = new PasswordTextBox();
		setVisibleLen(visibleLength);
		setConverter(converter);
		// tb.addFocusListener(this);
		tb.addChangeHandler(this);
		addHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == 'e') { // TODO fix for enter key!
					setFocus(false);
					setFocus(true);
				}
			}
		}, KeyPressEvent.getType());
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
	protected FocusWidget getEditable() {
		return tb;
	}

	public String getValue() {
		final String t = tb.getText();
		return StringUtil.isEmpty(t) ? null : t;
	}

	@Override
	protected void setNativeValue(String nativeValue) {
		final String old = getValue();
		setText(nativeValue);
		final String newval = getValue();
		if(!ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	@Override
	public void onChange(ChangeEvent event) {
		super.onChange(event);
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
