/**
 * The Logic Lab
 * @author jpk
 * Feb 18, 2008
 */
package com.tll.client.model;

/**
 * AbstractPropertyValue
 * @author jpk
 */
public abstract class AbstractPropertyValue extends AbstractPropertyBinding implements IPropertyValue {

	/**
	 * The optional property meta data.
	 */
	protected PropertyData pdata;

	/**
	 * Constructor
	 */
	public AbstractPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param pdata
	 */
	public AbstractPropertyValue(String propertyName, PropertyData pdata) {
		super(propertyName);
		this.pdata = pdata;
	}

	public final PropertyData getPropertyData() {
		return pdata;
	}

	public final void setPropertyData(PropertyData pdata) {
		this.pdata = pdata;
	}

	/**
	 * NOTE: This method is used for debugging.
	 */
	@Override
	public final String toString() {
		String pn = propertyName;
		return pn + "=" + (getValue() == null ? "<UNSET>" : getValue().toString());
	}
}
