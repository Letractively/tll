/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.client.model.RefKey;
import com.tll.model.EntityType;
import com.tll.model.key.BusinessKey;

/**
 * EntityLoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityRequest {

	private RefKey entityRef;

	private BusinessKey businessKey;

	private boolean loadByName;

	/**
	 * Constructor
	 */
	public EntityLoadRequest() {
		super();
	}

	/**
	 * Constructor - Use for loading by primary key.
	 * @param entityRef
	 */
	public EntityLoadRequest(RefKey entityRef) {
		super();
		this.entityRef = entityRef;
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
	 * Constructor
	 * @param criteria
	 */
	public EntityLoadRequest(BusinessKey businessKey) {
		super();
		this.businessKey = businessKey;
	}

	@Override
	public EntityType getEntityType() {
		// TODO fix
		return null;
	}

	public boolean isLoadByName() {
		return loadByName;
	}

	/**
	 * @return the entityRef
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}

	/**
	 * @return the businessKey
	 */
	public BusinessKey getBusinessKey() {
		return businessKey;
	}
}
