/**
 * The Logic Lab
 * @author jpk
 * Feb 16, 2008
 */
package com.tll.client.model;

/**
 * UnsetPropPathBinding - Property path binding that points to an absent
 * property value under a given {@link Model}.
 * @author jpk
 */
final class UnsetPropPathBinding extends PropPathBinding {

	/**
	 * Constructor
	 * @param parent
	 * @param propPath
	 */
	public UnsetPropPathBinding(Model parent, PropertyPath propPath) {
		super(parent, propPath);
	}

	@Override
	IPropertyValue getPropertyBinding() throws UnsetPropertyException {
		throw new UnsetPropertyException(propPath.toString());
	}
}
