/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.client.model.RefKey;

/**
 * EntityPurgeRequest
 * @author jpk
 */
public class EntityPurgeRequest extends EntityRequest {

	private RefKey entityRef;

	/**
	 * Constructor
	 */
	public EntityPurgeRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param entityRef
	 */
	public EntityPurgeRequest(RefKey entityRef) {
		super();
		assert entityRef != null;
		this.entityRef = entityRef;
	}

	public String descriptor() {
		return "Entity purge request";
	}

	@Override
	public String getEntityType() {
		return entityRef.getType();
	}

	/**
	 * @return the entityRef
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}

}
