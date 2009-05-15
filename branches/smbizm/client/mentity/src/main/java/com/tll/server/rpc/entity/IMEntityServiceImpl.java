package com.tll.server.rpc.entity;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
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
	 * @param payload The {@link ModelPayload} that is filled
	 */
	void load(MEntityContext context, EntityLoadRequest request, ModelPayload payload);

	/**
	 * Persists an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link PersistRequest}
	 * @param payload The {@link ModelPayload} that is filled
	 */
	void persist(MEntityContext context, PersistRequest request, ModelPayload payload);

	/**
	 * Purges an entity.
	 * @param context Guaranteed non-<code>null</code>
	 * @param request The guaranteed non-<code>null</code>
	 *        {@link PurgeRequest}
	 * @param payload The {@link ModelPayload} that is filled
	 */
	void purge(MEntityContext context, PurgeRequest request, ModelPayload payload);

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