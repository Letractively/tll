/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.server.rpc.entity.mock;

import com.tll.common.search.ISearch;
import com.tll.model.Address;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.rpc.entity.IMEntityServiceImpl;
import com.tll.server.rpc.entity.IMEntityServiceImplResolver;

/**
 * TestMEntityServiceImplResolver
 * @author jpk
 */
public class TestMEntityServiceImplResolver implements IMEntityServiceImplResolver {

	@Override
	public Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> resolveMEntityServiceImpl(
			Class<? extends IEntity> entityClass) throws IllegalArgumentException {
		final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

		if(Address.class.isAssignableFrom(rootEntityClass)) {
			return TestAddressService.class;
		}

		throw new IllegalArgumentException("Unhandled entity type: " + entityClass);
	}
}
