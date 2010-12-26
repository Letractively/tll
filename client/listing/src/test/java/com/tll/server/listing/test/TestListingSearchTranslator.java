/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.server.listing.test;

import com.tll.IMarshalable;
import com.tll.criteria.Criteria;
import com.tll.model.IEntity;
import com.tll.model.test.Address;
import com.tll.server.listing.IListingSearchTranslator;
import com.tll.server.listing.ListingContext;

/**
 * TestListingSearchTranslator
 * @author jpk
 */
public class TestListingSearchTranslator implements IListingSearchTranslator {

	@Override
	public Criteria<? extends IEntity> translateListingSearchCriteria(ListingContext context, IMarshalable listingSearch) {
		final Criteria<Address> c = new Criteria<Address>(Address.class);
		return c;
	}

}
