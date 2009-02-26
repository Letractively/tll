/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.TextArea;

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
	TextAreaField(String name, String propName, String labelText, String helpText, int numRows, int numCols) {
		super(name, propName, labelText, helpText);
		ta = new Impl();
		ta.addValueChangeHandler(this);
		setNumRows(numRows);
		setNumCols(numCols);
	}

	public int getNumRows() {
		return ta.getVisibleLines();
	}

	public void setNumRows(int numRows) {
		ta.setVisibleLines(numRows);
	}

	public int getNumCols() {
		return ta.getCharacterWidth();
	}

	public void setNumCols(int numCols) {
		ta.setCharacterWidth(numCols);
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public String getText() {
		return ta.getText();
	}

	public void setText(String text) {
		ta.setText(text);
	}

	@Override
	protected IEditable<String> getEditable() {
		return ta;
	}
}
