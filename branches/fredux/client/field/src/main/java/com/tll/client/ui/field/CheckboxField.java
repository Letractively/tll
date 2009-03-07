/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.CheckBox;
import com.tll.client.convert.ToBooleanConverter;
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
		public Impl(String label) {
			super(label);
		}

		@Override
		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}

		@Override
		public void setValue(Boolean value, boolean fireEvents) {
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
	CheckboxField(String name, String propName, String labelText, String helpText) {
		super(name, propName, null, helpText);
		setConverter(ToBooleanConverter.DEFAULT);
		this.cblabelText = labelText;
		cb = new Impl(cblabelText);
		cb.setStyleName(Styles.LABEL);
		cb.addClickHandler(this);
		cb.addBlurHandler(this);
		cb.addValueChangeHandler(this);
	}

	@Override
	protected IEditable<Boolean> getEditable() {
		return cb;
	}

	public boolean isChecked() {
		return cb.getValue() == Boolean.TRUE; 
	}

	public void setChecked(boolean checked) {
		cb.setValue(checked ? Boolean.TRUE : Boolean.FALSE);
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
	
	@Override
	public String getLabelText() {
		return cblabelText;
	}
}
