package com.tll.client.model;

/**
 * CopyBinding - Used for deep copying {@link Model} instances.
 * @author jpk
 */
class CopyBinding extends PropBinding {

	Model target;

	/**
	 * Constructor
	 * @param source
	 * @param target
	 */
	CopyBinding(Model source, Model target) {
		super(source);
		this.target = target;
	}
}