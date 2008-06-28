/**
 * The Logic Lab
 * @author jpk
 * Feb 15, 2008
 */
package com.tll.client.model;

/**
 * RelatedOnePropPathBinding - Represents a "reference" to a
 * {@link ModelRefProperty} held in a parent {@link Model}.
 * @author jpk
 */
final class RelatedOnePropPathBinding extends PropPathBinding {

	/**
	 * The bound group property value. May be <code>null</code>.
	 */
	private final ModelRefProperty mpv;

	/**
	 * Constructor
	 * @param parent
	 * @param propPath
	 * @param mpv The bound {@link ModelRefProperty}. May be <code>null</code>
	 *        indicating the property is unset.
	 */
	RelatedOnePropPathBinding(Model parent, PropertyPath propPath, ModelRefProperty mpv) {
		super(parent, propPath);
		this.mpv = mpv;
	}

	@Override
	IPropertyBinding getPropertyBinding() throws UnsetPropertyException {
		if(mpv == null) {
			throw new UnsetPropertyException(propPath.toString());
		}
		return mpv;
	}

}
