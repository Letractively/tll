/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import com.google.inject.Inject;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.common.model.IEntityType;
import com.tll.listhandler.IListingDataProvider;
import com.tll.model.IEntity;
import com.tll.model.IEntityTypeResolver;
import com.tll.server.listing.IListingDataProviderResolver;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * SmbizListingDataProviderResolver
 * @author jpk
 */
public class SmbizListingDataProviderResolver implements IListingDataProviderResolver {

	private final IEntityTypeResolver etResolver;
	private final IEntityServiceFactory svcFactory;

	/**
	 * Constructor
	 * @param etResolver
	 * @param svcFactory
	 */
	@Inject
	public SmbizListingDataProviderResolver(IEntityTypeResolver etResolver, IEntityServiceFactory svcFactory) {
		super();
		this.etResolver = etResolver;
		this.svcFactory = svcFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IListingDataProvider resolve(ListingRequest request) throws IllegalArgumentException {
		try {
			final IEntityType et = request.getListingDef().getSearchCriteria().getEntityType();
			return svcFactory.instanceByEntityType((Class<IEntity>) etResolver.resolveEntityClass(et));
		}
		catch(final Exception e) {
			// fall through
		}
		throw new IllegalArgumentException("Can't resolve listing data provider for request: " + request.descriptor());
	}
}
