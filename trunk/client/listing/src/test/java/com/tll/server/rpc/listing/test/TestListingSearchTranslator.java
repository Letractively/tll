/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.server.rpc.listing.test;

import com.tll.common.search.IListingSearch;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.criteria.Criteria;
import com.tll.model.Address;
import com.tll.model.IEntity;
import com.tll.server.rpc.listing.AbstractListingSearchTranslator;
import com.tll.server.rpc.listing.ListingContext;


/**
 * TestListingSearchTranslator
 * @author jpk
 */
public class TestListingSearchTranslator extends AbstractListingSearchTranslator {

	@Override
	protected Criteria<? extends IEntity> translateNonNamedQuery(ListingContext context, IListingSearch listingSearch) {
		if(listingSearch instanceof TestAddressSearch) {
			final Criteria<Address> c = new Criteria<Address>(Address.class);
			return c;
		}
		throw new IllegalArgumentException("Un-handled search criteria type.");
	}

}
