/**
 * The Logic Lab
 * @author jpk
 * Nov 12, 2007
 */
package com.tll.server.rpc.entity;

import com.tll.SystemError;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.data.rpc.ICrudService;
import com.tll.common.data.rpc.IListingService;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.IEntity;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.service.entity.IEntityService;

/**
 * IMEntityService - Performs tasks specific to a single entity type.
 * @author jpk
 * @param <E>
 * @param <S>
 */
public interface IMEntityService<E extends IEntity, S extends ISearch> extends ICrudService {

	/**
	 * @return The {@link IEntityService}.
	 */
	IEntityService<E> getEntityService();

	/**
	 * Translates {@link ISearch} instances to server-side compatible
	 * {@link ICriteria} instances.
	 * @param search Client-side search instance.
	 * @return Server-side criteria instance.
	 * @throws IllegalArgumentException When <code>search</code> param is invalid.
	 * @throws SystemError When the impl service is unable to be properly
	 *         resolved.
	 */
	ICriteria<? extends E> translate(ISearch search) throws IllegalArgumentException, SystemError;

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
	IMarshalingListHandler<E> getMarshalingListHandler(RemoteListingDefinition<S> listingDefinition)
			throws IllegalArgumentException, SystemError;
}
