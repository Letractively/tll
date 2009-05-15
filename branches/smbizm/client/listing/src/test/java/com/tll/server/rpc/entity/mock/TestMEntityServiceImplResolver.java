/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.server.rpc.entity.mock;

import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.model.Address;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.rpc.entity.EntityTypeUtil;
import com.tll.server.rpc.entity.IMEntityServiceImpl;
import com.tll.server.rpc.entity.IMEntityServiceImplResolver;

/**
 * TestMEntityServiceImplResolver
 * @author jpk
 */
public class TestMEntityServiceImplResolver implements IMEntityServiceImplResolver {

	@Override
	public Class<? extends IMEntityServiceImpl<? extends IEntity>> resolve(IModelRelatedRequest request)
			throws IllegalArgumentException {
		if(request instanceof IEntityTypeProvider) {
			final Class<? extends IEntity> entityClass =
					EntityTypeUtil.getEntityClass(((IEntityTypeProvider) request).getEntityType());
			final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

			if(Address.class.isAssignableFrom(rootEntityClass)) {
				return TestAddressService.class;
			}
		}

		throw new IllegalArgumentException("Unhandled request: " + request.descriptor());
	}
}
