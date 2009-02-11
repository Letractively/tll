/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import javax.servlet.ServletContext;

import com.tll.SystemError;
import com.tll.common.data.EntityFetchPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.Status;
import com.tll.common.model.IEntityType;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.service.entity.IEntityService;

/**
 * MEntityServiceDelegate - Front line rpc servlet that routes entity related
 * rpc requests.
 * @author jpk
 */
public class MEntityServiceDelegate extends RpcServlet implements IMEntityService<IEntity, ISearch> {

	private static final long serialVersionUID = 5017008307371980402L;
	
	/**
	 * Obtains the {@link IMEntityServiceContext} from the {@link ServletContext}.
	 * @return The {@link IMEntityServiceContext} instance.
	 */
	private IMEntityServiceContext getContext() {
		return (IMEntityServiceContext) getThreadLocalRequest().getSession(false).getServletContext().getAttribute(
				IMEntityServiceContext.SERVLET_CONTEXT_KEY);
	}

	/**
	 * Resolves the appropriate {@link IMEntityServiceImpl} class for the given
	 * entity type.
	 * @param request The {@link EntityRequest}
	 * @param payload The {@link EntityPayload}
	 * @return The associated {@link IMEntityServiceImpl} impl instance or
	 *         <code>null</code> when unable to resolve in which case, the
	 *         {@link EntityPayload}'s {@link Status} is updated with an error
	 *         message.
	 */
	private IMEntityServiceImpl<? extends IEntity, ? extends ISearch> resolveEntityServiceImpl(
			final EntityRequest request, final EntityPayload payload) {
		// ensure non-null request
		if(request == null) {
			payload.getStatus().addMsg("No entity request specified", MsgLevel.ERROR);
			return null;
		}

		// validate entity type string
		IEntityType entityType = request.getEntityType();
		if(entityType == null) {
			payload.getStatus().addMsg("No entity type specified", MsgLevel.ERROR);
			return null;
		}

		// resolve the rpc entity service impl
		try {
			return MEntityServiceImplFactory.instance(EntityTypeUtil.getEntityClass(entityType), getContext()
					.getServiceResolver());
		}
		catch(final SystemError se) {
			getContext().getExceptionHandler().handleException(payload.getStatus(), se, se.getMessage(), true);
			return null;
		}
	}

	public EntityPayload getEmptyEntity(final EntityFetchPrototypeRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.getEmptyEntity(getContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload load(final EntityLoadRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.load(getContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload persist(final EntityPersistRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.persist(getContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload purge(final EntityPurgeRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity, ? extends ISearch> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.purge(getContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public IEntityService<IEntity> getEntityService() {
		throw new UnsupportedOperationException("getEntityService method is unsupported at the delegate level.");
	}

	public ICriteria<? extends IEntity> translate(final ISearch search) throws IllegalArgumentException, SystemError {
		if(search == null) {
			throw new IllegalArgumentException("Null search argument.");
		}
		IMEntityServiceContext context = getContext();
		final Class<? extends IEntity> entityClass = EntityTypeUtil.getEntityClass(search.getEntityType());
		return MEntityServiceImplFactory.instance(entityClass, context.getServiceResolver())
				.translate(getContext(), search);
	}

	@SuppressWarnings("unchecked")
	public IMarshalingListHandler<IEntity> getMarshalingListHandler(final RemoteListingDefinition listingDefinition) {
		if(listingDefinition == null || listingDefinition.getSearchCriteria() == null) {
			throw new IllegalArgumentException("A listing command and member search property must be set.");
		}
		final IEntityType entityType = listingDefinition.getSearchCriteria().getEntityType();
		final MEntityServiceImpl<IEntity, ISearch> svc =
				(MEntityServiceImpl<IEntity, ISearch>) MEntityServiceImplFactory.instance(EntityTypeUtil
						.getEntityClass(entityType), getContext()
						.getServiceResolver());
		return svc.getMarshalingListHandler(getContext(), listingDefinition);
	}

}
