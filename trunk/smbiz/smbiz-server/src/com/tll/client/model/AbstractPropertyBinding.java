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
}
