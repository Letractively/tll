/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.IMarshalable;

/**
 * PersistRequest - Model data transport for add/update crud ops.
 * @param <M> model type
 * @author jpk
 */
public class PersistRequest<M extends IMarshalable> extends AbstractModelRequest {

	private M model;

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
	public PersistRequest(M model, boolean dirtyProps) {
		super();
		this.model = model;
		this.dirtyProps = dirtyProps;
	}

	@Override
	public String descriptor() {
		return model == null ? "Persist Request" : "Persist Request for: " + model;
	}

	/**
	 * @return the entity
	 */
	public M getModel() {
		return model;
	}


	/**
	 * @return the dirtyProps
	 */
	public boolean isDirtyProps() {
		return dirtyProps;
	}
}
