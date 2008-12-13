package com.tll.client.model;

/**
 * PropPathBinding - Holds necessary info for performing subsequent actions
 * after a given property path is resolved.
 * @author jpk
 */
abstract class PropPathBinding extends PropBinding {

	/**
	 * The parsed property path.
	 */
	protected final PropertyPath propPath;

	/**
	 * Constructor
	 * @param propPath The parsed property path. May NOT be <code>null</code>.
	 */
	protected PropPathBinding(PropertyPath propPath) {
		super();
		assert propPath != null;
		this.propPath = propPath;
	}

	/**
	 * Constructor
	 * @param parent The parent model
	 * @param propPath
	 */
	public PropPathBinding(Model parent, PropertyPath propPath) {
		super(parent);
		this.propPath = propPath;
	}

	final PropertyPath getPropPath() {
		return propPath;
	}

	/**
	 * @return The bound model property with <em>no</em> alterations made to the
	 *         model
	 * @throws UnsetPropertyException When the model property is not set (
	 *         <code>null</code>).
	 */
	abstract IModelProperty getModelProperty() throws UnsetPropertyException;
}