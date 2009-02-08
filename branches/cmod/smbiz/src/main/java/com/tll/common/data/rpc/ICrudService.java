/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityFetchPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;
import com.tll.common.model.RefKey;

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
	EntityPayload getEmptyEntity(EntityFetchPrototypeRequest request);

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
