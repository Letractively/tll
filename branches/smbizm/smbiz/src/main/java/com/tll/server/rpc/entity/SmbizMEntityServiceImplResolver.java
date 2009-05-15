/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Authority;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.Interface;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.User;

/**
 * SmbizMEntityServiceImplResolver
 * @author jpk
 */
public class SmbizMEntityServiceImplResolver implements IMEntityServiceImplResolver {

	@Override
	public Class<? extends IMEntityServiceImpl<? extends IEntity>> resolve(IModelRelatedRequest request)
			throws IllegalArgumentException {
		if(request instanceof IEntityTypeProvider) {
			final Class<? extends IEntity> entityClass =
				EntityTypeUtil.getEntityClass(((IEntityTypeProvider) request).getEntityType());
			final Class<? extends IEntity> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

			if(User.class.isAssignableFrom(rootEntityClass)) {
				return UserService.class;
			}
			else if(Account.class.isAssignableFrom(rootEntityClass)) {
				return AccountService.class;
			}
			else if(Address.class.isAssignableFrom(rootEntityClass)) {
				return AddressService.class;
			}
			else if(AccountAddress.class.isAssignableFrom(rootEntityClass)) {
				return AccountAddressService.class;
			}
			else if(Authority.class.isAssignableFrom(rootEntityClass)) {
				return AuthorityService.class;
			}
			else if(Interface.class.isAssignableFrom(rootEntityClass)) {
				return InterfaceService.class;
			}
			else if(InterfaceOptionAccount.class.isAssignableFrom(rootEntityClass)) {
				return InterfaceOptionAccountService.class;
			}
		}
		else {
			// non entity type having request
		}

		throw new IllegalArgumentException("Unhandled request: " + request.descriptor());
	}
}
