/**
 * The Logic Lab
 * @author jpk
 * Feb 15, 2008
 */
package com.tll.client.model;

/**
 * PropertyValuePropPathBinding - Reference to a non-relational
 * {@link IPropertyValue} held in a parent {@link Model}.
 * @author jpk
 */
class PropertyValuePropPathBinding extends PropPathBinding {

	protected AbstractPropertyValue rpv;

	/**
	 * Constructor
	 * @param propPath The parsed property path.
	 * @param rpv The property value
	 */
	PropertyValuePropPathBinding(PropertyPath propPath, AbstractPropertyValue rpv) {
		super(propPath);
		assert rpv != null;
		this.rpv = rpv;
	}

	@Override
	IPropertyValue getPropertyBinding() throws UnsetPropertyException {
		if(rpv == null) {
			throw new UnsetPropertyException(propPath.toString());
		}
		return rpv;
	}
}
