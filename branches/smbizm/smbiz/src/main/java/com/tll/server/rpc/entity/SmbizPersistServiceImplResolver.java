/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.google.inject.Inject;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.ListingRequest;
import com.tll.common.model.IEntityType;
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
 * SmbizPersistServiceImplResolver
 * @author jpk
 */
public class SmbizPersistServiceImplResolver implements IPersistServiceImplResolver {

	private final IEntityTypeResolver etResolver;

	/**
	 * Constructor
	 * @param etResolver
	 */
	@Inject
	public SmbizPersistServiceImplResolver(IEntityTypeResolver etResolver) {
		super();
		this.etResolver = etResolver;
	}

	@Override
	public Class<? extends IPersistServiceImpl<? extends IEntity>> resolve(IModelRelatedRequest request)
			throws IllegalArgumentException {

		if(request instanceof IEntityTypeProvider) {
			return resolve(((IEntityTypeProvider) request).getEntityType());
		}

		// listing request?
		if(request instanceof ListingRequest) {
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
	private Class<? extends IPersistServiceImpl<? extends IEntity>> resolve(IEntityType etype)
			throws IllegalArgumentException {
		final Class<? extends IEntity> entityClass = (Class<? extends IEntity>) etResolver.resolveEntityClass(etype);
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
		throw new IllegalArgumentException("Unhandled entity type: " + etype);
	}
}
