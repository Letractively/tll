/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.data;

import com.tll.model.EntityType;

/**
 * EntityGetEmptyRequest
 * @author jpk
 */
public class EntityGetEmptyRequest extends EntityRequest {

	private EntityType entityType;

	/**
	 * Tells the server to generate (set the id) of a newly created entity.
	 */
	private boolean generate;

	/**
	 * Constructor
	 */
	public EntityGetEmptyRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param generate
	 */
	public EntityGetEmptyRequest(EntityType entityType, boolean generate) {
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
