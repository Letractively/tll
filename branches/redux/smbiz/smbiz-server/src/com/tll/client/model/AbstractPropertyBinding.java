/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.model;

/**
 * AbstractPropertyBinding
 * @author jpk
 */
public abstract class AbstractPropertyBinding implements IPropertyBinding {

	/**
	 * The property name.
	 */
	protected String propertyName;

	/**
	 * Constructor
	 */
	public AbstractPropertyBinding() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractPropertyBinding(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	public final String getPropertyName() {
		return propertyName;
	}

	public final void setPropertyName(String name) {
		this.propertyName = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final AbstractPropertyBinding other = (AbstractPropertyBinding) obj;
		if(propertyName == null) {
			if(other.propertyName != null) return false;
		}
		else if(!propertyName.equals(other.propertyName)) return false;
		return true;
	}

}
