/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.data.ListingRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.search.ISearch;
import com.tll.listhandler.IListingDataProvider;
import com.tll.server.RequestContext;
import com.tll.server.rpc.entity.EntityTypeUtil;
import com.tll.server.rpc.entity.MEntityContext;


/**
 * SmbizListingDataProviderResolver
 * @author jpk
 */
public class SmbizListingDataProviderResolver implements IListingDataProviderResolver {

	@Override
	public IListingDataProvider resolve(RequestContext requestContext, ListingRequest<? extends ISearch> request)
			throws IllegalArgumentException {
		try {
			final MEntityContext mc = (MEntityContext) requestContext.getServletContext().getAttribute(MEntityContext.KEY);
			final IEntityType et = request.getListingDef().getSearchCriteria().getEntityType();
			return mc.getEntityServiceFactory().instanceByEntityType(EntityTypeUtil.getEntityClass(et));
		}
		catch(final Exception e) {
			// fall through
		}
		throw new IllegalArgumentException("Can't resolve listing data provider for request: " + request.descriptor());
	}

}
