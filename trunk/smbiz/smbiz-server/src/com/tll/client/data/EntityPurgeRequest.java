/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.client.model.RefKey;
import com.tll.model.EntityType;

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

	@Override
	public EntityType getEntityType() {
		return entityRef.getType();
	}

	/**
	 * @return the entityRef
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}

}
