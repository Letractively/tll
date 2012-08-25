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
import com.tll.common.dto.test.AddressDto;
import com.tll.dao.Sorting;
import com.tll.listhandler.DecoratedListHandler;
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

	public static class AddressDtoListHandler extends DecoratedListHandler<Address, AddressDto> {

		public AddressDtoListHandler(IListHandler<Address> listHandler) {
			super(listHandler);
		}

		@Override
		protected AddressDto getDecoratedElement(Address elm) {
			return new AddressDto(Long.toString(elm.getId().longValue()), elm.getFirstName(), elm.getLastName(),
					elm.getAddress1(), elm.getAddress2(), elm.getCity(), elm.getProvince(), elm.getPostalCode());
		}

	}

	private final EntityGraph egraph;

	@Inject
	public TestRowListHandlerProvider(EntityGraph egraph) {
		super();
		this.egraph = egraph;
	}

	@Override
	public IListHandler<? extends IMarshalable> getRowListHandler(
			RemoteListingDefinition<? extends IMarshalable> listingDef) {
		try {
			// just create a list handler full of test address entities
			Collection<Address> clc = egraph.getEntitiesByType(Address.class);
			IListHandler<Address> lh = ListHandlerFactory.create(clc, new Sorting("address1"));
			return new AddressDtoListHandler(lh);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
