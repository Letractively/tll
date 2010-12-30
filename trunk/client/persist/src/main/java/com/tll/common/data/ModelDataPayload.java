/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.common.model.Model;
import com.tll.common.msg.Status;

/**
 * Construct to hold model data based on what is requested in model related
 * request.
 * @see ModelDataRequest
 * @author jpk
 */
public class ModelDataPayload extends Payload {

	/**
	 * Map of entity lists keyed by the entity type.
	 */
	protected Map<String, List<Model>> entityMap;

	/**
	 * Set of entity prototypes
	 */
	protected Set<Model> entityPrototypes;

	/**
	 * Constructor
	 */
	public ModelDataPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public ModelDataPayload(Status status) {
		super(status);
	}

	public Map<String, List<Model>> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, List<Model>> entityMap) {
		this.entityMap = entityMap;
	}

	public Set<Model> getEntityPrototypes() {
		return entityPrototypes;
	}

	public void setEntityPrototypes(Set<Model> entityPrototypes) {
		this.entityPrototypes = entityPrototypes;
	}

}
