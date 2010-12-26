/**
 * The Logic Lab
 * @author jpk
 * @since Dec 26, 2010
 */
package com.tll.server.listing.test;

import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.search.test.TestAddressSearch;
import com.tll.listhandler.IListHandler;
import com.tll.model.test.Address;
import com.tll.server.listing.IRowListHandlerProvider;


/**
 * TestRowListHandlerProvider
 * @author jpk
 */
public class TestRowListHandlerProvider implements IRowListHandlerProvider<TestAddressSearch, Address> {

	@Override
	public IListHandler<Address> getRowListHandler(RemoteListingDefinition<TestAddressSearch> listingDef) {
		// TODO impl
		return null;
	}


}
