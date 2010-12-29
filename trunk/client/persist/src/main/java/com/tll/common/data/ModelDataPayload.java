/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.IMarshalable;
import com.tll.common.msg.Status;

/**
 * Construct to hold model data based on what is requested in model related
 * request.
 * @param <M> model type
 * @see ModelDataRequest
 * @author jpk
 */
public class ModelDataPayload<M extends IMarshalable> extends Payload {

	/**
	 * Map of entity lists keyed by the entity type.
	 */
	protected Map<String, List<M>> entityMap;

	/**
	 * Set of entity prototypes
	 */
	protected Set<M> entityPrototypes;

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

	public Map<String, List<M>> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, List<M>> entityMap) {
		this.entityMap = entityMap;
	}

	public Set<M> getEntityPrototypes() {
		return entityPrototypes;
	}

	public void setEntityPrototypes(Set<M> entityPrototypes) {
		this.entityPrototypes = entityPrototypes;
	}

}
