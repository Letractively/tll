/**
 * The Logic Lab
 * @author jpk
 * Feb 15, 2008
 */
package com.tll.client.model;

/**
 * IndexablePropPathBinding - Binding to a {@link RelatedManyProperty}.
 * @author jpk
 */
final class IndexablePropPathBinding extends PropPathBinding {

	private final RelatedManyProperty rmpv;

	/**
	 * Constructor
	 * @param parent The parent model
	 * @param propPath
	 * @param rmpv The bound related many property value
	 */
	IndexablePropPathBinding(Model parent, PropertyPath propPath, RelatedManyProperty rmpv) {
		super(parent, propPath);
		this.rmpv = rmpv;
	}

	@Override
	IPropertyBinding getPropertyBinding() throws UnsetPropertyException {
		if(rmpv == null) {
			throw new UnsetPropertyException(propPath.toString());
		}
		return rmpv;
	}
}
