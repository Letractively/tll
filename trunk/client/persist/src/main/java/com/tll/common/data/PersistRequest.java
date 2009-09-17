/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.Model;

/**
 * PersistRequest - Model data transport for add/update crud ops.
 * @author jpk
 */
public class PersistRequest extends ModelRequest {

	private Model model;

	/**
	 * Constructor
	 */
	public PersistRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param model
	 */
	public PersistRequest(Model model) {
		super();
		this.model = model;
	}

	@Override
	public String descriptor() {
		return model == null ? "Persist Request" : "Persist Request for: " + model.descriptor();
	}

	/**
	 * @return the entity
	 */
	public Model getModel() {
		return model;
	}
}
