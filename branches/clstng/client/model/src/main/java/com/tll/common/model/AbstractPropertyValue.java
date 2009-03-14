/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2008
 */
package com.tll.common.model;

import com.tll.criteria.IQueryParam;
import com.tll.model.schema.PropertyMetadata;
import com.tll.util.ObjectUtil;

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
		String pn = propertyName;
		return pn + "=" + (getValue() == null ? "<UNSET>" : getValue().toString());
	}
}
