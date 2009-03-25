package com.tll.client.mvc.view;

import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * EditViewRequest
 * @author jpk
 */
public final class EditViewRequest extends ShowViewRequest {

	private final ViewClass viewClass;

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
	 * @param viewClass
	 * @param modelRef
	 */
	public EditViewRequest(ViewClass viewClass, ModelKey modelRef) {
		this.viewClass = viewClass;
		this.modelRef = modelRef;
		this.model = null;
	}

	/**
	 * Constructor - Use when the entity group is available thus avoiding a server
	 * request.
	 * @param viewClass
	 * @param model
	 */
	public EditViewRequest(ViewClass viewClass, Model model) {
		this.viewClass = viewClass;
		this.modelRef = null;
		this.model = model;
	}

	@Override
	public IViewKey getViewKey() {
		final int vid = model == null ? modelRef.hashCode() : model.getRefKey().hashCode();
		return new ViewKey(viewClass, vid);
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