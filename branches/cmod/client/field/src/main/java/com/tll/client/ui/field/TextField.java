/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;
import com.tll.client.convert.IConverter;
import com.tll.client.convert.IFormattedConverter;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.GlobalFormat;
import com.tll.common.util.ObjectUtil;
import com.tll.common.util.StringUtil;

/**
 * TextField
 * @param <B> The bound type
 * @author jpk
 */
public final class TextField<B> extends AbstractField<B, String> implements IHasMaxLength, IHasFormat {

	private final TextBox tb;
	private String old;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param visibleLength
	 * @param converter
	 */
	TextField(String name, String propName, String labelText, String helpText, int visibleLength,
			IConverter<String, B> converter) {
		super(name, propName, labelText, helpText);
		setConverter(converter);
		tb = new TextBox();
		setVisibleLen(visibleLength);
		// tb.addFocusListener(this);
		tb.addChangeHandler(this);
		addHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == 'e') { // TODO change to enter key!
					setFocus(false);
					setFocus(true);
				}
			}
		}, KeyPressEvent.getType());
		
	}

	public GlobalFormat getFormat() {
		final IConverter<String, B> c = getConverter();
		if(c instanceof IFormattedConverter) {
			return ((IFormattedConverter<String, B>) c).getFormat();
		}
		return null;
	}

	public void setFormat(GlobalFormat format) {
		throw new UnsupportedOperationException();
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
	protected Focusable getEditable() {
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
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	@Override
	public void onChange(ChangeEvent event) {
		super.onChange(event);
		if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
