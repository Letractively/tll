/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.model;

import java.util.EventObject;

import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;

/**
 * ModelChangeEvent - Used to dissemminate model changes.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class ModelChangeEvent extends EventObject {

	public static enum ModelChangeOp {
		AUXDATA_READY,
		LOADED,
		ADDED,
		UPDATED,
		DELETED;
	}

	private final ModelChangeOp change;
	private final Model model;
	private final RefKey modelRef;

	private final Status status;

	/**
	 * Constructor - Use for add and update model change events.
	 * @param source
	 * @param change
	 * @param model
	 * @param status
	 */
	public ModelChangeEvent(Object source, ModelChangeOp change, Model model, Status status) {
		super(source);
		this.change = change;
		this.model = model;
		this.modelRef = null;
		this.status = status;
	}

	/**
	 * Constructor - Use for delete model change events.
	 * @param source
	 * @param change
	 * @param modelRef
	 * @param status
	 */
	public ModelChangeEvent(Object source, ModelChangeOp change, RefKey modelRef, Status status) {
		super(source);
		this.change = change;
		this.model = null;
		this.modelRef = modelRef;
		this.status = status;
	}

	/**
	 * Constructor - Use for {@link ModelChangeOp#AUXDATA_READY} events.
	 * @param source
	 * @param change Expected to be a {@link ModelChangeOp#AUXDATA_READY} event
	 */
	public ModelChangeEvent(Object source, ModelChangeOp change) {
		this(source, change, (Model) null, null);

	}

	public ModelChangeOp getChangeOp() {
		return change;
	}

	public Model getModel() {
		return model;
	}

	public RefKey getModelRef() {
		return modelRef == null ? (model == null ? null : model.getRefKey()) : modelRef;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		String s = change.toString();
		RefKey rk = getModelRef();
		if(rk != null) {
			s += " [ " + rk.toString() + " ]";
		}
		return s;
	}
}
