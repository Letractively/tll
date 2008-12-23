/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.BaseEvent;
import com.tll.client.data.Status;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;

/**
 * ModelChangeEvent - Used to dissemminate model changes.
 * @author jpk
 */
public final class ModelChangeEvent extends BaseEvent {

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
	public ModelChangeEvent(Widget source, ModelChangeOp change, Model model, Status status) {
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
	public ModelChangeEvent(Widget source, ModelChangeOp change, RefKey modelRef, Status status) {
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
	public ModelChangeEvent(Widget source, ModelChangeOp change) {
		this(source, change, (Model) null, null);

	}

	public ModelChangeOp getChangeOp() {
		return change;
	}

	public Model getModel() {
		return model;
	}

	public RefKey getModelRef() {
		return modelRef;
	}

	public Status getStatus() {
		return status;
	}
}
