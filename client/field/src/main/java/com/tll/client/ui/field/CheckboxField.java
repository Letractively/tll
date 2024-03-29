/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.CheckBox;
import com.tll.client.convert.ToStringConverter;

/**
 * CheckboxField
 * @author jpk
 */
public final class CheckboxField extends AbstractField<Boolean> {

	/**
	 * Impl
	 * @author jpk
	 */
	private static final class Impl extends CheckBox implements IEditable<Boolean> {

		/**
		 * Constructor
		 * @param label
		 */
		public Impl(final String label) {
			super(label);
			setStyleName(Styles.CBRB);
		}

		@Override
		public void setValue(final Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}

		@Override
		public void setValue(final Boolean value, final boolean fireEvents) {
			super.setValue(value == null ? Boolean.FALSE : value, fireEvents);
		}
	}

	private final Impl cb;

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
	 */
	CheckboxField(final String name, final String propName, final String labelText, final String helpText) {
		super(name, propName, null, helpText);
		// setConverter(ToBooleanConverter.DEFAULT);
		this.cblabelText = labelText;
		cb = new Impl(cblabelText);
		cb.addFocusHandler(this);
		cb.addBlurHandler(this);
		cb.addValueChangeHandler(this);
	}

	@Override
	public IEditable<Boolean> getEditable() {
		return cb;
	}

	public boolean isChecked() {
		return cb.getValue() == Boolean.TRUE;
	}

	public void setChecked(final boolean checked) {
		cb.setValue(checked ? Boolean.TRUE : Boolean.FALSE);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		cb.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		if(cb != null) cb.setText(readOnly ? "" : cblabelText);
		super.setReadOnly(readOnly);
	}

	@Override
	public String doGetText() {
		return ToStringConverter.INSTANCE.convert(getValue());
	}

	@Override
	public void setText(final String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLabelText() {
		return cblabelText;
	}

	@Override
	public void setLabelText(final String labelText) {
		this.cblabelText = labelText == null ? "" : labelText;
		if(cb != null) cb.setText(cblabelText);
	}
}
