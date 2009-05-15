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
import com.tll.model.key.NameKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.INamedEntityService;

/**
 * UserService
 * @author jpk
 */
public class UserService extends PersistServiceImpl<User> {

	public static final MarshalOptions MARSHAL_OPTIONS = new MarshalOptions(true, 2);

	@Override
	public MarshalOptions getMarshalOptions(PersistContext contexxt) {
		return MARSHAL_OPTIONS;
	}

	@Override
	protected User loadByName(Class<User> entityClass, IEntityService<User> svc, String name)
			throws UnsupportedOperationException {
		final INamedEntityService<User> nsvc = (INamedEntityService<User>) svc;
		return nsvc.load(new NameKey<User>(entityClass, name));
	}

	@Override
	protected void handleLoadOptions(PersistContext contexxt, User e, EntityOptions options,
			Map<String, ModelKey> refs)
	throws SystemError {
	}

	@Override
	protected void handlePersistOptions(PersistContext contexxt, User e, EntityOptions options)
	throws SystemError {
	}

	@Override
	protected IBusinessKey<User> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(PersistContext contexxt, ISearch search,
			ICriteria<User> criteria)
	throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
