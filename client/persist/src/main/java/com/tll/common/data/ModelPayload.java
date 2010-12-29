/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data;

import com.tll.IMarshalable;
import com.tll.common.msg.Status;
import com.tll.model.ModelKey;

/**
 * Generic model data transport.
 * @author jpk
 * @param <M> model type
 */
public final class ModelPayload<M extends IMarshalable> extends ModelDataPayload<M> {

	/**
	 * The model.
	 */
	private M model;

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
	public ModelPayload(Status status, M model) {
		super(status);
		this.model = model;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public ModelKey getRef() {
		return ref;
	}

	public void setRef(ModelKey ref) {
		this.ref = ref;
	}
}
