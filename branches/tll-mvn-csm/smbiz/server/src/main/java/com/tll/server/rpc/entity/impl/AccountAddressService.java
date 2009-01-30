/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2007
 */
package com.tll.server.rpc.entity.impl;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.RefKey;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.AccountAddress;
import com.tll.model.key.BusinessKey;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.MNamedEntityServiceImpl;

/**
 * AccountAddressService
 * @author jpk
 */
public final class AccountAddressService extends MNamedEntityServiceImpl<AccountAddress, ISearch> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(false, 1);

	public MarshalOptions getMarshalOptions(RequestContext requestContext) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(RequestContext requestContext, AccountAddress e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError {
		// no-op
	}

	@Override
	protected void handlePersistOptions(RequestContext requestContext, AccountAddress e, EntityOptions options)
			throws SystemError {
		// no-op
	}

	@Override
	protected BusinessKey<AccountAddress> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	protected void handleSearchTranslation(RequestContext requestContext, ISearch search,
			ICriteria<? extends AccountAddress> criteria) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
}
