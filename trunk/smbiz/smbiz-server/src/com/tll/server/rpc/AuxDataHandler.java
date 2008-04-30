/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.server.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tll.client.data.AuxDataPayload;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.RequestContext;
import com.tll.service.entity.IEntityService;
import com.tll.util.EnumUtil;
import com.tll.util.StringUtil;

/**
 * AuxDataHandler
 * @author jpk
 */
public abstract class AuxDataHandler {

	/**
	 * Provides auxiliary data.
	 * @param requestContext
	 * @param auxDataRequest
	 * @param payload
	 */
	public static void getAuxData(final RequestContext requestContext, final AuxDataRequest auxDataRequest,
			final AuxDataPayload payload) {

		Map<String, Map<String, String>> enumsMap = null;
		Map<String, Map<String, String>> appRefDataMap = null;
		Map<String, List<Model>> entityMap = null;

		for(final Iterator<?> itr = auxDataRequest.requestIterator(); itr.hasNext();) {
			String key;
			final String request = (String) itr.next();

			// enums
			if(request.startsWith(AuxDataRequest.enumTypePrefix)) {
				key = request.substring(AuxDataRequest.enumTypePrefix.length());
				final Class<? extends Enum<?>> enumClass = EnumUtil.enumClassFromString(key);
				if(enumClass == null) {
					payload.getStatus().addMsg("Unable to find enum class: " + key, MsgLevel.ERROR);
				}
				else {
					final Map<String, String> enumMap = EnumUtil.toMap(enumClass);
					if(enumsMap == null) {
						enumsMap = new HashMap<String, Map<String, String>>();
					}
					enumsMap.put(key, enumMap);
				}
			}

			// app ref data
			else if(request.startsWith(AuxDataRequest.refDataTypePrefix)) {
				key = request.substring(AuxDataRequest.refDataTypePrefix.length());
				final Map<String, String> map = requestContext.getAppRefData().getRefData(key);
				if(map == null) {
					payload.getStatus().addMsg("Unable to find app ref data: " + key, MsgLevel.ERROR);
				}
				else {
					if(appRefDataMap == null) {
						appRefDataMap = new HashMap<String, Map<String, String>>();
					}
					appRefDataMap.put(key, map);
				}
			}

			// entity collection
			else if(request.startsWith(AuxDataRequest.entityTypePrefix)) {
				final String entityTypeStr = request.substring(AuxDataRequest.entityTypePrefix.length());
				final Class<? extends IEntity> entityClass = EntityUtil.entityClassFromType(entityTypeStr);
				final IEntityService<? extends IEntity> svc =
						requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
				final List<? extends IEntity> list = svc.loadAll();
				if(list == null || list.size() < 1) {
					payload.getStatus().addMsg(
							"Unable to obtain " + StringUtil.formatEnumValue(entityTypeStr) + " entities for aux data.",
							MsgLevel.ERROR);
				}
				else {
					final List<Model> elist = new ArrayList<Model>(list.size());
					for(final IEntity e : list) {
						final Model group = requestContext.getMarshaler().marshalEntity(e, new MarshalOptions(false, false, -1));
						elist.add(group);
					}
					if(entityMap == null) {
						entityMap = new HashMap<String, List<Model>>();
					}
					entityMap.put(entityTypeStr, elist);
				}
			}
		}

		payload.setEnumsMap(enumsMap);
		payload.setRefDataMaps(appRefDataMap);
		payload.setEntityGroupMap(entityMap);

	}
}
