/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.convert.ToStringConverter;
import com.tll.client.util.SimpleComparator;
import com.tll.client.util.StringUtil;

/**
 * TextAreaField
 * @author jpk
 */
public class TextAreaField extends AbstractField<String> implements HasMaxLength {

	int maxLen = -1;
	private final TextArea ta;
	private String old;

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param numRows if -1, value won't be set
	 * @param numCols if -1, value won't be set
	 */
	public TextAreaField(String propName, String labelText, String helpText, int numRows, int numCols) {
		super(propName, labelText, helpText);
		setConverter(ToStringConverter.INSTANCE);
		setComparator(SimpleComparator.INSTANCE);
		ta = new TextArea();
		ta.addChangeListener(this);
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
		setValue(text);
	}

	@Override
	protected HasFocus getEditable() {
		return ta;
	}

	public String getValue() {
		String t = ta.getText();
		return StringUtil.isEmpty(t) ? null : t;
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getConverter().convert(value));
		String newval = getValue();
		if(old != newval && (old != null && !old.equals(newval)) || (newval != null && !newval.equals(old))) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
