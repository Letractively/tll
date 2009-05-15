/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;

/**
 * PersistRequest
 * @author jpk
 */
public class PersistRequest extends EntityModelRequest {

	private Model model;

	/**
	 * The entity options. May be <code>null</code>.
	 */
	private EntityOptions entityOptions;

	/**
	 * Constructor
	 */
	public PersistRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param model
	 */
	public PersistRequest(Model model) {
		super();
		this.model = model;
	}

	@Override
	public String descriptor() {
		return model == null ? "Persist Request" : "Persist Request for: " + model.descriptor();
	}

	@Override
	public IEntityType getEntityType() {
		return model.getEntityType();
	}

	/**
	 * @return the entity
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return the entityOptions
	 */
	public EntityOptions getEntityOptions() {
		return entityOptions;
	}

	/**
	 * @param entityOptions the entityOptions to set
	 */
	public void setEntityOptions(EntityOptions entityOptions) {
		this.entityOptions = entityOptions;
	}
}
