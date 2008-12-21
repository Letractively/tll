/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.model;

/**
 * AbstractModelProperty
 * @author jpk
 */
public abstract class AbstractModelProperty implements IModelProperty {

	/**
	 * The property name.
	 */
	protected String propertyName;

	/**
	 * Constructor
	 */
	public AbstractModelProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	protected AbstractModelProperty(String propertyName) {
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
		final AbstractModelProperty other = (AbstractModelProperty) obj;
		if(propertyName == null) {
			if(other.propertyName != null) return false;
		}
		else if(!propertyName.equals(other.propertyName)) return false;
		return true;
	}

}
