/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2008
 */
package com.tll.client.model;

import com.tll.criteria.IQueryParam;
import com.tll.model.schema.PropertyMetadata;

/**
 * AbstractPropertyValue
 * @author jpk
 */
public abstract class AbstractPropertyValue extends AbstractModelProperty implements IPropertyValue, IQueryParam {

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

	@Override
	public final void clear() {
		final Object oldValue = getValue();
		if(oldValue != null) {
			doSetValue(null);
			if(changeSupport != null) changeSupport.firePropertyChange(propertyName, oldValue, getValue());
		}
	}

	/**
	 * Sub-classes implment this method to perform the actual value setting.
	 * @param value
	 * @throws IllegalArgumentException
	 */
	protected abstract void doSetValue(Object value) throws IllegalArgumentException;

	@Override
	public final void setValue(Object value) throws IllegalArgumentException {
		final Object oldValue = getValue();
		if((value != oldValue) || (value != null && !value.equals(oldValue))
				|| (oldValue != null && !oldValue.equals(value))) {
			doSetValue(value);
			if(changeSupport != null) changeSupport.firePropertyChange(propertyName, oldValue, getValue());
		}
	}

	@Override
	public final String toString() {
		String pn = propertyName;
		return pn + "=" + (getValue() == null ? "<UNSET>" : getValue().toString());
	}
}
