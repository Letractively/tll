/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.RefKey;
import com.tll.common.search.ISearch;
import com.tll.model.EntityType;

/**
 * EntityLoadRequest
 * @author jpk
 */
public class EntityLoadRequest extends EntityRequest {

	private RefKey entityRef;

	private ISearch search;

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
	 * @param search
	 */
	public EntityLoadRequest(ISearch search) {
		super();
		this.search = search;
		this.loadByBusinessKey = true;
	}

	@Override
	public EntityType getEntityType() {
		return loadByBusinessKey ? search.getEntityType() : entityRef.getType();
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
	 * @return The search used for marshalling the business key
	 */
	public ISearch getSearch() {
		return search;
	}

}
