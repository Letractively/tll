/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.client.model.RefKey;
import com.tll.criteria.Criteria;
import com.tll.model.EntityType;

/**
 * EntityLoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityRequest {

	private RefKey entityRef;

	private Criteria criteria;

	private boolean loadByName;

	private boolean loadByBusinessKey;

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
	public EntityLoadRequest(Criteria criteria) {
		super();
		this.criteria = criteria;
		this.loadByBusinessKey = true;
	}

	@Override
	public EntityType getEntityType() {
		return loadByBusinessKey ? criteria.getEntityType() : entityRef.getType();
	}

	public boolean isLoadByName() {
		return loadByName;
	}

	public boolean isLoadByBusinessKey() {
		return loadByBusinessKey;
	}

	/**
	 * @return the entityRef
	 */
	public RefKey getEntityRef() {
		return entityRef;
	}

	/**
	 * @return The criteria used for marshalling the business key
	 */
	public Criteria getCriteria() {
		return criteria;
	}

}
