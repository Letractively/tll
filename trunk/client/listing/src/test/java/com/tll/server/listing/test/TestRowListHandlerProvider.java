/**
 * The Logic Lab
 * @author jpk
 * @since Dec 26, 2010
 */
package com.tll.server.listing.test;

import java.util.Collection;

import com.google.inject.Inject;
import com.tll.IMarshalable;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.model.egraph.EntityGraph;
import com.tll.model.test.Address;
import com.tll.server.listing.IRowListHandlerProvider;

/**
 * TestRowListHandlerProvider
 * @author jpk
 */
public class TestRowListHandlerProvider implements IRowListHandlerProvider {

	private final EntityGraph egraph;

	@Inject
	public TestRowListHandlerProvider(EntityGraph egraph) {
		super();
		this.egraph = egraph;
	}

	@Override
	public IListHandler<? extends IMarshalable> getRowListHandler(RemoteListingDefinition<? extends IMarshalable> listingDef) {
		try {
			// just create a list handler full of test address entities
			Collection<Address> clc = egraph.getEntitiesByType(Address.class);
			return ListHandlerFactory.create(clc, null);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
