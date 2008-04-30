/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.client.data;

import java.util.List;
import java.util.Map;

import com.tll.client.model.Model;

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
	 * Map of string-wise enum maps keyed by the enum class name.<br>
	 * Enum map format: key: enum name (presentation worthy), val: enum value
	 * (Enum.name()).
	 */
	protected Map<String, Map<String, String>> enumsMap;

	/**
	 * Map of app ref data name/value pairs keyed by the app ref data terse name.<br>
	 */
	protected Map<String, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	protected Map<String, List<Model>> entityGroupMap;

	public Map<String, Map<String, String>> getEnumsMap() {
		return enumsMap;
	}

	public void setEnumsMap(Map<String, Map<String, String>> enumsMap) {
		this.enumsMap = enumsMap;
	}

	public Map<String, Map<String, String>> getRefDataMaps() {
		return refDataMaps;
	}

	public void setRefDataMaps(Map<String, Map<String, String>> refDataMaps) {
		this.refDataMaps = refDataMaps;
	}

	public Map<String, List<Model>> getEntityGroupMap() {
		return entityGroupMap;
	}

	public void setEntityGroupMap(Map<String, List<Model>> entityMap) {
		this.entityGroupMap = entityMap;
	}

	public Map<String, String> getEnumMap(String enumClassName) {
		return enumsMap == null ? null : enumsMap.get(enumClassName);
	}

	public Map<String, String> getRefDataMap(String terseName) {
		return refDataMaps == null ? null : refDataMaps.get(terseName);
	}

	public List<Model> getEntityList(String entityTypeName) {
		return entityGroupMap == null ? null : entityGroupMap.get(entityTypeName);
	}
}
