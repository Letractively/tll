/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.IEntityType;
import com.tll.common.model.ModelKey;

/**
 * EntityPurgeRequest
 * @author jpk
 */
public class EntityPurgeRequest extends EntityRequest {

	private ModelKey entityRef;

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
	public EntityPurgeRequest(ModelKey entityRef) {
		super();
		assert entityRef != null;
		this.entityRef = entityRef;
	}

	@Override
	public IEntityType getEntityType() {
		return entityRef.getEntityType();
	}

	/**
	 * @return the entityRef
	 */
	public ModelKey getEntityRef() {
		return entityRef;
	}

}
