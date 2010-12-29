/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.AbstractModelRequest;
import com.tll.common.data.Payload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.msg.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;
import com.tll.service.entity.IEntityService;

/**
 * PersistServiceDelegate - Server side handling of model data persist
 * operations.
 * @author jpk
 */
public final class PersistServiceDelegate {

	/**
	 * Publicly available unique token.
	 */
	public static final String KEY = Long.toString(5017008307371980402L);

	/**
	 * The cache of implementations.
	 */
	private static final Map<Class<? extends IPersistServiceImpl>, IPersistServiceImpl> map =
		new HashMap<Class<? extends IPersistServiceImpl>, IPersistServiceImpl>();

	/**
	 * Attempts to resolve marshaling options from the persist svc delegate
	 * falling back on the provided defaults.
	 * <p>
	 * NOTE: we provide a <code>null</code> status instance since the error is
	 * spurious since we have a fallback marshal options instance.
	 * @param context the persist context
	 * @param entityType
	 * @param fallback Used when no persist svc is resolved from the given entity
	 *        type
	 * @return Never-<code>null</code> instance.
	 */
	private static MarshalOptions getMarshalOptions(PersistContext context, IEntityType entityType,
			MarshalOptions fallback) {
		try {
			return context.getMarshalOptionsResolver().resolve(entityType);
		}
		catch(final RuntimeException e) {
			return fallback;
		}
	}

	/**
	 * Provides model data.
	 * @param context
	 * @param auxDataRequest
	 * @param payload
	 */
	@SuppressWarnings("unchecked")
	public static void getModelData(PersistContext context, final ModelDataRequest auxDataRequest,
			final ModelDataPayload payload) {

		//Map<RefDataType, Map<String, String>> appRefDataMap = null;
		Map<IEntityType, List<Model>> entityMap = null;
		Set<Model> entityPrototypes = null;

		// app ref data
		/*
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
		*/

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
					final MarshalOptions mo = getMarshalOptions(context, et, MarshalOptions.NO_REFERENCES);
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
						(Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et), null);
			final MarshalOptions mo = getMarshalOptions(context, et, MarshalOptions.NO_REFERENCES);
			final Model model = context.getMarshaler().marshalEntity(e, mo);
			if(entityPrototypes == null) {
				entityPrototypes = new HashSet<Model>();
			}
			entityPrototypes.add(model);
		}

		//payload.setRefDataMaps(appRefDataMap);
		payload.setEntityMap(entityMap);
		payload.setEntityPrototypes(entityPrototypes);
	}
	
	private final PersistContext context;

	private final IPersistServiceImplResolver resolver;

	/**
	 * Constructor
	 * @param context
	 * @param resolver
	 */
	@Inject
	public PersistServiceDelegate(PersistContext context, IPersistServiceImplResolver resolver) {
		super();
		this.context = context;
		this.resolver = resolver;
	}

	/**
	 * Loads model data.
	 * @param request
	 * @return the resultant payload
	 */
	public ModelPayload load(final LoadRequest<? extends ISearch> request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request, payload.getStatus()).load(request.getSearch(), payload);
		}
		// load any requested auxiliary
		if(request.getModelDataRequest() != null) {
			getModelData(context, request.getModelDataRequest(), payload);
		}
		return payload;
	}

	/**
	 * Persists (adds or updates) model data.
	 * @param request
	 * @return the resultant payload
	 */
	public ModelPayload persist(final PersistRequest request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request, payload.getStatus()).persist(request.getModel(), payload);
		}
		return payload;
	}

	/**
	 * Purges model data.
	 * @param request
	 * @return the resultant payload
	 */
	public ModelPayload purge(final PurgeRequest request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			if(request.getEntityRef() != null) {
				resolveImpl(request, payload.getStatus()).purge(request.getEntityRef(), payload);
			}
			else {
				resolveImpl(request, payload.getStatus()).purge(request.getModel(), payload);
			}
		}
		return payload;
	}

	/**
	 * Loads auxiliary data.
	 * @param request
	 * @return the resultant payload
	 */
	public ModelDataPayload loadModelData(final ModelDataRequest request) {
		final ModelDataPayload payload = new ModelDataPayload();
		if(validateModelDataRequest(request, payload)) {
			try {
				getModelData(context, request, payload);
			}
			catch(final RuntimeException se) {
				RpcServlet.exceptionToStatus(se, payload.getStatus());
				context.getExceptionHandler().handleException(se);
			}
		}
		return payload;
	}

	/**
	 * Resolves the appropriate {@link IPersistServiceImpl} implementation
	 * instance for the given model related request.
	 * @param request the model request
	 * @param status The status object that is filled with the generated erroro
	 *        msg(s)
	 * @return The associated {@link IPersistServiceImpl} impl instance or
	 *         <code>null</code> when unable to resolve in which case, the
	 *         {@link ModelPayload}'s {@link Status} is updated with an error
	 *         message.
	 */
	private IPersistServiceImpl resolveImpl(final IModelRelatedRequest request, final Status status) {
		try {
			IPersistServiceImpl svc;
			Class<? extends IPersistServiceImpl> svcType;
			try {
				svcType = resolver.resolve(request);
			}
			catch(final IllegalArgumentException e) {
				throw new RuntimeException("Can't resolve persist service impl class for request: " + request.descriptor());
			}
			svc = map.get(svcType);
			if(svc == null) {
				Constructor<?> c;
				try {
					c = svcType.getConstructor(PersistContext.class);
					svc = (IPersistServiceImpl) c.newInstance(context);
				}
				catch(final Throwable e) {
					throw new RuntimeException("Unable to instantiate Persist implementation instance: " + request.descriptor(),
							e);
				}
				map.put(svcType, svc);
			}
			return svc;
		}
		catch(final RuntimeException se) {
			if(status != null) {
				RpcServlet.exceptionToStatus(se, status);
			}
			context.getExceptionHandler().handleException(se);
			throw se;
		}
	}

	/**
	 * Validates an inbound entity request.
	 * @param request
	 * @param payload Can't be <code>null</code>
	 * @return true/false
	 */
	private boolean validateEntityRequest(final AbstractModelRequest request, final Payload payload) {
		if(request == null) {
			payload.getStatus().addMsg("No model request specified", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return false;
		}
		return true;
	}

	/**
	 * Validates an inbound model data request.
	 * @param request
	 * @param payload Can't be <code>null</code>
	 * @return true/false
	 */
	private boolean validateModelDataRequest(final ModelDataRequest request, final Payload payload) {
		if(request == null) {
			payload.getStatus().addMsg("No aux data request specified", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return false;
		}
		return true;
	}
}
