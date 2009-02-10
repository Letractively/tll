/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.IEntityType;

/**
 * EntityFetchPrototypeRequest
 * @author jpk
 */
public class EntityFetchPrototypeRequest extends EntityRequest {

	private IEntityType entityType;

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
	public EntityFetchPrototypeRequest(IEntityType entityType, boolean generate) {
		super();
		this.entityType = entityType;
		this.generate = generate;
	}

	@Override
	public IEntityType getEntityType() {
		return entityType;
	}

	/**
	 * @return the generate
	 */
	public boolean isGenerate() {
		return generate;
	}

}
