/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.StringLengthValidator;

/**
 * TextField
 * @author jpk
 */
public final class TextField extends AbstractField<String> implements IHasMaxLength, IHasFormat {

	/**
	 * Impl
	 * @author jpk
	 */
	static final class Impl extends TextBox implements IEditable<String> {

		/**
		 * Constructor
		 */
		public Impl() {
			super();
			addStyleName(Styles.TBOX);
		}

	}

	private final Impl tb;

	/**
	 * Optional format direcive.
	 */
	private GlobalFormat format;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param visibleLength
	 */
	TextField(final String name, final String propName, final String labelText, final String helpText,
			final int visibleLength) {
		super(name, propName, labelText, helpText);
		tb = new Impl();
		setVisibleLen(visibleLength);
		tb.addValueChangeHandler(this);
		tb.addFocusHandler(this);
		tb.addBlurHandler(this);
		// setConverter(ToStringConverter.INSTANCE);
		addHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(final KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER) {
					setFocus(false);
					setFocus(true);
				}
			}
		}, KeyPressEvent.getType());

	}

	@Override
	public GlobalFormat getFormat() {
		return format;
	}

	@Override
	public void setFormat(final GlobalFormat format) {
		this.format = format;
	}

	public int getVisibleLen() {
		return tb.getVisibleLength();
	}

	public void setVisibleLen(final int visibleLength) {
		tb.setVisibleLength(visibleLength < 0 ? 256 : visibleLength);
	}

	@Override
	public int getMaxLen() {
		return tb.getMaxLength();
	}

	@Override
	public void setMaxLen(final int maxLen) {
		tb.setMaxLength(maxLen < 0 ? 256 : maxLen);
		if(maxLen == -1) {
			removeValidator(StringLengthValidator.class);
		}
		else {
			addValidator(new StringLengthValidator(-1, maxLen));
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		tb.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public String doGetText() {
		return tb.getText();
	}

	@Override
	public void setText(final String text) {
		tb.setText(text);
	}

	@Override
	public IEditable<String> getEditable() {
		return tb;
	}
}
