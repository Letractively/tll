/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity.mock;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.ModelKey;
import com.tll.common.search.ISearch;
import com.tll.common.search.mock.TestAddressSearch;
import com.tll.criteria.Comparator;
import com.tll.criteria.ICriteria;
import com.tll.model.Address;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.MEntityContext;
import com.tll.server.rpc.entity.MEntityServiceImpl;

/**
 * TestAddressService
 * @author jpk
 */
public class TestAddressService extends MEntityServiceImpl<Address> {

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
	protected IBusinessKey<Address> handleBusinessKeyTranslation(ISearch search) {
		final TestAddressSearch tas = (TestAddressSearch) search;
		IBusinessKey<Address> bk;
		try {
			bk = BusinessKeyFactory.create(Address.class, tas.getBusinessKeyName());
		}
		catch(final BusinessKeyNotDefinedException e) {
			throw new SystemError("No business keys defined for Address entity");
		}
		bk.setPropertyValue("address1", tas.getAddress1());
		bk.setPropertyValue("postalCode", tas.getPostalCode());
		return bk;
	}

	@Override
	protected void handleSearchTranslation(MEntityContext context, ISearch search,
			ICriteria<Address> criteria) throws IllegalArgumentException {
		final TestAddressSearch tas = (TestAddressSearch) search;
		criteria.getPrimaryGroup().addCriterion("address1", tas.getAddress1(), Comparator.EQUALS, false);
		criteria.getPrimaryGroup().addCriterion("postalCode", tas.getPostalCode(), Comparator.EQUALS, false);
	}
}
