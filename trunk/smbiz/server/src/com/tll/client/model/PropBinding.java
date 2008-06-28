/**
 * 
 */
package com.tll.client.model;

/**
 * PropBinding - Abstract property related binding used for various routines
 * within the prop package.
 * @author jpk
 */
class PropBinding {

	protected Model model;

	/**
	 * Constructor
	 */
	PropBinding() {
		super();
	}

	/**
	 * Constructor
	 * @param model
	 */
	PropBinding(Model model) {
		super();
		this.model = model;
	}

	/**
	 * @return The {@link Model}
	 */
	Model getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 * @param model The {@link Model} to set
	 */
	void setModel(Model model) {
		this.model = model;
	}

}
