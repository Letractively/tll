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

	private boolean dirtyProps;

	/**
	 * Constructor
	 */
	public PersistRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param model
	 * @param dirtyProps Signifies the held model data is for updating and
	 *        contains <em>only</em> those properties that were marked as dirty during the ui edit process.
	 */
	public PersistRequest(Model model, boolean dirtyProps) {
		super();
		this.model = model;
		this.dirtyProps = dirtyProps;
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


	/**
	 * @return the dirtyProps
	 */
	public boolean isDirtyProps() {
		return dirtyProps;
	}
}
