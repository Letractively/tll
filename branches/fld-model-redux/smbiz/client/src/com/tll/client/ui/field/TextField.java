/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.beans.PropertyChangeSupport;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.HasFormat;
import com.tll.client.field.HasMaxLength;
import com.tll.client.util.GlobalFormat;
import com.tll.client.util.StringUtil;

/**
 * TextField
 * @author jpk
 */
public class TextField extends AbstractField implements HasMaxLength, HasFormat, HasText {

	private int visibleLen = -1, maxLen = -1;
	private TextBox tb;
	private String old;

	/**
	 * The display format directive. Its meaning is dependent on the
	 * implementation.
	 */
	protected GlobalFormat format;

	private final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

	protected final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param visibleLen
	 */
	public TextField(String propName, String lblTxt, int visibleLen) {
		super(propName, lblTxt);
		this.visibleLen = visibleLen;
	}

	public int getVisibleLen() {
		return visibleLen;
	}

	public void setVisibleLen(int visibleLength) {
		this.visibleLen = visibleLength;
	}

	public String getText() {
		return getFieldValue();
	}

	public void setText(String text) {
		setFieldValue(text);
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public final GlobalFormat getFormat() {
		return format;
	}

	public final void setFormat(GlobalFormat format) {
		this.format = format;
	}

	public TextBox getTextBox() {
		if(tb == null) {
			tb = new TextBox();
			// tb.addFocusListener(this);
			tb.addChangeListener(this);
			tb.addChangeListener(new ChangeListener() {

				public void onChange(Widget sender) {
					changes.firePropertyChange("value", old, getValue());
					old = getValue();
					changeListeners.fireChange(tb);
				}
			});
		}
		return tb;
	}

	public void addChangeListener(ChangeListener listener) {
		getTextBox().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getTextBox().removeChangeListener(listener);
	}

	@Override
	protected HasFocus getEditable(String value) {
		getTextBox();
		if(visibleLen > -1) {
			tb.setVisibleLength(visibleLen);
		}
		if(maxLen > -1) {
			tb.setMaxLength(maxLen);
		}
		if(value != null) {
			tb.setText(value);
		}
		return tb;
	}

	@Override
	protected String getEditableValue() {
		return tb == null ? null : tb.getText();
	}

	@Override
	protected String getReadOnlyHtml() {
		return StringUtil.abbr(super.getReadOnlyHtml(), visibleLen);
	}

	public void setValue(Object value) {
		String old = this.getValue();
		setText(this.getRenderer() != null ? getRenderer().render(value) : "" + value);
		if(this.getValue() != old && this.getValue() != null && this.getValue().equals(old)) {
			changeSupport.firePropertyChange("value", old, this.getValue());
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
