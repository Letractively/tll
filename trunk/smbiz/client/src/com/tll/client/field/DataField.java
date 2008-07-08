/**
 * The Logic Lab
 * @author jkirton
 * Jun 10, 2008
 */
package com.tll.client.field;

import com.tll.client.model.IPropertyBinding;
import com.tll.client.model.IPropertyValue;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;

/**
 * DataField - An IField serving only to transfer data to the model. DataFields
 * are <em>not</em> rendered in the UI.
 * @author jpk
 */
public class DataField implements IField {

	private String propName;
	private Object value;

	/**
	 * Constructor
	 * @param propName
	 */
	public DataField(String propName) {
		super();
		setPropertyName(propName);
	}

	/**
	 * Constructor
	 * @param propName
	 * @param value
	 */
	public DataField(String propName, Object value) {
		this(propName);
		this.value = value;
	}

	public void addValidator(IValidator validator) {
		throw new UnsupportedOperationException();
	}

	public void bindModel(IPropertyBinding binding) {
		// value = binding.getValue();
		// no-op
	}

	public void clear() {
		// no-op
	}

	public String getValue() {
		return null;
	}

	public void handleValidationFeedback(IValidationFeedback feedback) {
		// no-op
	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isReadOnly() {
		return false;
	}

	public boolean isRequired() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}

	public void reset() {
		// no-op
	}

	public void setEnabled(boolean enabled) {
		// no-op
	}

	public void setPropertyName(String propName) {
		this.propName = propName;
	}

	public void setReadOnly(boolean readOnly) {
		// no-op
	}

	public void setRequired(boolean required) {
		// no-op
	}

	public void setValue(String value) {
		// no-op
	}

	public void setVisible(boolean visible) {
		// no-op
	}

	public boolean updateModel(IPropertyBinding binding) {
		IPropertyValue pv = (IPropertyValue) binding;
		pv.setValue(value);
		return true;
	}

	public String getPropertyName() {
		return propName;
	}

	public void markReset() {
		// no-op
	}

	public void validate() {
		// no-op
	}
}
