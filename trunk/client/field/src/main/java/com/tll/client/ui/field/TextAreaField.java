/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextArea;
import com.tll.client.convert.IConverter;
import com.tll.common.util.ObjectUtil;
import com.tll.common.util.StringUtil;

/**
 * TextAreaField
 * @param <B> The bound type
 * @author jpk
 */
public class TextAreaField<B> extends AbstractField<B, String> implements IHasMaxLength {

	int maxLen = -1;
	private final TextArea ta;
	private String old;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param numRows if -1, value won't be set
	 * @param numCols if -1, value won't be set
	 * @param converter
	 */
	TextAreaField(String name, String propName, String labelText, String helpText, int numRows, int numCols,
			IConverter<String, B> converter) {
		super(name, propName, labelText, helpText);
		setConverter(converter);
		// setComparator(SimpleComparator.INSTANCE);
		ta = new TextArea();
		ta.addChangeHandler(this);
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
	protected Focusable getEditable() {
		return ta;
	}

	public String getValue() {
		final String t = ta.getText();
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
		if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
