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
	private final ModelKey modelRef;

	private final Status status;

	/**
	 * Constructor - Use for add and update model change events.
	 * @param change
	 * @param model
	 * @param status
	 */
	public ModelChangeEvent(ModelChangeOp change, Model model, Status status) {
		this.change = change;
		this.model = model;
		this.modelRef = null;
		this.status = status;
	}

	/**
	 * Constructor - Use for delete model change events.
	 * @param change
	 * @param modelRef
	 * @param status
	 */
	public ModelChangeEvent(ModelChangeOp change, ModelKey modelRef, Status status) {
		this.change = change;
		this.model = null;
		this.modelRef = modelRef;
		this.status = status;
	}

	/**
	 * Constructor - Use for {@link ModelChangeOp#AUXDATA_READY} events.
	 * @param change Expected to be a {@link ModelChangeOp#AUXDATA_READY} event
	 */
	public ModelChangeEvent(ModelChangeOp change) {
		this(change, (Model) null, null);

	}

	public ModelChangeOp getChangeOp() {
		return change;
	}

	public Model getModel() {
		return model;
	}

	public ModelKey getModelRef() {
		return modelRef == null ? (model == null ? null : model.getRefKey()) : modelRef;
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
		final ModelKey rk = getModelRef();
		if(rk != null) {
			s += " [ " + rk.toString() + " ]";
		}
		return s;
	}
}
