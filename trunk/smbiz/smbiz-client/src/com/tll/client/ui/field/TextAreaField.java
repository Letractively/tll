/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.TextArea;
import com.tll.client.field.HasMaxLength;
import com.tll.client.field.IField;
import com.tll.client.util.StringUtil;

/**
 * TextAreaField
 * @author jpk
 */
public class TextAreaField extends AbstractField implements HasMaxLength {

	private int maxlen = -1;
	private int numRows, numCols;
	private TextArea ta;

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param numRows if -1, value won't be set
	 * @param numCols if -1, value won't be set
	 */
	public TextAreaField(String propName, String lblTxt, LabelMode lblMode, int numRows, int numCols) {
		super(propName, lblTxt, lblMode);
		this.numRows = numRows;
		this.numCols = numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public TextArea getTextArea() {
		if(ta == null) {
			ta = new TextArea();
			if(numCols > -1) {
				ta.setCharacterWidth(numCols);
			}
			if(numRows > -1) {
				ta.setVisibleLines(numRows);
			}
			ta.addFocusListener(this);
		}
		return ta;
	}

	public int getMaxLen() {
		return maxlen;
	}

	public void setMaxLen(int maxLen) {
		this.maxlen = maxLen;
	}

	@Override
	protected HasFocus getEditable(String value) {
		getTextArea();
		if(value != null) {
			ta.setText(value);
		}
		return ta;
	}

	@Override
	protected String getEditableValue() {
		return ta == null ? null : ta.getText();
	}

	@Override
	protected String getReadOnlyRenderValue() {
		return StringUtil.abbr(super.getReadOnlyRenderValue(), getMaxLen());
	}

	public IField copy() {
		return new TextAreaField(propName, lblTxt, lblMode, numRows, numCols);
	}
}
