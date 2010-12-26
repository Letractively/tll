/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.search.IListingSearch;
import com.tll.criteria.Criteria;
import com.tll.model.IEntity;
import com.tll.server.listing.ListingContext;


/**
 * SmbizListingSearchTranslator
 * @author jpk
 */
public class SmbizListingSearchTranslator extends AbstractListingSearchTranslator {

	@Override
	protected Criteria<? extends IEntity> translateNonNamedQuery(ListingContext context, IListingSearch listingSearch) {
		return null;
	}

}
