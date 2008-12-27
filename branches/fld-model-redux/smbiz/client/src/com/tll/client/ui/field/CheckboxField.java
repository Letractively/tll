/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.util.ToBooleanRenderer;

/**
 * CheckboxField
 * @author jpk
 */
public final class CheckboxField extends AbstractField<Boolean> {

	private CheckBox cb;

	/**
	 * This text overrides the base field label text mechanism in order to have
	 * the text appear to the right of the form control.
	 */
	protected String cbLblTxt;

	/**
	 * The change listeners.
	 */
	private ChangeListenerCollection changeListeners;

	public void addChangeListener(ChangeListener listener) {
		if(changeListeners == null) {
			changeListeners = new ChangeListenerCollection();
		}
		changeListeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		if(changeListeners != null) {
			changeListeners.remove(listener);
		}
	}

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 */
	public CheckboxField(String propName, String lblTxt) {
		super(propName, null);
		this.cbLblTxt = lblTxt;
	}

	public CheckBox getCheckBox() {
		if(cb == null) {
			cb = new CheckBox(cbLblTxt);
			cb.setStyleName(FieldLabel.CSS_FIELD_LABEL);
			// cb.addFocusListener(this);
			cb.addClickListener(this);
			addChangeListener(this);
			setRenderer(ToBooleanRenderer.INSTANCE);
		}
		return cb;
	}

	@Override
	protected final HasFocus getEditable(String value) {
		getCheckBox();
		if(value != null) {
			cb.setChecked(Boolean.TRUE.toString().equals(value) ? true : false);
		}
		return cb;
	}

	@Override
	public final String getEditableValue() {
		return cb == null ? null : (cb.isChecked() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
	}

	@Override
	protected String getReadOnlyHtml() {
		return (cbLblTxt == null ? "" : "<label>" + cbLblTxt + "</label><br/> ") + getFieldValue();
	}

	@Override
	public void onClick(Widget sender) {
		assert sender == cb;
		super.onClick(sender);
		Boolean old = isChecked() ? Boolean.FALSE : Boolean.TRUE;
		changeSupport.firePropertyChange("value", old, getValue());
	}

	public boolean isChecked() {
		return cb.isChecked();
	}

	public void setChecked(boolean checked) {
		cb.setChecked(checked);
	}

	@Override
	public String getFieldValue() {
		return getValue().toString();
	}

	public Boolean getValue() {
		return isChecked() ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setValue(Object value) {
		Boolean old = getValue();
		setChecked(value == null ? false : getRenderer().render(value).booleanValue());
		if((old != getValue()) && !old.equals(getValue())) {
			changeSupport.firePropertyChange("value", old, getValue());
		}
	}
}
