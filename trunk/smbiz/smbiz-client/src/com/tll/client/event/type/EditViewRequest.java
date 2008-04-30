package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.view.ViewClass;

/**
 * EditViewRequest
 * @author jpk
 */
public final class EditViewRequest extends ShowViewRequest {

	/**
	 * The entity. May be <code>null</code> in which case, {@link #modelRef} is
	 * expected to be non-<code>null</code>.
	 */
	private final Model entity;

	/**
	 * The entity ref. May be <code>null</code> in which case, {@link #entity}
	 * is expected to be non-<code>null</code>.
	 */
	private final RefKey entityRef;

	/**
	 * Constructor - Use when only an entity ref is available. This implies a
	 * server request for the referred entity.
	 * @param source
	 * @param viewClass
	 * @param entityRef
	 */
	public EditViewRequest(Widget source, ViewClass viewClass, RefKey entityRef) {
		super(source, viewClass);
		assert entityRef != null;
		this.entityRef = entityRef;
		this.entity = null;
	}

	/**
	 * Constructor - Use when the entity group is available thus avoiding a server
	 * request.
	 * @param source
	 * @param viewClass
	 * @param entity
	 */
	public EditViewRequest(Widget source, ViewClass viewClass, Model entity) {
		super(source, viewClass);
		this.entityRef = null;
		assert entity != null;
		this.entity = entity;
	}

	@Override
	protected int getViewId() {
		return entity == null ? entityRef.hashCode() : entity.getRefKey().hashCode();
	}

	/**
	 * @return The entity model.
	 */
	public Model getEntity() {
		return entity;
	}

	/**
	 * @return The entity ref.
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}
}