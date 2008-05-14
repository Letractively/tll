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
public abstract class AbstractPropertyValue extends AbstractPropertyBinding implements IPropertyValue, IQueryParam {

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

	/**
	 * NOTE: This method is used for debugging.
	 */
	@Override
	public final String toString() {
		String pn = propertyName;
		return pn + "=" + (getValue() == null ? "<UNSET>" : getValue().toString());
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
