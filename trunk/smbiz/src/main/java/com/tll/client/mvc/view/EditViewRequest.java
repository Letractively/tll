package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * EditViewRequest
 * @author jpk
 */
@SuppressWarnings("serial")
public final class EditViewRequest extends ShowViewRequest {

	/**
	 * The entity model. May be <code>null</code> in which case,
	 * {@link #modelRef} is expected to be non-<code>null</code>.
	 */
	// TODO do we want to drive edit view's from a model ref always? (i.e. remove
	// this member?)
	private final Model model;

	/**
	 * The entity ref. May be <code>null</code> in which case, {@link #model} is
	 * expected to be non-<code>null</code>.
	 */
	private final ModelKey modelRef;

	/**
	 * Constructor - Use when only an entity ref is available. This implies a
	 * server request for the referred entity.
	 * @param source
	 * @param viewClass
	 * @param modelRef
	 */
	public EditViewRequest(Widget source, ViewClass viewClass, ModelKey modelRef) {
		super(source, viewClass);
		assert modelRef != null;
		this.modelRef = modelRef;
		this.model = null;
	}

	/**
	 * Constructor - Use when the entity group is available thus avoiding a server
	 * request.
	 * @param source
	 * @param viewClass
	 * @param model
	 */
	public EditViewRequest(Widget source, ViewClass viewClass, Model model) {
		super(source, viewClass);
		this.modelRef = null;
		assert model != null;
		this.model = model;
	}

	@Override
	protected int getViewId() {
		return model == null ? modelRef.hashCode() : model.getRefKey().hashCode();
	}

	/**
	 * @return The entity model.
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return The entity ref.
	 */
	public ModelKey getModelKey() {
		return modelRef;
	}
}