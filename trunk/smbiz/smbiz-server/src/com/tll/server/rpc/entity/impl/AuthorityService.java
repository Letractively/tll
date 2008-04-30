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
import com.tll.client.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.impl.Authority;
import com.tll.server.RequestContext;
import com.tll.server.rpc.MarshalOptions;
import com.tll.server.rpc.entity.MNamedEntityServiceImpl;

/**
 * AuthorityService
 * @author jpk
 */
public class AuthorityService extends MNamedEntityServiceImpl<Authority> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(false, false, 1);

	public MarshalOptions getMarshalOptions(RequestContext requestContext) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(RequestContext requestContext, Authority e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(RequestContext requestContext, Authority e, EntityOptions options)
			throws SystemError {
	}

	@Override
	protected void handleSearchTranslation(RequestContext requestContext, ISearch search,
			ICriteria<? extends Authority> criteria) throws IllegalArgumentException {
	}
}
