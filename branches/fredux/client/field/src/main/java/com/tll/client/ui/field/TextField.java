/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.tll.client.convert.ToStringConverter;

/**
 * TextField
 * @author jpk
 */
public final class TextField extends AbstractField<String> implements IHasMaxLength {

	/**
	 * Impl
	 * @author jpk
	 */
	static final class Impl extends TextBox implements IEditable<String> {

	}

	private final Impl tb;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param visibleLength
	 */
	TextField(String name, String propName, String labelText, String helpText, int visibleLength) {
		super(name, propName, labelText, helpText);
		tb = new Impl();
		setVisibleLen(visibleLength);
		tb.addValueChangeHandler(this);
		tb.addBlurHandler(this);
		setConverter(ToStringConverter.INSTANCE);
		addHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER) {
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
	protected IEditable<String> getEditable() {
		return tb;
	}
}
