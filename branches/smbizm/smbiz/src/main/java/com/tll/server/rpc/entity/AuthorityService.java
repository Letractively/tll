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
import com.tll.model.Authority;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;

/**
 * AuthorityService
 * @author jpk
 */
public class AuthorityService extends MNamedEntityServiceImpl<Authority> {

	public static final MarshalOptions MARSHAL_OPTIONS = new MarshalOptions(false, 0);

	@Override
	public MarshalOptions getMarshalOptions(MEntityContext context) {
		return MARSHAL_OPTIONS;
	}

	@Override
	protected void handleLoadOptions(MEntityContext context, Authority e, EntityOptions options,
			Map<String, ModelKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(MEntityContext context, Authority e, EntityOptions options)
	throws SystemError {
	}

	@Override
	protected IBusinessKey<Authority> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(MEntityContext context, ISearch search, ICriteria<Authority> criteria)
	throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
