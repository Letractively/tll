/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2008
 */
package com.tll.common.model;

import com.tll.criteria.IQueryParam;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
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
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		final AbstractPropertyValue other = (AbstractPropertyValue) obj;
		final Object v = getValue();
		if(v == null) {
			if(other.getValue() != null) return false;
		}
		else if(!v.equals(other.getValue())) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		final int result = super.hashCode();
		final Object v = getValue();
		return 31 * result + ((v == null) ? 0 : v.hashCode());
	}

	@Override
	public final String toString() {
		final String pn = propertyName;
		return pn + "=" + (getValue() == null ? "<UNSET>" : getValue().toString());
	}
}
