/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2007
 */
package com.tll.server.rpc.entity.impl;

import java.util.Map;

import com.tll.SystemError;
import com.tll.client.data.EntityOptions;
import com.tll.client.model.RefKey;
import com.tll.model.impl.AccountAddress;
import com.tll.server.RequestContext;
import com.tll.server.rpc.MarshalOptions;
import com.tll.server.rpc.entity.MNamedEntityServiceImpl;

/**
 * AccountAddressService
 * @author jpk
 */
public final class AccountAddressService extends MNamedEntityServiceImpl<AccountAddress> {

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
}
