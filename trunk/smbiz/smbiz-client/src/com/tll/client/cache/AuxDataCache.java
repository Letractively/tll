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
import com.tll.client.model.IEntityType;
import com.tll.client.model.Model;

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
	 * Eliminates those requests already present in the {@link AuxDataCache}.
	 * @param adr The aux data request to filter.
	 * @return The filtered data request to send to the server.
	 */
	public AuxDataRequest filterRequest(AuxDataRequest adr) {
		if(adr == null) return null;
		AuxDataRequest sadr = new AuxDataRequest();
		for(Iterator<String> itr = adr.requestIterator(); itr.hasNext();) {
			String request = itr.next();

			// enums
			if(request.startsWith(AuxDataRequest.enumTypePrefix)) {
				String key = request.substring(AuxDataRequest.enumTypePrefix.length());
				if(!isCached(key, AuxDataRequest.TYPE_ENUM)) {
					sadr.requestEnum(key);
				}
			}
			else if(request.startsWith(AuxDataRequest.refDataTypePrefix)) {
				String key = request.substring(AuxDataRequest.refDataTypePrefix.length());
				if(!isCached(key, AuxDataRequest.TYPE_REF_DATA)) {
					sadr.requestAppRefData(key);
				}
			}
			else if(request.startsWith(AuxDataRequest.entityTypePrefix)) {
				String key = request.substring(AuxDataRequest.entityTypePrefix.length());
				if(!isCached(key, AuxDataRequest.TYPE_ENTITY)) {
					sadr.requestEntityList(key);
				}
			}
		}
		return sadr;
	}

	/**
	 * Map of string-wise enum maps keyed by the enum class name.<br>
	 * Enum map format: key: enum name (presentation worthy), val: enum value
	 * (Enum.name()).
	 */
	private Map<String, Map<String, String>> enumsMap;

	/**
	 * Map of app ref data name/value pairs keyed by the app ref data terse name.<br>
	 */
	private Map<String, Map<String, String>> refDataMaps;

	/**
	 * Map of entity lists keyed by the entity class name.
	 */
	private Map<String, List<Model>> entityMap;

	private AuxDataCache() {
	}

	public void cache(AuxDataPayload payload) {
		Map<String, Map<String, String>> map;

		// enum maps
		map = payload.getEnumsMap();
		if(map != null) {
			for(String key : map.keySet()) {
				cacheEnumMap(key, map.get(key));
			}
		}

		// ref data maps
		map = payload.getRefDataMaps();
		if(map != null) {
			for(String key : map.keySet()) {
				cacheRefDataMap(key, map.get(key));
			}
		}

		// entity lists
		Map<String, List<Model>> egm = payload.getEntityGroupMap();
		if(egm != null) {
			for(String key : egm.keySet()) {
				cacheEntityList(key, egm.get(key));
			}
		}
	}

	private void cacheEnumMap(String enumClassName, Map<String, String> map) {
		if(enumClassName == null || map == null || map.size() < 1) return;
		if(enumsMap == null) enumsMap = new HashMap<String, Map<String, String>>();
		enumsMap.put(enumClassName, map);
	}

	private void cacheRefDataMap(String appRefDataTerseName, Map<String, String> map) {
		if(appRefDataTerseName == null || map == null || map.size() < 1) return;
		if(refDataMaps == null) refDataMaps = new HashMap<String, Map<String, String>>();
		refDataMaps.put(appRefDataTerseName, map);
	}

	private void cacheEntityList(String entityClassName, List<Model> list) {
		if(entityClassName == null || list == null || list.size() < 1) return;
		if(entityMap == null) entityMap = new HashMap<String, List<Model>>();
		entityMap.put(entityClassName, list);
	}

	public Map<String, String> getEnumMap(String enumClassName) {
		return enumsMap == null ? null : enumsMap.get(enumClassName);
	}

	public Map<String, String> getRefDataMap(String appRefDataTerseName) {
		return refDataMaps == null ? null : refDataMaps.get(appRefDataTerseName);
	}

	public List<Model> getEntityList(String entityTypeName) {
		return entityMap == null ? null : entityMap.get(entityTypeName);
	}

	private static Map<String, String> currencyMap;

	/**
	 * Provides a map of the available currencies.
	 * @return Map of the the system currency ids keyed by descriptive Strings.
	 */
	public Map<String, String> getCurrencyDataMap() {
		if(currencyMap == null) {
			List<Model> currencies = getEntityList(IEntityType.CURRENCY);
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

	public boolean isCached(String key, int type) {
		switch(type) {
			case AuxDataRequest.TYPE_ENUM:
				return enumsMap == null ? false : enumsMap.containsKey(key);
			case AuxDataRequest.TYPE_REF_DATA:
				return refDataMaps == null ? false : refDataMaps.containsKey(key);
			case AuxDataRequest.TYPE_ENTITY:
				return entityMap == null ? false : entityMap.containsKey(key);
			default:
				return false;
		}
	}

	public void clear() {
		if(enumsMap != null) enumsMap.clear();
		if(refDataMaps != null) refDataMaps.clear();
		if(enumsMap != null) enumsMap.clear();
	}

}