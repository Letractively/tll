/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.ModelKey;
import com.tll.common.search.TestAddressSearch;
import com.tll.criteria.Comparator;
import com.tll.criteria.ICriteria;
import com.tll.model.Address;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;

/**
 * TestAddressService
 * @author jpk
 */
public class TestAddressService extends MEntityServiceImpl<Address, TestAddressSearch> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(false, 0);

	@Override
	public MarshalOptions getMarshalOptions(MEntityContext context) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(MEntityContext context, Address e, EntityOptions options,
			Map<String, ModelKey> refs)
			throws SystemError {
	}

	@Override
	protected void handlePersistOptions(MEntityContext context, Address e, EntityOptions options)
			throws SystemError {
	}

	@Override
	protected IBusinessKey<Address> handleBusinessKeyTranslation(TestAddressSearch search) {
		IBusinessKey<Address> bk;
		try {
			bk = BusinessKeyFactory.create(Address.class, search.getBusinessKeyName());
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new SystemError("No business keys defined for Address entity");
		}
		bk.setPropertyValue("address1", search.getAddress1());
		bk.setPropertyValue("postalCode", search.getPostalCode());
		return bk;
	}

	@Override
	protected void handleSearchTranslation(MEntityContext context, TestAddressSearch search,
			ICriteria<Address> criteria) throws IllegalArgumentException {
		criteria.getPrimaryGroup().addCriterion("address1", search.getAddress1(), Comparator.EQUALS, false);
		criteria.getPrimaryGroup().addCriterion("postalCode", search.getPostalCode(), Comparator.EQUALS, false);
	}
}
