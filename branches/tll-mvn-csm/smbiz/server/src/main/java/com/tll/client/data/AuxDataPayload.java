/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.client.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.client.model.Model;
import com.tll.model.EntityType;
import com.tll.refdata.RefDataType;

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
	 * Map of app ref data name/value pairs keyed by the app ref data terse name.<br>
	 */
	protected Map<RefDataType, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	protected Map<EntityType, List<Model>> entityGroupMap;

	/**
	 * Set of entity prototypes
	 */
	protected Set<Model> entityPrototypes;

	public Map<RefDataType, Map<String, String>> getRefDataMaps() {
		return refDataMaps;
	}

	public void setRefDataMaps(Map<RefDataType, Map<String, String>> refDataMaps) {
		this.refDataMaps = refDataMaps;
	}

	public Map<EntityType, List<Model>> getEntityGroupMap() {
		return entityGroupMap;
	}

	public void setEntityGroupMap(Map<EntityType, List<Model>> entityMap) {
		this.entityGroupMap = entityMap;
	}

	public Set<Model> getEntityPrototypes() {
		return entityPrototypes;
	}

	public void setEntityPrototypes(Set<Model> entityPrototypes) {
		this.entityPrototypes = entityPrototypes;
	}

}
