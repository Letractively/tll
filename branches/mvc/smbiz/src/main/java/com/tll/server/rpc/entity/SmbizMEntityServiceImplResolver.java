/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.search.ISearch;
import com.tll.model.Account;
import com.tll.model.Address;
import com.tll.model.Authority;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.Interface;


/**
 * SmbizMEntityServiceImplResolver
 * @author jpk
 */
public class SmbizMEntityServiceImplResolver implements IMEntityServiceImplResolver {

	@Override
	public Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> resolveMEntityServiceImpl(
			Class<? extends IEntity> entityClass) throws IllegalArgumentException {
		final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

		if(Account.class.isAssignableFrom(rootEntityClass)) {
			return AccountService.class;
		}
		else if(Address.class.isAssignableFrom(rootEntityClass)) {
			return AddressService.class;
		}
		else if(Authority.class.isAssignableFrom(rootEntityClass)) {
			return AuthorityService.class;
		}
		else if(Interface.class.isAssignableFrom(rootEntityClass)) {
			return InterfaceService.class;
		}

		throw new IllegalArgumentException("Unhandled entity type: " + entityClass);
	}
}
