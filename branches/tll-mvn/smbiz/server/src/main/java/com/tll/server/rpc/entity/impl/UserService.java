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
import com.tll.client.search.impl.UserSearch;
import com.tll.criteria.ICriteria;
import com.tll.model.impl.User;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.IBusinessKeyFactory;
import com.tll.server.RequestContext;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.MNamedEntityServiceImpl;

/**
 * UserService
 * @author jpk
 */
public class UserService extends MNamedEntityServiceImpl<User, UserSearch> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(true, 1);

	@Override
	public MarshalOptions getMarshalOptions(RequestContext requestContext) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(RequestContext requestContext, User e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(RequestContext requestContext, User e, EntityOptions options) throws SystemError {
	}

	@Override
	protected BusinessKey<User> handleBusinessKeyTranslation(UserSearch search, IBusinessKeyFactory bkf) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(RequestContext requestContext, UserSearch search,
			ICriteria<? extends User> criteria) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
