/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;

import com.tll.SystemError;
import com.tll.common.search.ISearch;
import com.tll.model.EntityType;
import com.tll.model.EntityTypeUtil;
import com.tll.model.IEntity;
import com.tll.server.rpc.entity.impl.AccountAddressService;
import com.tll.server.rpc.entity.impl.AccountService;
import com.tll.server.rpc.entity.impl.AddressService;
import com.tll.server.rpc.entity.impl.AuthorityService;
import com.tll.server.rpc.entity.impl.InterfaceService;
import com.tll.server.rpc.entity.impl.UserService;

/**
 * MEntityServiceImplFactory - Provides {@link IMEntityServiceImpl}
 * implementation instances. These impls are cached on an on demand basis.
 * @author jpk
 */
public final class MEntityServiceImplFactory {

	private static final Map<Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>>, IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> map =
			new HashMap<Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>>, IMEntityServiceImpl<? extends IEntity, ? extends ISearch>>();

	/**
	 * Returns the {@link IMEntityServiceImpl} instance for the given entity
	 * {@link Class}.
	 * @param entityClass The entity {@link Class}
	 * @return the supporting entity service
	 * @throws SystemError When no {@link IMEntityServiceImpl} implementation is
	 *         found or an service instantiation related exception occurrs.
	 */
	public static IMEntityServiceImpl<? extends IEntity, ? extends ISearch> instance(Class<? extends IEntity> entityClass) {
		return instance(EntityTypeUtil.entityTypeFromClass(entityClass));
	}

	/**
	 * Returns the {@link IMEntityServiceImpl} instance for the given
	 * {@link EntityType}.
	 * @param entityType The {@link EntityType}
	 * @return the supporting entity service
	 * @throws SystemError When no {@link IMEntityServiceImpl} implementation is
	 *         found or an service instantiation related exception occurrs.
	 */
	@SuppressWarnings("unchecked")
	public static IMEntityServiceImpl<IEntity, ISearch> instance(EntityType entityType) {
		Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> svcType;
		IMEntityServiceImpl<IEntity, ISearch> svc;
		switch(entityType) {
			case ADDRESS:
				svcType = AddressService.class;
				break;
			case ASP:
			case ISP:
			case MERCHANT:
			case CUSTOMER:
			case ACCOUNT:
				svcType = AccountService.class;
				break;
			case ACCOUNT_ADDRESS:
				svcType = AccountAddressService.class;
				break;
			case USER:
				svcType = UserService.class;
				break;
			case AUTHORITY:
				svcType = AuthorityService.class;
				break;
			case INTERFACE:
			case INTERFACE_SINGLE:
			case INTERFACE_SWITCH:
			case INTERFACE_MULTI:
				svcType = InterfaceService.class;
				break;
			default:
				throw new SystemError("Unhandled MEntityServiceImpl entity type: " + entityType);
		}

		svc = (IMEntityServiceImpl<IEntity, ISearch>) map.get(svcType);
		if(svc == null) {
			try {
				svc = (IMEntityServiceImpl<IEntity, ISearch>) svcType.newInstance();
			}
			catch(InstantiationException e) {
				throw new SystemError("Unable to instantiate MEntityService class for entity type: " + entityType, e);
			}
			catch(IllegalAccessException e) {
				throw new SystemError("Unable to access MEntityService class for entity type: " + entityType, e);
			}
			map.put(svcType, svc);
		}
		return svc;
	}

}
