/**
 * The Logic Lab

 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.search.IListingSearch;
import com.tll.common.search.INamedQuerySearch;
import com.tll.criteria.Criteria;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.IEntity;

/**
 * AbstractListingSearchTranslator
 * @author jpk
 */
public abstract class AbstractListingSearchTranslator implements IListingSearchTranslator {

	@Override
	public final Criteria<? extends IEntity> translateListingSearchCriteria(ListingContext context,
			IListingSearch listingSearch) throws IllegalArgumentException {
		if(listingSearch instanceof INamedQuerySearch) {
			// named query search
			final INamedQuerySearch nqs = (INamedQuerySearch) listingSearch;
			final ISelectNamedQueryDef queryDef = context.getNamedQueryResolver().resolveNamedQuery(nqs.getQueryName());
			if(queryDef == null) {
				throw new IllegalArgumentException("Unable to resolve named query: " + nqs.getQueryName());
			}
			return new Criteria<IEntity>(queryDef, nqs.getQueryParams());
		}
		// non-named query search
		return translateNonNamedQuery(context, listingSearch);
	}

	/**
	 * Translates a non-named query search criteria instance.
	 * @param context
	 * @param listingSearch
	 * @return A new server-side {@link Criteria} instance
	 */
	protected abstract Criteria<? extends IEntity> translateNonNamedQuery(ListingContext context,
			IListingSearch listingSearch);
}
