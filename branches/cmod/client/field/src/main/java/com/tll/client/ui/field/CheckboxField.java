/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.tll.client.convert.IConverter;
import com.tll.client.convert.ToStringConverter;
import com.tll.common.util.ObjectUtil;

/**
 * CheckboxField
 * @param <B> the bound type
 * @author jpk
 */
public final class CheckboxField<B> extends AbstractField<B, Boolean> {

	private final CheckBox cb;

	/**
	 * This text overrides the base field label text mechanism in order to have
	 * the text appear to the right of the form control.
	 */
	protected String cblabelText;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param converter
	 */
	CheckboxField(String name, String propName, String labelText, String helpText, IConverter<Boolean, B> converter) {
		super(name, propName, null, helpText);
		this.cblabelText = labelText;
		setConverter(converter);
		cb = new CheckBox(cblabelText);
		cb.setStyleName(Styles.LABEL);
		// cb.addFocusListener(this);
		cb.addClickHandler(this);
		addChangeHandler(this);
	}

	@Override
	protected FocusWidget getEditable() {
		return cb;
	}

	public boolean isChecked() {
		return cb.getValue() == Boolean.TRUE; 
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
		cb.setValue(checked ? Boolean.TRUE : Boolean.FALSE);
	}

	public Boolean getValue() {
		return cb.getValue();
	}

	@Override
	protected void setNativeValue(Boolean nativeValue) {
		final Boolean old = getValue();
		setChecked(nativeValue == null ? false : nativeValue);
		final Boolean newval = getValue();
		if(!ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	@Override
	public void onClick(ClickEvent event) {
		assert event.getSource() == cb;
		super.onClick(event);
		// i.e. not is checked since this event fires before the click value sticks!
		final Boolean old = !(isChecked() ? Boolean.FALSE : Boolean.TRUE);
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		fireChangeListeners();
	}
}
