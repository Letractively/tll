/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * EntityPurgeRequest
 * @author jpk
 */
public class PurgeRequest extends AbstractModelRequest {

	/**
	 * This member is set when the purge request is for a server side defined
	 * entity and this key identifies it.
	 */
	private ModelKey entityRef;

	/**
	 * This member is set when the purge request is non-entity based and is
	 * implementation specific.
	 */
	private Model model;

	/**
	 * Constructor
	 */
	public PurgeRequest() {
		super();
	}

	/**
	 * Constructor - Use this constructor when the purge request is for a server
	 * side entity.
	 * @param entityRef
	 */
	public PurgeRequest(ModelKey entityRef) {
		super();
		if(entityRef == null) throw new IllegalArgumentException("Null model ref");
		this.entityRef = entityRef;
	}

	/**
	 * Constructor - Use this constructor when the purge reqeust is implmentation
	 * specific.
	 * @param model
	 */
	public PurgeRequest(Model model) {
		super();
		if(model == null) throw new IllegalArgumentException("Null model");
		this.model = model;
	}

	@Override
	public String descriptor() {
		return entityRef == null ? model == null ? "Persist Request" : "Persist Request for: " + model.descriptor()
				: "Persist Request for: " + entityRef.descriptor();
	}

	/**
	 * @return the entityRef
	 */
	public ModelKey getEntityRef() {
		return entityRef;
	}

	/**
	 * @return the non-entity model data that indicates what is to be purged and
	 *         is impl specific.
	 */
	public Model getModel() {
		return model;
	}
}
