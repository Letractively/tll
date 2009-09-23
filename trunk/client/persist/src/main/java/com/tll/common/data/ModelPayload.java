/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data;

import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * ModelPayload - To transport model and any supporting aux data to the client.
 * @author jpk
 */
public final class ModelPayload extends AuxDataPayload {

	/**
	 * The model.
	 */
	private Model model;

	//private Model changedModel;

	private ModelKey ref;

	/**
	 * Constructor
	 */
	public ModelPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public ModelPayload(Status status) {
		super(status);
	}

	/**
	 * Constructor
	 * @param status
	 * @param model
	 */
	public ModelPayload(Status status, Model model) {
		super(status);
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ModelKey getRef() {
		return ref;
	}

	public void setRef(ModelKey ref) {
		this.ref = ref;
	}
}
