/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;

/**
 * ModelChangeEvent
 * @author jpk
 */
public final class ModelChangeEvent extends BaseEvent {

	public static enum ModelChangeOp {
		ADD,
		UPDATE,
		DELETE;
	}

	private final ModelChangeOp change;
	private final Model model;
	private final RefKey modelRef;

	/**
	 * Constructor - Use for add and update model change events.
	 * @param source
	 * @param change
	 * @param model
	 */
	public ModelChangeEvent(Widget source, ModelChangeOp change, Model model) {
		super(source);
		this.change = change;
		this.model = model;
		this.modelRef = null;
	}

	/**
	 * Constructor - Use for delete model change events.
	 * @param source
	 * @param change
	 * @param modelRef
	 */
	public ModelChangeEvent(Widget source, ModelChangeOp change, RefKey modelRef) {
		super(source);
		this.change = change;
		this.model = null;
		this.modelRef = modelRef;
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
}
