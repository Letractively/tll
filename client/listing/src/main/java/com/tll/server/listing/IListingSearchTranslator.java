/**
 * The Logic Lab
 * @author jpk
 * @since May 20, 2009
 */
package com.tll.server.listing;

import com.tll.IMarshalable;
import com.tll.criteria.Criteria;
import com.tll.model.IEntity;

/**
 * IListingSearchTranslator - Responsible for translating {@link IMarshalable}
 * criteria to server-side {@link Criteria}.
 * @author jpk
 */
public interface IListingSearchTranslator {

	/**
	 * Translates client-side listing search criteria to server-side search
	 * criteria.
	 * @param context the listing context
	 * @param listingSearch client side search criteria for a listing of model
	 *        data
	 * @return Translated search {@link Criteria}.
	 * @throws IllegalArgumentException When an error occurrs during the
	 *         translation
	 */
	Criteria<? extends IEntity> translateListingSearchCriteria(ListingContext context, IMarshalable listingSearch)
	throws IllegalArgumentException;
}
