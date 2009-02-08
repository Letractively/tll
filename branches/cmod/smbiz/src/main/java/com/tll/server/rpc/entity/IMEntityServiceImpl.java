package com.tll.server.rpc.entity;

import com.tll.common.data.EntityFetchPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.EntityType;
import com.tll.model.IEntity;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.listing.IMarshalingListHandler;

/**
 * IMEntityServiceImpl - Server side support for the RPC {@link IMEntityService}
 * implementation delegate.
 * @author jpk
 * @param <E>
 * @param <S>
 */
public interface IMEntityServiceImpl<E extends IEntity, S extends ISearch> {

	/**
	 * Get an empty entity.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityFetchPrototypeRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link EntityType} for the {@link EntityRequest}.
	 * @param payload
	 */
	// TODO change to generateEntity where generate is always true ?
	void getEmptyEntity(RequestContext requestContext, EntityFetchPrototypeRequest request, EntityType entityType,
			EntityPayload payload);

	/**
	 * Loads an entity.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityLoadRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link EntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void load(RequestContext requestContext, EntityLoadRequest request, EntityType entityType, EntityPayload payload);

	/**
	 * Persists an entity.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPersistRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link EntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void persist(RequestContext requestContext, EntityPersistRequest request, EntityType entityType, EntityPayload payload);

	/**
	 * Purges an entity.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPurgeRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link EntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void purge(RequestContext requestContext, EntityPurgeRequest request, EntityType entityType, EntityPayload payload);

	/**
	 * Translate client-side search to server-side serach.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link EntityType} for the {@link ISearch}.
	 * @param search The client side {@link ISearch} instance
	 * @return Translated search {@link ICriteria}.
	 * @throws IllegalArgumentException
	 */
	ICriteria<E> translate(RequestContext requestContext, EntityType entityType, S search)
			throws IllegalArgumentException;

	/**
	 * Gets the entity type specific marshaling options.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @return The {@link MarshalOptions}
	 */
	MarshalOptions getMarshalOptions(RequestContext requestContext);

	/**
	 * Provides the entity type specific {@link IMarshalingListHandler}.
	 * @param requestContext Guaranteed non-<code>null</code>
	 * @param listingDefinition The listing definition
	 * @return The marshaling list handler.
	 */
	IMarshalingListHandler<E> getMarshalingListHandler(RequestContext requestContext,
			RemoteListingDefinition<S> listingDefinition);

}