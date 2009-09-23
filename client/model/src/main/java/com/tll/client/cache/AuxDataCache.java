/**
 * The Logic Lab
 * @author jpk Dec 27, 2007
 */
package com.tll.client.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.common.cache.AuxDataType;
import com.tll.common.model.CopyCriteria;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.refdata.RefDataType;

/**
 * AuxDataCache - Caches Aux data on the client.
 * @author jpk
 */
public final class AuxDataCache {

	private static AuxDataCache instance;

	public static final AuxDataCache get() {
		if(instance == null) {
			instance = new AuxDataCache();
		}
		return instance;
	}

	/**
	 * Map of app ref data name/value pairs keyed by the app ref data type.<br>
	 * Each ref data map ascribes to the format where the map keys are the
	 * datastore bound values and the map values are the presentation worthy
	 * tokens.
	 */
	private Map<RefDataType, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	private Map<IEntityType, List<Model>> entityMap;

	/**
	 * Cache of entity prototypes
	 */
	private Set<Model> entityPrototypes;

	/**
	 * Constructor
	 */
	private AuxDataCache() {
	}

	/**
	 * Caches ref data.
	 * @param refDataType
	 * @param map
	 */
	public void cacheRefDataMap(RefDataType refDataType, Map<String, String> map) {
		if(refDataMaps == null) {
			refDataMaps = new HashMap<RefDataType, Map<String, String>>();
		}
		refDataMaps.put(refDataType, map);
	}

	/**
	 * Caches a list of like entities as client side model instances.
	 * @param entityType
	 * @param list
	 */
	public void cacheEntityList(IEntityType entityType, List<Model> list) {
		if(entityMap == null) {
			entityMap = new HashMap<IEntityType, List<Model>>();
		}
		entityMap.put(entityType, list);
	}

	/**
	 * Caches a single empty prototype model entity.
	 * @param prototype
	 */
	public void cacheEntityPrototype(Model prototype) {
		if(entityPrototypes == null) {
			entityPrototypes = new HashSet<Model>();
		}
		entityPrototypes.add(prototype);
	}

	/**
	 * @param refDataType
	 * @return the cached refdata map or <code>null</code> if it isn't cached.
	 */
	public Map<String, String> getRefDataMap(RefDataType refDataType) {
		return refDataMaps == null ? null : refDataMaps.get(refDataType);
	}

	/**
	 * @param entityType
	 * @return the cached entity list or <code>null</code> if it isn't cached.
	 */
	public List<Model> getEntityList(IEntityType entityType) {
		return entityMap == null ? null : entityMap.get(entityType);
	}

	/**
	 * Returns a
	 * <em>distinct<em> prototype {@link Model} instance of the given entity type.
	 * @param entityType The entity type
	 * @return A distinct prototypical {@link Model} instance of the given entity
	 *         type or <code>null</code> if no prototype model of the given entity
	 *         type is cached.
	 */
	public Model getEntityPrototype(IEntityType entityType) {
		if(entityPrototypes != null && entityType != null) {
			for(final Model p : entityPrototypes) {
				if(p.getEntityType().equals(entityType)) {
					return p.copy(CopyCriteria.COPY_ALL); // IMPT: provide a distinct instance
				}
			}
		}
		return null;
	}

	/**
	 * Is a particular item of the given aux data type currently cached?
	 * @param type
	 * @param obj
	 * @return true/false
	 */
	public boolean isCached(AuxDataType type, Object obj) {
		if(obj == null) return false;
		switch(type) {
		case REFDATA:
			return refDataMaps == null ? false : refDataMaps.containsKey(obj);
		case ENTITY:
			return entityMap == null ? false : entityMap.containsKey(obj);
		case ENTITY_PROTOTYPE: {
			if(entityPrototypes != null) {
				final IEntityType et = (IEntityType) obj;
				for(final Model p : entityPrototypes) {
					if(et.equals(p.getEntityType())) return true;
				}
			}
			return false;
		}
		default:
			return false;
		}
	}

	/**
	 * Clears out all cached elements of all types.
	 */
	public void clear() {
		if(refDataMaps != null) refDataMaps.clear();
		if(entityMap != null) entityMap.clear();
		if(entityPrototypes != null) entityPrototypes.clear();
	}

}