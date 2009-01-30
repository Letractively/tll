/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.model.EntityType;

/**
 * EntityFetchPrototypeRequest
 * @author jpk
 */
public class EntityFetchPrototypeRequest extends EntityRequest {

	private EntityType entityType;

	/**
	 * Tells the server to generate (set the id) of a newly created entity.
	 */
	private boolean generate;

	/**
	 * Constructor
	 */
	public EntityFetchPrototypeRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param generate
	 */
	public EntityFetchPrototypeRequest(EntityType entityType, boolean generate) {
		super();
		this.entityType = entityType;
		this.generate = generate;
	}

	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * @return the generate
	 */
	public boolean isGenerate() {
		return generate;
	}

}
