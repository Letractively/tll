/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing.mock;

import com.google.inject.Inject;
import com.tll.common.data.ListingRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.search.ISearch;
import com.tll.listhandler.IListingDataProvider;
import com.tll.model.IEntity;
import com.tll.server.RequestContext;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.PersistContext;
import com.tll.server.rpc.listing.IListingDataProviderResolver;


/**
 * TestListingDataProviderResolver
 * @author jpk
 */
public class TestListingDataProviderResolver implements IListingDataProviderResolver {

	private final IEntityTypeResolver etResolver;

	/**
	 * Constructor
	 * @param etResolver
	 */
	@Inject
	public TestListingDataProviderResolver(IEntityTypeResolver etResolver) {
		super();
		this.etResolver = etResolver;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IListingDataProvider resolve(RequestContext requestContext, ListingRequest<? extends ISearch> request)
	throws IllegalArgumentException {
		try {
			final PersistContext mc = (PersistContext) requestContext.getServletContext().getAttribute(PersistContext.KEY);
			final IEntityType et = request.getListingDef().getSearchCriteria().getEntityType();
			return mc.getEntityServiceFactory().instanceByEntityType((Class<IEntity>) etResolver.resolveEntityClass(et));
		}
		catch(final Exception e) {
			// fall through
		}
		throw new IllegalArgumentException("Can't resolve listing data provider for request: " + request.descriptor());
	}

}
