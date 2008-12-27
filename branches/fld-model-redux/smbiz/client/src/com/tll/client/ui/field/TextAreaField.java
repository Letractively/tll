/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.renderer.ToStringRenderer;
import com.tll.client.util.SimpleComparator;
import com.tll.client.util.StringUtil;

/**
 * TextAreaField
 * @author jpk
 */
public class TextAreaField extends AbstractField<String> implements HasMaxLength, HasText {

	private int maxlen = -1;
	private int numRows, numCols;
	private TextArea ta;
	private String old;

	private final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param numRows if -1, value won't be set
	 * @param numCols if -1, value won't be set
	 */
	public TextAreaField(String propName, String lblTxt, int numRows, int numCols) {
		super(propName, lblTxt);
		this.numRows = numRows;
		this.numCols = numCols;
		setRenderer(ToStringRenderer.INSTANCE);
		setComparator(SimpleComparator.INSTANCE);
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

	public String getText() {
		return getFieldValue();
	}

	public void setText(String text) {
		if(!isReadOnly()) {
			getTextArea().setText(text);
		}
		else {
			setFieldValue(text);
		}
	}

	public TextArea getTextArea() {
		if(ta == null) {
			ta = new TextArea();
			// ta.addFocusListener(this);
			ta.addChangeListener(this);
		}
		if(numCols > -1) {
			ta.setCharacterWidth(numCols);
		}
		if(numRows > -1) {
			ta.setVisibleLines(numRows);
		}
		return ta;
	}

	public void addChangeListener(ChangeListener listener) {
		getTextArea().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getTextArea().removeChangeListener(listener);
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
	protected String getReadOnlyHtml() {
		return StringUtil.abbr(super.getReadOnlyHtml(), getMaxLen());
	}

	public String getValue() {
		final TextArea ta = getTextArea();
		try {
			return ta.getText().length() == 0 ? null : ta.getText();
		}
		catch(RuntimeException re) {
			GWT.log("" + ta, re);
			return null;
		}
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getRenderer() != null ? getRenderer().render(value) : "" + value);
		if(getValue() != old && getValue() != null && getValue().equals(old)) {
			changeSupport.firePropertyChange("value", old, getValue());
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		changeSupport.firePropertyChange("value", old, getValue());
		old = getValue();
		changeListeners.fireChange(this);
	}
}
