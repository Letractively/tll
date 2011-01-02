/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.google.inject.Inject;
import com.tll.common.data.IModelRelatedRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.rpc.ListingRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.common.model.IEntityTypeResolver;
import com.tll.common.search.AccountInterfaceDataSearch;
import com.tll.common.search.ISearch;
import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.Address;
import com.tll.model.Authority;
import com.tll.model.CustomerAccount;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.Interface;
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
	public Class<? extends IPersistServiceImpl> resolve(IModelRelatedRequest request)
	throws IllegalArgumentException {

		if(request instanceof IEntityTypeProvider) {
			return resolve(((IEntityTypeProvider) request).getEntityType());
		}

		if(request instanceof LoadRequest<?>) {
			return resolve(((LoadRequest<?>) request).getSearch());
		}

		if(request instanceof PersistRequest) {
			return resolve(((PersistRequest) request).getModel().getEntityType());
		}

		if(request instanceof PurgeRequest) {
			final PurgeRequest pr = (PurgeRequest) request;
			if(pr.getEntityRef() != null) {
				return resolve(pr.getEntityRef().getEntityType());
			}
			return resolve(pr.getModel().getEntityType());
		}

		// listing request?
		if(request instanceof ListingRequest) {
			try {
				return resolve(((ListingRequest) request).getListingDef().getSearchCriteria().getEntityType());
			}
			catch(final NullPointerException e) {
				// fall through
			}
		}

		// unhandled
		throw new IllegalArgumentException("Unhandled request: " + request.descriptor());
	}

	private Class<? extends IPersistServiceImpl> resolve(ISearch search)
	throws IllegalArgumentException {

		if(IEntityTypeProvider.class.isAssignableFrom(search.getClass())) {
			return resolve(((IEntityTypeProvider) search).getEntityType());
		}
		else if(search instanceof AccountInterfaceDataSearch) {
			return AccountInterfaceService.class;
		}

		throw new IllegalArgumentException("Unhandled search type: " + search);

	}

	@SuppressWarnings("unchecked")
	private Class<? extends IPersistServiceImpl> resolve(IEntityType etype)
	throws IllegalArgumentException {
		final Class<? extends IEntity> entityClass = (Class<? extends IEntity>) etResolver.resolveEntityClass(etype);
		final Class<?> rootEntityClass = EntityUtil.getRootEntityClass(entityClass);

		if(User.class.isAssignableFrom(rootEntityClass)) {
			return UserService.class;
		}
		else if(Account.class.isAssignableFrom(rootEntityClass)) {
			return AccountService.class;
		}
		else if(CustomerAccount.class.isAssignableFrom(rootEntityClass)) {
			return CustomerAccountService.class;
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
		else if(AccountInterface.class.isAssignableFrom(rootEntityClass)) {
			return AccountInterfaceService.class;
		}
		throw new IllegalArgumentException("Unhandled entity type: " + etype);
	}
}
