/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.client.model.RefKey;
import com.tll.model.EntityType;

/**
 * EntityLoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityRequest {

	private RefKey entityRef;

	private boolean loadByName;

	/**
	 * Constructor
	 */
	public EntityLoadRequest() {
		super();
	}

	/**
	 * Constructor - Use for loading by name.
	 * @param entityType
	 * @param name
	 */
	public EntityLoadRequest(EntityType entityType, String name) {
		super();
		this.entityRef = new RefKey(entityType, null, name);
		this.loadByName = true;
	}

	/**
	 * Constructor - Use for loading by primary key.
	 * @param entityRef
	 */
	public EntityLoadRequest(RefKey entityRef) {
		super();
		this.entityRef = entityRef;
		this.loadByName = false;
	}

	public String descriptor() {
		return "Entity load request";
	}

	@Override
	public EntityType getEntityType() {
		return entityRef.getType();
	}

	/**
	 * @return the loadByName
	 */
	public boolean isLoadByName() {
		return loadByName;
	}

	/**
	 * @return the entityRef
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}

}
