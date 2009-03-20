/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * BooleanPropertyValue
 * @author jpk
 */
public class BooleanPropertyValue extends AbstractPropertyValue {

	private Boolean value;

	/**
	 * Constructor
	 */
	public BooleanPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public BooleanPropertyValue(String propertyName, Boolean value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public BooleanPropertyValue(String propertyName, PropertyMetadata metadata, Boolean value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.BOOL;
	}

	public IPropertyValue copy() {
		return new BooleanPropertyValue(propertyName, metadata, value == null ? null : new Boolean(value.booleanValue()));
	}

	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj != null && obj instanceof Boolean == false) {
			throw new IllegalArgumentException("The value must be a Boolean");
		}
		this.value = (Boolean) obj;
	}

	public Boolean getBoolean() {
		return value;
	}
}
