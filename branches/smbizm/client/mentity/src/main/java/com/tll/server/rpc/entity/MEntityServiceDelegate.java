/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.data.Payload;
import com.tll.common.data.Status;
import com.tll.common.model.IEntityType;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;

/**
 * MEntityServiceDelegate - Server side handling of core marshaled entity
 * routines.
 * @author jpk
 */
public final class MEntityServiceDelegate {

	private static final long serialVersionUID = 5017008307371980402L;

	/**
	 * Publicly available unique token.
	 */
	public static final String KEY = Long.toString(serialVersionUID);

	/**
	 * The cache of implementations.
	 */
	private static final Map<Class<? extends IMEntityServiceImpl<? extends IEntity>>, IMEntityServiceImpl<? extends IEntity>> map =
		new HashMap<Class<? extends IMEntityServiceImpl<? extends IEntity>>, IMEntityServiceImpl<? extends IEntity>>();

	private final MEntityContext context;

	private final IMEntityServiceImplResolver resolver;

	/**
	 * Constructor
	 * @param context
	 * @param resolver
	 */
	public MEntityServiceDelegate(MEntityContext context, IMEntityServiceImplResolver resolver) {
		super();
		this.context = context;
		this.resolver = resolver;
	}

	/**
	 * Validates an inbound entity request.
	 * @param request
	 * @param payload Can't be <code>null</code>
	 * @return true/false
	 */
	private boolean validateEntityRequest(final EntityRequest request, final Payload payload) {
		assert payload != null;
		if(request == null) {
			payload.getStatus().addMsg("No entity request specified", MsgLevel.ERROR);
			return false;
		}
		if(request.getEntityType() == null) {
			payload.getStatus().addMsg("No entity type specified", MsgLevel.ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Validates an inbound aux data request.
	 * @param request
	 * @param payload Can't be <code>null</code>
	 * @return true/false
	 */
	private boolean validateAuxDataRequest(final AuxDataRequest request, final Payload payload) {
		if(request == null) {
			payload.getStatus().addMsg("No aux data request specified", MsgLevel.ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Resolves the appropriate {@link IMEntityServiceImpl} implementation
	 * instance for the given entity type.
	 * @param entityType the entity type
	 * @param status The status object that is filled with the generated erroro
	 *        msg(s)
	 * @return The associated {@link IMEntityServiceImpl} impl instance or
	 *         <code>null</code> when unable to resolve in which case, the
	 *         {@link EntityPayload}'s {@link Status} is updated with an error
	 *         message.
	 */
	private IMEntityServiceImpl<? extends IEntity> resolveImpl(
			final IEntityType entityType, final Status status) {
		try {
			final Class<? extends IEntity> entityClass = EntityTypeUtil.getEntityClass(entityType);
			IMEntityServiceImpl<? extends IEntity> svc;
			Class<? extends IMEntityServiceImpl<? extends IEntity>> svcType;
			try {
				svcType = resolver.resolveMEntityServiceImpl(entityClass);
			}
			catch(final IllegalArgumentException e) {
				throw new SystemError("Can't resolve mEntity service impl class for entity: " + entityClass.getName());
			}
			svc = map.get(svcType);
			if(svc == null) {
				try {
					svc = svcType.newInstance();
				}
				catch(final InstantiationException e) {
					throw new SystemError("Unable to instantiate MEntityService class for entity type: " + entityClass, e);
				}
				catch(final IllegalAccessException e) {
					throw new SystemError("Unable to access MEntityService class for entity type: " + entityClass, e);
				}
				map.put(svcType, svc);
			}
			return svc;
		}
		catch(final SystemError se) {
			if(status != null) {
				RpcServlet.exceptionToStatus(se, status);
			}
			context.getExceptionHandler().handleException(se);
			throw se;
		}
	}

	/**
	 * Handles an aux data request.
	 * @param request
	 * @return the resultant payload
	 */
	public AuxDataPayload handleAuxDataRequest(final AuxDataRequest request) {
		final AuxDataPayload payload = new AuxDataPayload();
		if(validateAuxDataRequest(request, payload)) {
			try {
				AuxDataHandler.getAuxData(context, this, request, payload);
			}
			catch(final SystemError se) {
				RpcServlet.exceptionToStatus(se, payload.getStatus());
				context.getExceptionHandler().handleException(se);
			}
			catch(final RuntimeException re) {
				RpcServlet.exceptionToStatus(re, payload.getStatus());
				context.getExceptionHandler().handleException(re);
				throw re;
			}
		}
		return payload;
	}

	/**
	 * Loads an entity
	 * @param request
	 * @return the resultant payload
	 */
	public EntityPayload load(final EntityLoadRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request.getEntityType(), payload.getStatus()).load(context, request, payload);
		}
		// load any requested auxiliary
		if(request.getAuxDataRequest() != null) {
			AuxDataHandler.getAuxData(context, this, request.getAuxDataRequest(), payload);
		}
		return payload;
	}

	/**
	 * Persists an entity.
	 * @param request
	 * @return the resultant payload
	 */
	public EntityPayload persist(final EntityPersistRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request.getEntityType(), payload.getStatus()).persist(context, request, payload);
		}
		return payload;
	}

	/**
	 * Purges an entity.
	 * @param request
	 * @return the resultant payload
	 */
	public EntityPayload purge(final EntityPurgeRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request.getEntityType(), payload.getStatus()).purge(context, request, payload);
		}
		return payload;
	}

	/**
	 * Provides the entity type specific marshal options.
	 * @param entityType
	 * @return the applicable options
	 */
	public MarshalOptions getMarshalOptions(IEntityType entityType) {
		return resolveImpl(entityType, null).getMarshalOptions(context);
	}

	/**
	 * Translates client-side search criteria to server-side search criteria.
	 * @param search the client side search instance
	 * @return Newly created server side criteria instance
	 * @throws IllegalArgumentException
	 * @throws SystemError
	 */
	public ICriteria<? extends IEntity> translate(final ISearch search) throws IllegalArgumentException, SystemError {
		if(search == null) {
			throw new IllegalArgumentException("Null search argument.");
		}
		return resolveImpl(search.getEntityType(), null).translate(context, search);
	}
}
