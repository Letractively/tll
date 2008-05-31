/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.msg.Msg;

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

	private final List<Msg> errors;

	/**
	 * Constructor - Use for add and update model change events.
	 * @param source
	 * @param change
	 * @param model
	 * @param errors
	 */
	public ModelChangeEvent(Widget source, ModelChangeOp change, Model model, List<Msg> errors) {
		super(source);
		this.change = change;
		this.model = model;
		this.modelRef = null;
		this.errors = errors;
	}

	/**
	 * Constructor - Use for delete model change events.
	 * @param source
	 * @param change
	 * @param modelRef
	 * @param errors
	 */
	public ModelChangeEvent(Widget source, ModelChangeOp change, RefKey modelRef, List<Msg> errors) {
		super(source);
		this.change = change;
		this.model = null;
		this.modelRef = modelRef;
		this.errors = errors;
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

	public boolean isError() {
		return errors != null && errors.size() > 0;
	}

	public List<Msg> getErrors() {
		return errors;
	}
}
