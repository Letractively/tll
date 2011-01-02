/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.TextArea;
import com.tll.client.validate.StringLengthValidator;

/**
 * TextAreaField
 * @author jpk
 */
public class TextAreaField extends AbstractField<String> implements IHasMaxLength {

	/**
	 * Impl
	 * @author jpk
	 */
	static final class Impl extends TextArea implements IEditable<String> {

		/**
		 * Constructor
		 */
		public Impl() {
			super();
			addStyleName(Styles.TBOX);
		}

	}

	int maxLen = -1;
	private final Impl ta;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param numRows if -1, value won't be set
	 * @param numCols if -1, value won't be set
	 */
	TextAreaField(final String name, final String propName, final String labelText, final String helpText,
			final int numRows, final int numCols) {
		super(name, propName, labelText, helpText);
		ta = new Impl();
		ta.addValueChangeHandler(this);
		ta.addFocusHandler(this);
		ta.addBlurHandler(this);
		// setConverter(ToStringConverter.INSTANCE);
		setNumRows(numRows);
		setNumCols(numCols);
	}

	public int getNumRows() {
		return ta.getVisibleLines();
	}

	public void setNumRows(final int numRows) {
		ta.setVisibleLines(numRows);
	}

	public int getNumCols() {
		return ta.getCharacterWidth();
	}

	public void setNumCols(final int numCols) {
		ta.setCharacterWidth(numCols);
	}

	@Override
	public int getMaxLen() {
		return maxLen;
	}

	@Override
	public void setMaxLen(final int maxLen) {
		this.maxLen = maxLen;
		if(maxLen == -1) {
			removeValidator(StringLengthValidator.class);
		}
		else {
			addValidator(new StringLengthValidator(-1, maxLen));
		}
	}

	@Override
	public String doGetText() {
		return ta.getText();
	}

	@Override
	public void setText(final String text) {
		ta.setText(text);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		ta.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public IEditable<String> getEditable() {
		return ta;
	}
}
