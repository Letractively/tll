/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.listing.test;

import com.google.inject.Inject;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.listhandler.IListingDataProvider;
import com.tll.model.IEntity;
import com.tll.model.IEntityTypeResolver;
import com.tll.server.listing.IListingDataProviderResolver;
import com.tll.service.entity.IEntityServiceFactory;


/**
 * TestListingDataProviderResolver
 * @author jpk
 */
public class TestListingDataProviderResolver implements IListingDataProviderResolver {

	private final IEntityTypeResolver etResolver;

	private final IEntityServiceFactory entityServiceFactory;

	/**
	 * Constructor
	 * @param etResolver
	 * @param entityServiceFactory
	 */
	@Inject
	public TestListingDataProviderResolver(IEntityTypeResolver etResolver, IEntityServiceFactory entityServiceFactory) {
		super();
		assert etResolver != null && entityServiceFactory != null;
		this.etResolver = etResolver;
		this.entityServiceFactory = entityServiceFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IListingDataProvider resolve(ListingRequest request)
	throws IllegalArgumentException {
		try {
			//final String et = request.getListingDef().getSearchCriteria().getEntityType();
			final String et = "Address";
			return entityServiceFactory.instanceByEntityType((Class<IEntity>) etResolver.resolveEntityClass(et));
		}
		catch(final Exception e) {
			// fall through
		}
		throw new IllegalArgumentException("Can't resolve listing data provider for request: " + request.descriptor());
	}

}
