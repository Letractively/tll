/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.RefKey;
import com.tll.common.search.ISearch;
import com.tll.criteria.ICriteria;
import com.tll.model.Interface;
import com.tll.model.key.IBusinessKey;
import com.tll.server.marshal.MarshalOptions;

/**
 * InterfaceService
 * @author jpk
 */
public class InterfaceService extends MNamedEntityServiceImpl<Interface, ISearch> {

	private static final MarshalOptions marshalOptions = MarshalOptions.UNCONSTRAINED_MARSHALING;

	@Override
	public MarshalOptions getMarshalOptions(IMEntityServiceContext context) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(IMEntityServiceContext context, Interface e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(IMEntityServiceContext context, Interface e, EntityOptions options)
			throws SystemError {
	}

	@Override
	protected IBusinessKey<Interface> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(IMEntityServiceContext context, ISearch search, ICriteria<Interface> criteria)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
