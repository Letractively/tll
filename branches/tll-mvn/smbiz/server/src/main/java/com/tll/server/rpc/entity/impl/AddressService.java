/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity.impl;

import java.util.Map;

import com.tll.SystemError;
import com.tll.client.data.EntityOptions;
import com.tll.client.model.RefKey;
import com.tll.client.search.impl.AddressSearch;
import com.tll.criteria.ICriteria;
import com.tll.model.BusinessKeyFactory;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.impl.Address;
import com.tll.model.key.BusinessKey;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.MEntityServiceImpl;

/**
 * AddressService
 * @author jpk
 */
public class AddressService extends MEntityServiceImpl<Address, AddressSearch> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(false, 0);

	@Override
	public MarshalOptions getMarshalOptions(RequestContext requestContext) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(RequestContext requestContext, Address e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(RequestContext requestContext, Address e, EntityOptions options)
			throws SystemError {
	}

	@Override
	protected BusinessKey<Address> handleBusinessKeyTranslation(AddressSearch search) {
		BusinessKey<Address> bk;
		try {
			bk = BusinessKeyFactory.create(Address.class, search.getBusinessKeyName());
		}
		catch(BusinessKeyNotDefinedException e) {
			throw new SystemError("No business keys defined for Address entity");
		}
		bk.setPropertyValue(0, search.getAddress1());
		bk.setPropertyValue(1, search.getPostalCode());
		return bk;
	}

	@Override
	protected void handleSearchTranslation(RequestContext requestContext, AddressSearch search,
			ICriteria<? extends Address> criteria) throws IllegalArgumentException {
		throw new UnsupportedOperationException("No search implemented for Address type");
	}
}
