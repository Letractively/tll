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
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.User;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;

/**
 * UserService
 * @author jpk
 */
public class UserService extends MNamedEntityServiceImpl<User> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(true, 2);

	@Override
	public MarshalOptions getMarshalOptions(MEntityContext contexxt) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(MEntityContext contexxt, User e, EntityOptions options,
			Map<String, ModelKey> refs)
	throws SystemError {
	}

	@Override
	protected void handlePersistOptions(MEntityContext contexxt, User e, EntityOptions options)
	throws SystemError {
	}

	@Override
	protected IBusinessKey<User> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(MEntityContext contexxt, ISearch search,
			ICriteria<User> criteria)
	throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
