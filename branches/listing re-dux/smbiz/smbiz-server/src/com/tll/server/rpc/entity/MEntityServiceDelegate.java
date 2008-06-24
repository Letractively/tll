/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.SystemError;
import com.tll.client.data.EntityGetEmptyRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;
import com.tll.client.data.EntityRequest;
import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.Status;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.ServletUtil;
import com.tll.server.rpc.RpcServlet;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.service.entity.IEntityService;

/**
 * MEntityServiceDelegate
 * @author jpk
 */
public class MEntityServiceDelegate extends RpcServlet implements IMEntityService<IEntity> {

	private static final long serialVersionUID = 5017008307371980402L;

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
	private IMEntityServiceImpl<? extends IEntity> resolveEntityServiceImpl(final EntityRequest request,
			final EntityPayload payload) {
		// ensure non-null request
		if(request == null) {
			payload.getStatus().addMsg("No entity request specified", MsgLevel.ERROR);
			return null;
		}

		// validate entity type string
		EntityType entityType = request.getEntityType();
		if(entityType == null) {
			payload.getStatus().addMsg("No entity type specified", MsgLevel.ERROR);
			return null;
		}

		// resolve the rpc entity service impl
		try {
			return MEntityServiceImplFactory.instance(entityType);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(getRequestContext(), payload.getStatus(), se, se.getMessage(), true);
			return null;
		}
	}

	public EntityPayload getEmptyEntity(final EntityGetEmptyRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.getEmptyEntity(getRequestContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload load(final EntityLoadRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.load(getRequestContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload persist(final EntityPersistRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.persist(getRequestContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public EntityPayload purge(final EntityPurgeRequest request) {
		final EntityPayload payload = new EntityPayload();
		final IMEntityServiceImpl<? extends IEntity> impl = resolveEntityServiceImpl(request, payload);
		if(impl != null) {
			impl.purge(getRequestContext(), request, request.getEntityType(), payload);
		}
		return payload;
	}

	public IEntityService<IEntity> getEntityService() {
		throw new UnsupportedOperationException("getEntityService method is unsupported at the delegate level.");
	}

	@SuppressWarnings("unchecked")
	public IMarshalingListHandler<IEntity> getMarshalingListHandler(final RemoteListingDefinition listingDefinition) {
		if(listingDefinition == null || listingDefinition.getCriteria() == null) {
			throw new IllegalArgumentException("A listing command and member search property must be set.");
		}
		final MEntityServiceImpl<IEntity> svc =
				(MEntityServiceImpl<IEntity>) MEntityServiceImplFactory.instance(EntityUtil
						.entityTypeFromClass(listingDefinition.getCriteria().getEntityClass()));
		return svc.getMarshalingListHandler(getRequestContext(), listingDefinition);
	}

}
