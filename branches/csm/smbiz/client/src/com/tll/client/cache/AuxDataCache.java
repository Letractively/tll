/**
 * The Logic Lab
 * @author jpk Dec 27, 2007
 */
package com.tll.client.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.AuxDataRequest.AuxDataType;
import com.tll.client.model.Model;
import com.tll.model.EntityType;
import com.tll.service.app.RefDataType;

/**
 * AuxDataCache - Caches Aux data on the client.
 * @author jpk
 * @see AuxDataPayload
 * @see AuxDataRequest
 */
public final class AuxDataCache {

	private static AuxDataCache instance;

	public static final AuxDataCache instance() {
		if(instance == null) {
			instance = new AuxDataCache();
		}
		return instance;
	}

	/**
	 * Map of app ref data name/value pairs keyed by the app ref data type.<br>
	 */
	private Map<RefDataType, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	private Map<EntityType, List<Model>> entityMap;

	/**
	 * Cache of entity prototypes
	 */
	private Set<Model> entityPrototypes;

	private AuxDataCache() {
	}

	/**
	 * Eliminates those requests already present in the {@link AuxDataCache}.
	 * @param adr The aux data request to filter.
	 * @return The filtered data request to send to the server or
	 *         <code>null</code> if the filtering yields no needed aux data.
	 */
	public AuxDataRequest filterRequest(AuxDataRequest adr) {
		if(adr == null) return null;
		AuxDataRequest sadr = new AuxDataRequest();

		// ref data
		Iterator<RefDataType> rdi = adr.getRefDataRequests();
		if(rdi != null) {
			while(rdi.hasNext()) {
				RefDataType rdt = rdi.next();
				if(!isCached(AuxDataType.REFDATA, rdt)) {
					sadr.requestAppRefData(rdt);
				}
			}
		}

		// entities
		Iterator<EntityType> ets = adr.getEntityRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				EntityType et = ets.next();
				if(!isCached(AuxDataType.ENTITY, et)) {
					sadr.requestEntityList(et);
				}
			}
		}

		// entity prototypes
		ets = adr.getEntityPrototypeRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				EntityType et = ets.next();
				if(!isCached(AuxDataType.ENTITY_PROTOTYPE, et)) {
					sadr.requestEntityPrototype(et);
				}
			}
		}

		return sadr.size() > 0 ? sadr : null;
	}

	private void cacheRefDataMap(RefDataType refDataType, Map<String, String> map) {
		if(refDataMaps == null) {
			refDataMaps = new HashMap<RefDataType, Map<String, String>>();
		}
		refDataMaps.put(refDataType, map);
	}

	private void cacheEntityList(EntityType entityType, List<Model> list) {
		if(entityMap == null) {
			entityMap = new HashMap<EntityType, List<Model>>();
		}
		entityMap.put(entityType, list);
	}

	private void cacheEntityPrototype(Model prototype) {
		if(entityPrototypes == null) {
			entityPrototypes = new HashSet<Model>();
		}
		entityPrototypes.add(prototype);
	}

	/**
	 * Caches the resultant aux data received from the server.
	 * @param payload The aux data payload
	 */
	public void cache(AuxDataPayload payload) {

		// ref data maps
		Map<RefDataType, Map<String, String>> map = payload.getRefDataMaps();
		if(map != null) {
			for(RefDataType key : map.keySet()) {
				cacheRefDataMap(key, map.get(key));
			}
		}

		// entity lists
		Map<EntityType, List<Model>> egm = payload.getEntityGroupMap();
		if(egm != null) {
			for(EntityType et : egm.keySet()) {
				cacheEntityList(et, egm.get(et));
			}
		}

		// entity prototypes
		Set<Model> eps = payload.getEntityPrototypes();
		if(eps != null) {
			for(Model p : eps) {
				cacheEntityPrototype(p);
			}
		}
	}

	public Map<String, String> getRefDataMap(RefDataType refDataType) {
		return refDataMaps == null ? null : refDataMaps.get(refDataType);
	}

	public List<Model> getEntityList(EntityType entityType) {
		return entityMap == null ? null : entityMap.get(entityType);
	}

	/**
	 * Returns a
	 * <em>distinct<em> prototype {@link Model} instance of the given entity type.
	 * @param entityType The entity type
	 * @return A distinct prototypical {@link Model} instance of the given entity
	 *         type.
	 */
	public Model getEntityPrototype(EntityType entityType) {
		if(entityPrototypes != null) {
			for(Model p : entityPrototypes) {
				if(p.getEntityType() == entityType) {
					return p.copy(true); // IMPT: provide a distinct instance
				}
			}
		}
		return null;
	}

	private static Map<String, String> currencyMap;

	/**
	 * Provides a map of the available currencies.
	 * @return Map of the the system currency ids keyed by descriptive Strings.
	 */
	public Map<String, String> getCurrencyDataMap() {
		if(currencyMap == null) {
			List<Model> currencies = getEntityList(EntityType.CURRENCY);
			if(currencies == null) return null;
			currencyMap = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			for(Model e : currencies) {
				sb.setLength(0);
				sb.append(e.asString("symbol"));
				sb.append(" - ");
				sb.append(e.getName());
				currencyMap.put(sb.toString(), e.getId().toString());
			}
			return currencyMap;
		}
		return currencyMap;
	}

	public boolean isCached(AuxDataType type, Object obj) {
		switch(type) {
			case REFDATA:
				return refDataMaps == null ? false : refDataMaps.containsKey(obj);
			case ENTITY:
				return entityMap == null ? false : entityMap.containsKey(obj);
			case ENTITY_PROTOTYPE: {
				if(entityPrototypes == null) return false;
				assert obj instanceof EntityType;
				for(Model p : entityPrototypes) {
					if(p.getEntityType() == obj) return true;
				}
				return false;
			}
			default:
				return false;
		}
	}

	public void clear() {
		if(refDataMaps != null) refDataMaps.clear();
		if(entityMap != null) entityMap.clear();
		if(entityPrototypes != null) entityPrototypes.clear();
	}

}