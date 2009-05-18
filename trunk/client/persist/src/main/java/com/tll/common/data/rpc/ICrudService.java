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
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.ModelRequest;

/**
 * ICrudService
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/crud")
public interface ICrudService extends RemoteService {

	/**
	 * Loads an existing entity.
	 * @param request The {@link ModelRequest} specifying an entity key, an
	 *        optional {@link EntityOptions} and an optional
	 *        {@link AuxDataRequest}.
	 * @return EntityPayload
	 */
	ModelPayload load(EntityLoadRequest request);

	/**
	 * Persists an entity.
	 * @param request The {@link ModelRequest} specifying the entity to be
	 *        persisted, and an optional {@link EntityOptions}.
	 * @return EntityPayload
	 */
	ModelPayload persist(PersistRequest request);

	/**
	 * Purges an entity.
	 * @param request The {@link ModelRequest} specifying the key of the entity
	 *        to be purged.
	 * @return EntityPayload
	 */
	ModelPayload purge(PurgeRequest request);
}
