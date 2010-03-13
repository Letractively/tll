/**
 * The Logic Lab
 * @author jpk
 * @since Feb 28, 2010
 */
package com.tll.common.model;

import com.tll.schema.PropertyMetadata;
import com.tll.schema.PropertyType;


/**
 * ObjectPropertyValue
 * @author jpk
 */
public class ObjectPropertyValue extends AbstractPropertyValue {
	
	private Object object;

	/**
	 * Constructor
	 */
	public ObjectPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 */
	public ObjectPropertyValue(String propertyName, PropertyMetadata metadata) {
		super(propertyName, metadata);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public ObjectPropertyValue(String propertyName, PropertyMetadata metadata, Object value) {
		super(propertyName, metadata);
		this.object = value;
	}

	@Override
	protected void doSetValue(Object value) throws IllegalArgumentException {
		this.object = value;
	}

	/**
	 * TODO: we don't copy the object reather maintain the ref.
	 */
	@Override
	public IPropertyValue copy() {
		return new ObjectPropertyValue(propertyName, metadata, object);
	}

	@Override
	public PropertyType getType() {
		return PropertyType.OBJECT;
	}

	@Override
	public Object getValue() {
		return object;
	}
}
