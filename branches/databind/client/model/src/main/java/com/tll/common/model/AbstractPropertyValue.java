/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2008
 */
package com.tll.common.model;

import com.tll.schema.IQueryParam;
import com.tll.schema.PropertyMetadata;
import com.tll.schema.PropertyType;
import com.tll.util.ObjectUtil;

/**
 * AbstractPropertyValue
 * @author jpk
 */
public abstract class AbstractPropertyValue extends AbstractModelProperty implements IPropertyValue, IQueryParam {

	/**
	 * Ad-hoc factory method for stubbing {@link IPropertyValue}s.
	 * @param ptype
	 * @param name
	 * @param metadata
	 * @return the created {@link IPropertyValue} impl
	 */
	static IPropertyValue create(PropertyType ptype, String name, PropertyMetadata metadata) {
		if(name != null && ptype != null || ptype.isValue()) {
			switch(ptype) {
			case STRING:
				return new StringPropertyValue(name, metadata, null);
			case CHAR:
				return new CharacterPropertyValue(name, metadata, null);
			case ENUM:
				return new EnumPropertyValue(name, metadata, null);
			case BOOL:
				return new BooleanPropertyValue(name, metadata, null);
			case INT:
				return new IntPropertyValue(name, metadata, null);
			case LONG:
				return new LongPropertyValue(name, metadata, null);
			case FLOAT:
			case DOUBLE:
				return new DoublePropertyValue(name, metadata, null);
			case DATE:
				return new DatePropertyValue(name, metadata, null);
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * The optional property meta data.
	 */
	protected PropertyMetadata metadata;

	/**
	 * Constructor
	 */
	public AbstractPropertyValue() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 */
	public AbstractPropertyValue(String propertyName, PropertyMetadata metadata) {
		super(propertyName);
		this.metadata = metadata;
	}

	public final PropertyMetadata getMetadata() {
		return metadata;
	}

	public final void setPropertyData(PropertyMetadata metadata) {
		this.metadata = metadata;
	}

	public final void clear() {
		final Object oldValue = getValue();
		if(oldValue != null) {
			doSetValue(null);
			getChangeSupport().firePropertyChange(propertyName, oldValue, getValue());
		}
	}

	/**
	 * Sub-classes implment this method to perform the actual value setting.
	 * @param value
	 * @throws IllegalArgumentException
	 */
	protected abstract void doSetValue(Object value) throws IllegalArgumentException;

	public final void setValue(Object value) throws IllegalArgumentException {
		final Object oldValue = getValue();
		if(!ObjectUtil.equals(oldValue, value)) {
			doSetValue(value);
			getChangeSupport().firePropertyChange(propertyName, oldValue, getValue());
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(propertyName);
		sb.append("(" + (getValue() == null ? "null" : getValue().toString()) + ")");
		return sb.toString();
	}
}
