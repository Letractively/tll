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
	 * @param nullPropName
	 */
	public UnsetPropPathBinding(Model parent, PropertyPath propPath, String nullPropName) {
		super(propPath);
	}

	@Override
	IPropertyValue getPropertyBinding() {
		return null;
	}
}
