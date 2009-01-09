/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.BooleanConverter;
import com.tll.client.convert.ToStringConverter;

/**
 * CheckboxField
 * @author jpk
 */
public final class CheckboxField extends AbstractField<Boolean> {

	private final CheckBox cb;

	/**
	 * This text overrides the base field label text mechanism in order to have
	 * the text appear to the right of the form control.
	 */
	protected String cblabelText;

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 */
	public CheckboxField(String propName, String labelText, String helpText) {
		super(propName, null, helpText);
		this.cblabelText = labelText;
		setConverter(BooleanConverter.INSTANCE);
		cb = new CheckBox(cblabelText);
		cb.setStyleName(STYLE_FIELD_LABEL);
		// cb.addFocusListener(this);
		cb.addClickListener(this);
		addChangeListener(this);
	}

	@Override
	protected HasFocus getEditable() {
		return cb;
	}

	public boolean isChecked() {
		return cb.isChecked();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setLabelText(readOnly ? cblabelText : null);
	}

	public String getText() {
		return ToStringConverter.INSTANCE.convert(getValue());
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	public void setChecked(boolean checked) {
		cb.setChecked(checked);
	}

	public Boolean getValue() {
		return cb.isChecked() ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setValue(Object value) {
		Boolean old = getValue();
		setChecked(value == null ? false : getConverter().convert(value).booleanValue());
		Boolean newval = getValue();
		if((old != newval) && !old.equals(newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	public void onClick(Widget sender) {
		assert sender == cb;
		super.onClick(sender);
		Boolean old = isChecked() ? Boolean.FALSE : Boolean.TRUE;
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
	}
}