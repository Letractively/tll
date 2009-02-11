/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.server.rpc.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.model.IEntity;
import com.tll.refdata.RefDataType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityService;

/**
 * AuxDataHandler
 * @author jpk
 */
public abstract class AuxDataHandler {

	/**
	 * Provides auxiliary data.
	 * @param context
	 * @param auxDataRequest
	 * @param payload
	 */
	public static void getAuxData(IMEntityServiceContext context, final AuxDataRequest auxDataRequest,
			final AuxDataPayload payload) {

		Map<RefDataType, Map<String, String>> appRefDataMap = null;
		Map<IEntityType, List<Model>> entityMap = null;
		Set<Model> entityPrototypes = null;
		
		// app ref data
		Iterator<RefDataType> adritr = auxDataRequest.getRefDataRequests();
		while(adritr != null && adritr.hasNext()) {
			RefDataType rdt = adritr.next();
			final Map<String, String> map = context.getRefData().getRefData(rdt);
			if(map == null) {
				payload.getStatus().addMsg("Unable to find app ref data: " + rdt.getName(), MsgLevel.ERROR);
			}
			else {
				if(appRefDataMap == null) {
					appRefDataMap = new HashMap<RefDataType, Map<String, String>>();
				}
				appRefDataMap.put(rdt, map);
			}
		}

		// entity collection
		Iterator<IEntityType> etitr = auxDataRequest.getEntityRequests();
		while(etitr != null && etitr.hasNext()) {
			IEntityType et = etitr.next();
			final Class<? extends IEntity> entityClass = EntityTypeUtil.getEntityClass(et);
			final IEntityService<? extends IEntity> svc =
					context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final List<? extends IEntity> list = svc.loadAll();
			if(list == null || list.size() < 1) {
				payload.getStatus().addMsg("Unable to obtain " + et.getPresentationName() + " entities for aux data.",
						MsgLevel.ERROR);
			}
			else {
				final List<Model> elist = new ArrayList<Model>(list.size());
				for(final IEntity e : list) {
					final Model group =
							context.getMarshaler().marshalEntity(e, MarshalOptions.NON_RELATIONAL);
					elist.add(group);
				}
				if(entityMap == null) {
					entityMap = new HashMap<IEntityType, List<Model>>();
				}
				entityMap.put(et, elist);
			}
		}

		// entity prototypes
		etitr = auxDataRequest.getEntityPrototypeRequests();
		while(etitr != null && etitr.hasNext()) {
			IEntityType et = etitr.next();
			final IEntity e =
					context.getEntityFactory().createEntity(EntityTypeUtil.getEntityClass(et), false);
			final Model model = context.getMarshaler().marshalEntity(e, MarshalOptions.NO_REFERENCES);
			if(entityPrototypes == null) {
				entityPrototypes = new HashSet<Model>();
			}
			entityPrototypes.add(model);
		}

		payload.setRefDataMaps(appRefDataMap);
		payload.setEntityGroupMap(entityMap);
		payload.setEntityPrototypes(entityPrototypes);
	}
}
