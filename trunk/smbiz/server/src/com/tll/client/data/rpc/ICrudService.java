/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityGetEmptyRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;
import com.tll.client.data.EntityRequest;
import com.tll.client.model.RefKey;

/**
 * ICrudService
 * @author jpk
 */
public interface ICrudService extends RemoteService {

	/**
	 * Generates an empty entity of the desired type.
	 * @param request The {@link EntityRequest} specifying the entity type.
	 * @return EntityPayload
	 */
	EntityPayload getEmptyEntity(EntityGetEmptyRequest request);

	/**
	 * Loads an existing entity.
	 * @param request The {@link EntityRequest} specifying an {@link RefKey}, an
	 *        optional {@link EntityOptions} and an optional
	 *        {@link AuxDataRequest}.
	 * @return EntityPayload
	 */
	EntityPayload load(EntityLoadRequest request);

	/**
	 * Persists an entity.
	 * @param request The {@link EntityRequest} specifying the entity to be
	 *        persisted, and an optional {@link EntityOptions}.
	 * @return EntityPayload
	 */
	EntityPayload persist(EntityPersistRequest request);

	/**
	 * Purges an entity.
	 * @param request The {@link EntityRequest} specifying the {@link RefKey} to
	 *        be purged.
	 * @return EntityPayload
	 */
	EntityPayload purge(EntityPurgeRequest request);
}
