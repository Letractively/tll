package com.tll.client.mvc.view;

import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * EditViewInitializer
 * @author jpk
 */
public final class EditViewInitializer extends AbstractDynamicViewInitializer {

	/**
	 * The entity model. May be <code>null</code> in which case, {@link #modelRef}
	 * is expected to be non-<code>null</code>.
	 */
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
	public EditViewInitializer(ViewClass viewClass, ModelKey modelRef) {
		super(viewClass);
		this.modelRef = modelRef;
		this.model = null;
	}

	/**
	 * Constructor - Use when the entity group is available thus avoiding a server
	 * request.
	 * @param viewClass
	 * @param model
	 */
	public EditViewInitializer(ViewClass viewClass, Model model) {
		super(viewClass);
		this.modelRef = null;
		this.model = model;
	}

	@Override
	protected int getViewId() {
		return model == null ? modelRef.hashCode() : model.getKey().hashCode();
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