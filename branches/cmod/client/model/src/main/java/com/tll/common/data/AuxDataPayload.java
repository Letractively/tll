/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.common.model.Model;
import com.tll.model.IEntityType;
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
	protected Map<IEntityType, List<Model>> entityGroupMap;

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

	public Map<IEntityType, List<Model>> getEntityGroupMap() {
		return entityGroupMap;
	}

	public void setEntityGroupMap(Map<IEntityType, List<Model>> entityMap) {
		this.entityGroupMap = entityMap;
	}

	public Set<Model> getEntityPrototypes() {
		return entityPrototypes;
	}

	public void setEntityPrototypes(Set<Model> entityPrototypes) {
		this.entityPrototypes = entityPrototypes;
	}

}
