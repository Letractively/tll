/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.Model;
import com.tll.model.IEntityType;

/**
 * EntityPersistRequest
 * @author jpk
 */
public class EntityPersistRequest extends EntityRequest {

	private Model entity;

	/**
	 * Constructor
	 */
	public EntityPersistRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param entity
	 */
	public EntityPersistRequest(Model entity) {
		super();
		this.entity = entity;
	}

	@Override
	public IEntityType getEntityType() {
		return entity.getEntityType();
	}

	/**
	 * @return the entity
	 */
	public Model getEntity() {
		return entity;
	}

}
