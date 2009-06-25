/**
 * The Logic Lab
 * @author jpk
 * @since May 20, 2009
 */
package com.tll.server.rpc.listing;

import com.tll.common.search.IListingSearch;
import com.tll.criteria.Criteria;
import com.tll.model.IEntity;


/**
 * IListingSearchTranslator
 * @author jpk
 */
public interface IListingSearchTranslator {


	/**
	 * Translates client-side listing search criteria to server-side search
	 * criteria.
	 * @param listingSearch client side search criteria for a listing of model
	 *        data
	 * @return Translated search {@link Criteria}.
	 * @throws IllegalArgumentException When an error occurrs during the
	 *         translation
	 */
	Criteria<? extends IEntity> translateListingSearchCriteria(IListingSearch listingSearch)
	throws IllegalArgumentException;
}
