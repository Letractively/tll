/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;

/**
 * AuxDataPayload - Construct to hold aux (enum maps, ref data maps and related
 * on entity refs) data based on what is requested in {@link AuxDataRequest}.
 * @see AuxDataRequest
 * @author jpk
 */
public class AuxDataPayload extends Payload {

	/**
	 * Constructor
	 */
	public AuxDataPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public AuxDataPayload(Status status) {
		super(status);
	}

	/**
	 * Map of entity lists keyed by the entity type.
	 */
	protected Map<IEntityType, List<Model>> entityMap;

	/**
	 * Set of entity prototypes
	 */
	protected Set<Model> entityPrototypes;

	public Map<IEntityType, List<Model>> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<IEntityType, List<Model>> entityMap) {
		this.entityMap = entityMap;
	}

	public Set<Model> getEntityPrototypes() {
		return entityPrototypes;
	}

	public void setEntityPrototypes(Set<Model> entityPrototypes) {
		this.entityPrototypes = entityPrototypes;
	}

}
