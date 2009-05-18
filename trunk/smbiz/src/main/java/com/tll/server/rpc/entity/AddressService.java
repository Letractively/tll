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
import com.tll.common.search.AddressSearch;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.Address;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityService;

/**
 * AddressService
 * @author jpk
 */
public class AddressService extends PersistServiceImpl<Address> {

	public static final MarshalOptions MARSHAL_OPTIONS = new MarshalOptions(false, 0);

	@Override
	public MarshalOptions getMarshalOptions(PersistContext context) {
		return MARSHAL_OPTIONS;
	}

	@Override
	protected Address loadByName(Class<Address> entityClass, IEntityService<Address> svc, String name)
	throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleLoadOptions(PersistContext context, Address e, EntityOptions options,
			Map<String, ModelKey> refs)
	throws SystemError {
	}

	@Override
	protected void handlePersistOptions(PersistContext context, Address e, EntityOptions options)
	throws SystemError {
	}

	@Override
	protected IBusinessKey<Address> handleBusinessKeyTranslation(ISearch search) {
		if(search instanceof AddressSearch) {
			final AddressSearch as = (AddressSearch) search;
			IBusinessKey<Address> bk;
			try {
				bk = BusinessKeyFactory.create(Address.class, as.getBusinessKeyName());
			}
			catch(final BusinessKeyNotDefinedException e) {
				throw new SystemError("No business keys defined for Address entity");
			}
			bk.setPropertyValue("address1", as.getAddress1());
			bk.setPropertyValue("postalCode", as.getPostalCode());
			return bk;
		}
		throw new IllegalArgumentException("Unhandled address search type");
	}

	@Override
	protected void handleSearchTranslation(PersistContext context, ISearch search,
			ICriteria<Address> criteria) throws IllegalArgumentException {
		throw new UnsupportedOperationException("No search implemented for Address type");
	}
}
