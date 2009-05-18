/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.ModelRequest;
import com.tll.common.data.Payload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;

/**
 * PersistServiceDelegate - Server side handling of core marshaled entity
 * routines.
 * @author jpk
 */
public final class PersistServiceDelegate {

	private static final long serialVersionUID = 5017008307371980402L;

	/**
	 * Publicly available unique token.
	 */
	public static final String KEY = Long.toString(serialVersionUID);

	/**
	 * The cache of implementations.
	 */
	private static final Map<Class<? extends IPersistServiceImpl<? extends IEntity>>, IPersistServiceImpl<? extends IEntity>> map =
		new HashMap<Class<? extends IPersistServiceImpl<? extends IEntity>>, IPersistServiceImpl<? extends IEntity>>();

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
	 * Validates an inbound entity request.
	 * @param request
	 * @param payload Can't be <code>null</code>
	 * @return true/false
	 */
	private boolean validateEntityRequest(final ModelRequest request, final Payload payload) {
		assert payload != null;
		if(request == null) {
			payload.getStatus().addMsg("No model request specified", MsgLevel.ERROR, MsgAttr.STATUS.flag);
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
			payload.getStatus().addMsg("No aux data request specified", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return false;
		}
		return true;
	}

	/**
	 * Resolves the appropriate {@link IPersistServiceImpl} implementation
	 * instance for the given entity type.
	 * @param request the model request
	 * @param status The status object that is filled with the generated erroro
	 *        msg(s)
	 * @return The associated {@link IPersistServiceImpl} impl instance or
	 *         <code>null</code> when unable to resolve in which case, the
	 *         {@link ModelPayload}'s {@link Status} is updated with an error
	 *         message.
	 */
	private IPersistServiceImpl<? extends IEntity> resolveImpl(
			final IModelRelatedRequest request, final Status status) {
		try {
			IPersistServiceImpl<? extends IEntity> svc;
			Class<? extends IPersistServiceImpl<? extends IEntity>> svcType;
			try {
				svcType = resolver.resolve(request);
			}
			catch(final IllegalArgumentException e) {
				throw new SystemError("Can't resolve persist service impl class for request: " + request.descriptor());
			}
			svc = map.get(svcType);
			if(svc == null) {
				try {
					svc = svcType.newInstance();
				}
				catch(final InstantiationException e) {
					throw new SystemError("Unable to instantiate PersistService class for request: " + request.descriptor(), e);
				}
				catch(final IllegalAccessException e) {
					throw new SystemError("Unable to access PersistService class for request: " + request.descriptor(), e);
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
	public ModelPayload load(final EntityLoadRequest request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request, payload.getStatus()).load(context, request, payload);
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
	public ModelPayload persist(final PersistRequest request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request, payload.getStatus()).persist(context, request, payload);
		}
		return payload;
	}

	/**
	 * Purges an entity.
	 * @param request
	 * @return the resultant payload
	 */
	public ModelPayload purge(final PurgeRequest request) {
		final ModelPayload payload = new ModelPayload();
		if(validateEntityRequest(request, payload)) {
			resolveImpl(request, payload.getStatus()).purge(context, request, payload);
		}
		return payload;
	}

	/**
	 * Provides the entity type specific marshal options.
	 * @param request the model related request used to resolve the persist impl
	 *        type
	 * @param status Filled in upon error
	 * @return the applicable options
	 */
	public MarshalOptions getMarshalOptions(IModelRelatedRequest request, Status status) {
		return resolveImpl(request, status).getMarshalOptions(context);
	}

	/**
	 * Translates client-side search criteria to server-side search criteria.
	 * @param request the model related request used to resolve the persist impl
	 *        type
	 * @param search the client side search instance
	 * @return Newly created server side criteria instance
	 * @param status Filled in upon error
	 * @throws IllegalArgumentException
	 * @throws SystemError
	 */
	public ICriteria<? extends IEntity> translate(IModelRelatedRequest request, final ISearch search, Status status)
	throws IllegalArgumentException, SystemError {
		if(search == null) {
			throw new IllegalArgumentException("Null search argument.");
		}
		return resolveImpl(request, status).translate(context, search);
	}
}
