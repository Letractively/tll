/**
 * The Logic Lab
 * @author jpk
 * Nov 12, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.SystemError;
import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.rpc.ICrudService;
import com.tll.client.data.rpc.IListingService;
import com.tll.model.IEntity;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.service.entity.IEntityService;

/**
 * IMEntityService - Performs tasks specific to a single entity type.
 * @author jpk
 */
public interface IMEntityService<E extends IEntity> extends ICrudService {

	/**
	 * @return The {@link IEntityService}.
	 */
	IEntityService<E> getEntityService();

	/**
	 * Provides an {@link IMarshalingListHandler} for use by
	 * {@link IListingService} implementations.
	 * @param listingDefinition The listing definition.
	 * @return An entity row list handler.
	 * @throws IllegalArgumentException When the <code>listingCommand</code> arg
	 *         is <code>null</code> or invalid.
	 * @throws SystemError When the impl service is unable to be properly
	 *         resolved.
	 */
	IMarshalingListHandler<E> getMarshalingListHandler(RemoteListingDefinition listingDefinition)
			throws IllegalArgumentException, SystemError;
}
