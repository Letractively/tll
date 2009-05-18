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

import com.tll.SystemError;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
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
	 * Attempts to resolve marshaling options from the persist svc delegate
	 * falling back on the provided defaults.
	 * <p>
	 * NOTE: we provide a <code>null</code> status instance since the error is
	 * spurious since we have a fallback marshal options instance.
	 * @param delegate
	 * @param entityType
	 * @param fallback Used when no persist svc is resolved from the given entity
	 *        type
	 * @return Never-<code>null</code> instance.
	 */
	private static MarshalOptions getMarshalOptions(PersistServiceDelegate delegate, IEntityType entityType,
			MarshalOptions fallback) {
		try {
			return delegate.getMarshalOptions(new EntityTypeRequest(entityType, "Marshal Options request"), null);
		}
		catch(final SystemError e) {
			return fallback;
		}
	}

	/**
	 * Provides auxiliary data.
	 * @param context
	 * @param delegate
	 * @param auxDataRequest
	 * @param payload
	 */
	@SuppressWarnings("unchecked")
	public static void getAuxData(PersistContext context, PersistServiceDelegate delegate,
			final AuxDataRequest auxDataRequest,
			final AuxDataPayload payload) {

		Map<RefDataType, Map<String, String>> appRefDataMap = null;
		Map<IEntityType, List<Model>> entityMap = null;
		Set<Model> entityPrototypes = null;

		// app ref data
		final Iterator<RefDataType> adritr = auxDataRequest.getRefDataRequests();
		while(adritr != null && adritr.hasNext()) {
			final RefDataType rdt = adritr.next();
			final Map<String, String> map = context.getRefData().getRefData(rdt);
			if(map == null) {
				payload.getStatus()
				.addMsg("Unable to find app ref data: " + rdt.getName(), MsgLevel.ERROR, MsgAttr.STATUS.flag);
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
		if(etitr != null) {
			while(etitr.hasNext()) {
				final IEntityType et = etitr.next();
				final Class<? extends IEntity> entityClass =
					(Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
				final IEntityService<? extends IEntity> svc =
					context.getEntityServiceFactory().instanceByEntityType(entityClass);
				final List<? extends IEntity> list = svc.loadAll();
				if(list == null || list.size() < 1) {
					payload.getStatus().addMsg("Unable to obtain " + et.descriptor() + " entities for aux data.",
							MsgLevel.ERROR, MsgAttr.STATUS.flag);
				}
				else {
					final MarshalOptions mo = getMarshalOptions(delegate, et, MarshalOptions.NO_REFERENCES);
					final List<Model> elist = new ArrayList<Model>(list.size());
					for(final IEntity e : list) {
						final Model group = context.getMarshaler().marshalEntity(e, mo);
						elist.add(group);
					}
					if(entityMap == null) {
						entityMap = new HashMap<IEntityType, List<Model>>();
					}
					entityMap.put(et, elist);
				}
			}
		}

		// entity prototypes
		etitr = auxDataRequest.getEntityPrototypeRequests();
		while(etitr != null && etitr.hasNext()) {
			final IEntityType et = etitr.next();
			final IEntity e =
				context.getEntityAssembler().assembleEntity(
						(Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et), null, false);
			final MarshalOptions mo = getMarshalOptions(delegate, et, MarshalOptions.NO_REFERENCES);
			final Model model = context.getMarshaler().marshalEntity(e, mo);
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
