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
	 * Constructor - Use for add and update model change events.
	 * @param change
	 * @param model
	 * @param status
	 */
	public ModelChangeEvent(ModelChangeOp change, Model model, Status status) {
		this.change = change;
		this.model = model;
		this.modelKey = null;
		this.status = status;
	}

	/**
	 * Constructor - Use for delete model change events.
	 * @param change
	 * @param modelKey
	 * @param status
	 */
	public ModelChangeEvent(ModelChangeOp change, ModelKey modelKey, Status status) {
		this.change = change;
		this.model = null;
		this.modelKey = modelKey;
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

	public ModelKey getModelKey() {
		// NOTE: it is expected that the model's provided key
		// is the exact same in terms of equals() and hashCode()
		// behavior as the provided standalone model key!
		return modelKey == null ? (model == null ? null : model.getKey()) : modelKey;
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
