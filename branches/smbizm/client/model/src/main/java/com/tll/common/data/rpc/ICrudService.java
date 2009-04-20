/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.EntityRequest;

/**
 * ICrudService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/crud")
public interface ICrudService extends RemoteService {

	/**
	 * Loads an existing entity.
	 * @param request The {@link EntityRequest} specifying an entity key, an
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
	 * @param request The {@link EntityRequest} specifying the key of the entity
	 *        to be purged.
	 * @return EntityPayload
	 */
	EntityPayload purge(EntityPurgeRequest request);
}
