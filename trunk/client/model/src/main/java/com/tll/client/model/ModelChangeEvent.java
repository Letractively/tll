/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.model;

import com.google.gwt.event.shared.GwtEvent;
import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * ModelChangeEvent - Used to dissemminate model changes.
 * @author jpk
 */
public final class ModelChangeEvent extends GwtEvent<IModelChangeHandler> {

	/**
	 * The event type.
	 */
	public static final Type<IModelChangeHandler> TYPE = new Type<IModelChangeHandler>();

	/**
	 * ModelChangeOp
	 * @author jpk
	 */
	public static enum ModelChangeOp {
		AUXDATA_READY,
		LOADED,
		ADDED,
		UPDATED,
		DELETED;
	}

	private final ModelChangeOp change;
	private final Model model;
	private final ModelKey modelKey;

	private final Status status;

	/**
	 * Constructor
	 * @param change
	 * @param model
	 * @param modelKey
	 * @param status
	 */
	public ModelChangeEvent(ModelChangeOp change, Model model, ModelKey modelKey, Status status) {
		this.change = change;
		this.model = model;
		this.modelKey = modelKey;
		this.status = status;
	}

	public ModelChangeOp getChangeOp() {
		return change;
	}

	public Model getModel() {
		return model;
	}

	public ModelKey getModelKey() {
		return model == null ? modelKey : model.getKey();
	}

	public Status getStatus() {
		return status;
	}

	@Override
	protected void dispatch(IModelChangeHandler handler) {
		handler.onModelChangeEvent(this);
	}

	@Override
	public Type<IModelChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public String toString() {
		String s = change.toString();
		final ModelKey rk = getModelKey();
		if(rk != null) {
			s += " [ " + rk.toString() + " ]";
		}
		return s;
	}
}
