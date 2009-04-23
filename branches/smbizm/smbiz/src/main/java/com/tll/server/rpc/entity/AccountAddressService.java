/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2007
 */
package com.tll.server.rpc.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.ModelKey;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.AccountAddress;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;

/**
 * AccountAddressService
 * @author jpk
 */
public final class AccountAddressService extends MNamedEntityServiceImpl<AccountAddress> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(false, 1);

	public MarshalOptions getMarshalOptions(MEntityContext context) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(MEntityContext context, AccountAddress e, EntityOptions options,
			Map<String, ModelKey> refs) throws SystemError {
		// no-op
	}

	@Override
	protected void handlePersistOptions(MEntityContext context, AccountAddress e, EntityOptions options)
	throws SystemError {
		// no-op
	}

	@Override
	protected IBusinessKey<AccountAddress> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	protected void handleSearchTranslation(MEntityContext context, ISearch search,
			ICriteria<AccountAddress> criteria) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

}
