/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.server.rpc.entity.mock;

import com.google.inject.Inject;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.ListingRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.model.Address;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IPersistServiceImpl;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;

/**
 * TestMEntityServiceImplResolver
 * @author jpk
 */
public class TestPersistServiceImplResolver implements IPersistServiceImplResolver {

	private final IEntityTypeResolver etResolver;

	/**
	 * Constructor
	 * @param etResolver
	 */
	@Inject
	public TestPersistServiceImplResolver(IEntityTypeResolver etResolver) {
		super();
		this.etResolver = etResolver;
	}

	@Override
	public Class<? extends IPersistServiceImpl> resolve(IModelRelatedRequest request)
	throws IllegalArgumentException {

		if(request instanceof IEntityTypeProvider) {
			return resolve(((IEntityTypeProvider) request).getEntityType());
		}

		// listing request?
		if(request instanceof ListingRequest<?>) {
			try {
				return resolve(((ListingRequest<?>) request).getListingDef().getSearchCriteria().getEntityType());
			}
			catch(final NullPointerException e) {
				// fall through
			}
		}

		// unhandled
		throw new IllegalArgumentException("Unhandled request: " + request.descriptor());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IPersistServiceImpl> resolve(IEntityType etype)
	throws IllegalArgumentException {
		final Class<? extends IEntity> entityClass = (Class<? extends IEntity>) etResolver.resolveEntityClass(etype);
		final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

		if(Address.class.isAssignableFrom(rootEntityClass)) {
			return TestAddressService.class;
		}

		throw new IllegalArgumentException("Unhandled entity type: " + etype);
	}
}
