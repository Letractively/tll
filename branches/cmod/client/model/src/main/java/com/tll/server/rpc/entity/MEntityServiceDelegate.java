/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.SystemError;
import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPrototypeRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.data.Payload;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.Status;
import com.tll.common.model.IEntityType;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.listing.IMarshalingListHandler;

/**
 * MEntityServiceDelegate - Front line rpc servlet that routes entity related
 * rpc requests.
 * @author jpk
 */
public class MEntityServiceDelegate extends RpcServlet implements
		IMEntityService<IEntity, ISearch> {

	private static final long serialVersionUID = 5017008307371980402L;
	
	private MEntityContext getMEntityContext() {
		return (MEntityContext) getServletContext().getAttribute(MEntityContext.SERVLET_CONTEXT_KEY);
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
	 * Resolves the appropriate {@link IMEntityServiceImpl} class for the given
	 * entity type.
	 * @param entityType the entity type
	 * @param payload The {@link EntityPayload}
	 * @return The associated {@link IMEntityServiceImpl} impl instance or
	 *         <code>null</code> when unable to resolve in which case, the
	 *         {@link EntityPayload}'s {@link Status} is updated with an error
	 *         message.
	 */
	private IMEntityServiceImpl<? extends IEntity, ? extends ISearch> resolveEntityServiceImpl(
			final IEntityType entityType, final EntityPayload payload) {
		try {
			return MEntityServiceImplFactory.instance(EntityTypeUtil.getEntityClass(entityType), getMEntityContext()
					.getServiceResolver());
		}
		catch(final SystemError se) {
			getMEntityContext().getExceptionHandler().handleException(payload.getStatus(), se, se.getMessage(), true);
			// return null;
			throw se;
		}
	}

	@Override
	public AuxDataPayload handleAuxDataRequest(final AuxDataRequest request) {
		final AuxDataPayload payload = new AuxDataPayload();
		if(validateAuxDataRequest(request, payload)) {
			try {
				AuxDataHandler.getAuxData(getMEntityContext(), request, payload);
			}
			catch(final SystemError se) {
				getMEntityContext().getExceptionHandler().handleException(payload.getStatus(), se, null, true);
			}
			catch(final RuntimeException re) {
				getMEntityContext().getExceptionHandler().handleException(payload.getStatus(), re, null, true);
				throw re;
			}
		}
		return payload;
	}

	@Override
	public EntityPayload prototype(final EntityPrototypeRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl =
					resolveEntityServiceImpl(request.getEntityType(), payload);
			if(impl != null) {
				impl.prototype(getMEntityContext(), request, payload);
			}
		}
		return payload;
	}

	@Override
	public EntityPayload load(final EntityLoadRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl =
					resolveEntityServiceImpl(request.getEntityType(), payload);
			if(impl != null) {
				impl.load(getMEntityContext(), request, payload);
			}
		}
		return payload;
	}

	@Override
	public EntityPayload persist(final EntityPersistRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl =
					resolveEntityServiceImpl(request.getEntityType(), payload);
			if(impl != null) {
				impl.persist(getMEntityContext(), request, payload);
			}
		}
		return payload;
	}

	@Override
	public EntityPayload purge(final EntityPurgeRequest request) {
		final EntityPayload payload = new EntityPayload();
		if(validateEntityRequest(request, payload)) {
			final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl =
					resolveEntityServiceImpl(request.getEntityType(), payload);
			if(impl != null) {
				impl.purge(getMEntityContext(), request, payload);
			}
		}
		return payload;
	}

	@Override
	public ICriteria<? extends IEntity> translate(final ISearch search) throws IllegalArgumentException, SystemError {
		if(search == null) {
			throw new IllegalArgumentException("Null search argument.");
		}
		final MEntityContext context = getMEntityContext();
		final Class<? extends IEntity> entityClass = EntityTypeUtil.getEntityClass(search.getEntityType());
		return MEntityServiceImplFactory.instance(entityClass, context.getServiceResolver())
				.translate(
				getMEntityContext(),
				search);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IMarshalingListHandler<IEntity> getMarshalingListHandler(final RemoteListingDefinition listingDefinition) {
		if(listingDefinition == null || listingDefinition.getSearchCriteria() == null) {
			throw new IllegalArgumentException("A listing command and member search property must be set.");
		}
		final IEntityType entityType = listingDefinition.getSearchCriteria().getEntityType();
		final MEntityServiceImpl<IEntity, ISearch> svc =
				(MEntityServiceImpl<IEntity, ISearch>) MEntityServiceImplFactory.instance(EntityTypeUtil
						.getEntityClass(entityType), getMEntityContext()
						.getServiceResolver());
		return svc.getMarshalingListHandler(getMEntityContext(), listingDefinition);
	}

}
