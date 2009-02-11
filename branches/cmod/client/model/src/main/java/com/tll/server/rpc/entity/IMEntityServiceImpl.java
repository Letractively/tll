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
import com.tll.model.IEntity;
import com.tll.model.IEntityType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.listing.IMarshalingListHandler;

/**
 * IMEntityServiceImpl - Server side support for the RPC {@link IMEntityService}
 * implementation delegate.
 * @author jpk
 * @param <E> The entity type
 * @param <S> The search type
 */
public interface IMEntityServiceImpl<E extends IEntity, S extends ISearch> {

	/**
	 * Does this impl support the given entity type? This is used by the delegator
	 * to properly route requests.
	 * @param entityClass The entity type
	 * @return true/false
	 */
	boolean supports(Class<? extends IEntity> entityClass);

	/**
	 * @return The named query resolver.
	 */
	INamedQueryResolver getQueryResolver();

	/**
	 * Get an empty entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityFetchPrototypeRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link IEntityType} for the {@link EntityRequest}.
	 * @param payload
	 */
	// TODO change to generateEntity where generate is always true ?
	void getEmptyEntity(IMEntityServiceContext context, EntityFetchPrototypeRequest request, IEntityType entityType,
			EntityPayload payload);

	/**
	 * Loads an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityLoadRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link IEntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void load(IMEntityServiceContext context, EntityLoadRequest request, IEntityType entityType, EntityPayload payload);

	/**
	 * Persists an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPersistRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link IEntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void persist(IMEntityServiceContext context, EntityPersistRequest request, IEntityType entityType,
			EntityPayload payload);

	/**
	 * Purges an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPurgeRequest}
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link IEntityType} for the {@link EntityRequest}.
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void purge(IMEntityServiceContext context, EntityPurgeRequest request, IEntityType entityType, EntityPayload payload);

	/**
	 * Translate client-side search to server-side serach.
	 * @param context Guaranteed non-<code>null</code>
	 * @param entityType The guaranteed non-<code>null</code> resolved
	 *        {@link IEntityType} for the {@link ISearch}.
	 * @param search The client side {@link ISearch} instance
	 * @return Translated search {@link ICriteria}.
	 * @throws IllegalArgumentException
	 */
	ICriteria<E> translate(IMEntityServiceContext context, IEntityType entityType, S search)
			throws IllegalArgumentException;

	/**
	 * Gets the entity type specific marshaling options.
	 * @param context Guaranteed non-<code>null</code>
	 * @return The {@link MarshalOptions}
	 */
	MarshalOptions getMarshalOptions(IMEntityServiceContext context);

	/**
	 * Provides the entity type specific {@link IMarshalingListHandler}.
	 * @param context Guaranteed non-<code>null</code>
	 * @param listingDefinition The listing definition
	 * @return The marshaling list handler.
	 */
	IMarshalingListHandler<E> getMarshalingListHandler(IMEntityServiceContext context,
			RemoteListingDefinition<S> listingDefinition);

}