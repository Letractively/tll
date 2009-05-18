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
import com.tll.model.Interface;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.INamedEntityService;

/**
 * InterfaceService
 * @author jpk
 */
public class InterfaceService extends PersistServiceImpl<Interface> {

	public static final MarshalOptions MARSHAL_OPTIONS = MarshalOptions.UNCONSTRAINED_MARSHALING;

	@Override
	public MarshalOptions getMarshalOptions(PersistContext context) {
		return MARSHAL_OPTIONS;
	}

	@Override
	protected Interface loadByName(Class<Interface> entityClass, IEntityService<Interface> svc, String name)
			throws UnsupportedOperationException {
		final INamedEntityService<Interface> nsvc = (INamedEntityService<Interface>) svc;
		return nsvc.load(new NameKey<Interface>(entityClass, name));
	}

	@Override
	protected void handleLoadOptions(PersistContext context, Interface e, EntityOptions options,
			Map<String, ModelKey> refs) throws SystemError {
	}

	@Override
	protected void handlePersistOptions(PersistContext context, Interface e, EntityOptions options)
	throws SystemError {
	}

	@Override
	protected IBusinessKey<Interface> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void handleSearchTranslation(PersistContext context, ISearch search, ICriteria<Interface> criteria)
	throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
