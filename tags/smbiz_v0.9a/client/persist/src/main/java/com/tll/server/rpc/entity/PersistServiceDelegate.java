/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.AbstractModelRequest;
import com.tll.common.data.Payload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.server.rpc.RpcServlet;

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
		if(request.getAuxDataRequest() != null) {
			AuxDataHandler.getAuxData(context, request.getAuxDataRequest(), payload);
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
	public AuxDataPayload loadAuxData(final AuxDataRequest request) {
		final AuxDataPayload payload = new AuxDataPayload();
		if(validateAuxDataRequest(request, payload)) {
			try {
				AuxDataHandler.getAuxData(context, request, payload);
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
}
