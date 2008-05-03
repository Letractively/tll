/**
 * The Logic Lab
 * @author jpk Dec 27, 2007
 */
package com.tll.client.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.AuxDataRequest.AuxDataType;
import com.tll.client.model.Model;
import com.tll.model.EntityType;

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
	 * Map of app ref data name/value pairs keyed by the app ref data terse name.<br>
	 */
	private Map<String, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	private Map<EntityType, List<Model>> entityMap;

	private AuxDataCache() {
	}

	/**
	 * Eliminates those requests already present in the {@link AuxDataCache}.
	 * @param adr The aux data request to filter.
	 * @return The filtered data request to send to the server.
	 */
	public AuxDataRequest filterRequest(AuxDataRequest adr) {
		if(adr == null) return null;
		AuxDataRequest sadr = new AuxDataRequest();

		// ref data
		Iterator<String> rdi = adr.getRefDataRequests();
		if(rdi != null) {
			while(rdi.hasNext()) {
				String terseName = rdi.next();
				if(!isCached(AuxDataType.REFDATA, terseName)) {
					sadr.requestAppRefData(terseName);
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

		return sadr;
	}

	public void cache(AuxDataPayload payload) {

		// ref data maps
		Map<String, Map<String, String>> map = payload.getRefDataMaps();
		if(map != null) {
			for(String key : map.keySet()) {
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
	}

	private void cacheRefDataMap(String appRefDataTerseName, Map<String, String> map) {
		if(refDataMaps == null) {
			refDataMaps = new HashMap<String, Map<String, String>>();
		}
		refDataMaps.put(appRefDataTerseName, map);
	}

	private void cacheEntityList(EntityType entityType, List<Model> list) {
		if(entityMap == null) {
			entityMap = new HashMap<EntityType, List<Model>>();
		}
		entityMap.put(entityType, list);
	}

	public Map<String, String> getRefDataMap(String appRefDataTerseName) {
		return refDataMaps == null ? null : refDataMaps.get(appRefDataTerseName);
	}

	public List<Model> getEntityList(EntityType entityType) {
		return entityMap == null ? null : entityMap.get(entityType);
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

	private boolean isCached(AuxDataType type, Object obj) {
		switch(type) {
			case REFDATA:
				return refDataMaps == null ? false : refDataMaps.containsKey(obj);
			case ENTITY:
				return entityMap == null ? false : entityMap.containsKey(obj);
			default:
				return false;
		}
	}

	public void clear() {
		if(refDataMaps != null) refDataMaps.clear();
		if(entityMap != null) entityMap.clear();
	}

}