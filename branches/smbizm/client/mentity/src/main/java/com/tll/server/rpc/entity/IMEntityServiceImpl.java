package com.tll.server.rpc.entity;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.marshal.MarshalOptions;

/**
 * IMEntityServiceImpl - Server side entity crud support.
 * @author jpk
 * @param <E> The "root" entity type
 */
public interface IMEntityServiceImpl<E extends IEntity> {

	/**
	 * Loads an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityLoadRequest}
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void load(MEntityContext context, EntityLoadRequest request, EntityPayload payload);

	/**
	 * Persists an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPersistRequest}
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void persist(MEntityContext context, EntityPersistRequest request, EntityPayload payload);

	/**
	 * Purges an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link EntityPurgeRequest}
	 * @param payload The {@link EntityPayload} that is filled
	 */
	void purge(MEntityContext context, EntityPurgeRequest request, EntityPayload payload);

	/**
	 * Translate client-side search to server-side serach.
	 * @param context Guaranteed non-<code>null</code>
	 * @param search The client side {@link ISearch} instance
	 * @return Translated search {@link ICriteria}.
	 * @throws IllegalArgumentException
	 */
	ICriteria<E> translate(MEntityContext context, ISearch search) throws IllegalArgumentException;

	/**
	 * Gets the entity type specific marshaling options.
	 * @param context Guaranteed non-<code>null</code>
	 * @return The {@link MarshalOptions}
	 */
	MarshalOptions getMarshalOptions(MEntityContext context);
}