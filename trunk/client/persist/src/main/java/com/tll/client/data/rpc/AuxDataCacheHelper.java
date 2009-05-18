/**
 * The Logic Lab
 * @author jpk
 * @since Apr 25, 2009
 */
package com.tll.client.data.rpc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.client.cache.AuxDataCache;
import com.tll.common.cache.AuxDataType;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.refdata.RefDataType;


/**
 * AuxDataCacheHelper
 * @author jpk
 */
public abstract class AuxDataCacheHelper {
	/**
	 * Eliminates those requests already present in the {@link AuxDataCache}.
	 * @param adr The aux data request to filter.
	 * @return The filtered data request to send to the server or
	 *         <code>null</code> if the filtering yields no needed aux data.
	 */
	public static AuxDataRequest filterRequest(AuxDataRequest adr) {
		if(adr == null) return null;
		final AuxDataCache adc = AuxDataCache.get();
		final AuxDataRequest sadr = new AuxDataRequest();

		// ref data
		final Iterator<RefDataType> rdi = adr.getRefDataRequests();
		if(rdi != null) {
			while(rdi.hasNext()) {
				final RefDataType rdt = rdi.next();
				if(!adc.isCached(AuxDataType.REFDATA, rdt)) {
					sadr.requestAppRefData(rdt);
				}
			}
		}

		// entities
		Iterator<IEntityType> ets = adr.getEntityRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				final IEntityType et = ets.next();
				if(!adc.isCached(AuxDataType.ENTITY, et)) {
					sadr.requestEntityList(et);
				}
			}
		}

		// entity prototypes
		ets = adr.getEntityPrototypeRequests();
		if(ets != null) {
			while(ets.hasNext()) {
				final IEntityType et = ets.next();
				if(!adc.isCached(AuxDataType.ENTITY_PROTOTYPE, et)) {
					sadr.requestEntityPrototype(et);
				}
			}
		}

		return sadr.size() > 0 ? sadr : null;
	}

	/**
	 * Caches the resultant aux data received from the server.
	 * @param payload The aux data payload
	 */
	public static void cache(AuxDataPayload payload) {

		final AuxDataCache adc = AuxDataCache.get();

		// ref data maps
		final Map<RefDataType, Map<String, String>> map = payload.getRefDataMaps();
		if(map != null) {
			for(final Map.Entry<RefDataType, Map<String, String>> e : map.entrySet()) {
				adc.cacheRefDataMap(e.getKey(), e.getValue());
			}
		}

		// entity lists
		final Map<IEntityType, List<Model>> egm = payload.getEntityGroupMap();
		if(egm != null) {
			for(final Map.Entry<IEntityType, List<Model>> e : egm.entrySet()) {
				adc.cacheEntityList(e.getKey(), e.getValue());
			}
		}

		// entity prototypes
		final Set<Model> eps = payload.getEntityPrototypes();
		if(eps != null) {
			for(final Model p : eps) {
				adc.cacheEntityPrototype(p);
			}
		}
	}
}
