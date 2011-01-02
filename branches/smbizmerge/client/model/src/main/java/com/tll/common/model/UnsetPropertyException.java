/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.model;

/**
 * UnsetPropertyException - Indicates a model property does not exist.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class UnsetPropertyException extends PropertyPathException {

	final Model parentModel;
	final String parentProperty;

	/**
	 * Constructor
	 * @param propPath
	 * @param parentModel the model that is parent to <code>parentProperty</code>
	 *        which may be nested relative to the root model that raised this
	 *        exception
	 * @param parentProperty the last node path in the root relative property path
	 *        that raised this exception.
	 */
	public UnsetPropertyException(final String propPath, final Model parentModel, final String parentProperty) {
		super("Property: '" + propPath + "' does not exist.", propPath);
		this.parentModel = parentModel;
		this.parentProperty = parentProperty;
	}

	/**
	 * @return The unset property relative to the root model that raised this
	 *         exception
	 */
	public String getUnsetProperty() {
		return getPropertyPath();
	}

	/**
	 * @return the parent Model of the local non-path property that is unset
	 */
	public Model getParentModel() {
		return parentModel;
	}

	/**
	 * @return the local non-path property that is unset relative to the parent
	 *         model not the root model
	 */
	public String getParentProperty() {
		return parentProperty;
	}
}
