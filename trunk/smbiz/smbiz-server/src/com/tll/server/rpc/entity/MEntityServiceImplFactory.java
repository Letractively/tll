/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;

import com.tll.SystemError;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.server.rpc.entity.impl.AccountAddressService;
import com.tll.server.rpc.entity.impl.AccountService;
import com.tll.server.rpc.entity.impl.AuthorityService;
import com.tll.server.rpc.entity.impl.InterfaceService;
import com.tll.server.rpc.entity.impl.UserService;

/**
 * MEntityServiceImplFactory - Provides {@link IMEntityServiceImpl}
 * implementation instances. These impls are cached on an on demand basis.
 * @author jpk
 */
public final class MEntityServiceImplFactory {

	private static final Map<Class<? extends IMEntityServiceImpl<? extends IEntity>>, IMEntityServiceImpl<? extends IEntity>> map =
			new HashMap<Class<? extends IMEntityServiceImpl<? extends IEntity>>, IMEntityServiceImpl<? extends IEntity>>();

	/**
	 * Returns the {@link IMEntityServiceImpl} instance for the given entity
	 * {@link Class}.
	 * @param entityClass The entity {@link Class}
	 * @throws SystemError When no {@link IMEntityServiceImpl} implementation is
	 *         found or an service instantiation related exception occurrs.
	 */
	public static IMEntityServiceImpl<? extends IEntity> instance(Class<? extends IEntity> entityClass) {
		return instance(EntityUtil.entityTypeFromClass(entityClass));
	}

	/**
	 * Returns the {@link IMEntityServiceImpl} instance for the given
	 * {@link EntityType}.
	 * @param entityType The {@link EntityType}
	 * @throws SystemError When no {@link IMEntityServiceImpl} implementation is
	 *         found or an service instantiation related exception occurrs.
	 */
	public static IMEntityServiceImpl<? extends IEntity> instance(EntityType entityType) {
		Class<? extends IMEntityServiceImpl<? extends IEntity>> svcType;
		IMEntityServiceImpl<? extends IEntity> svc;
		switch(entityType) {
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

		svc = map.get(svcType);
		if(svc == null) {
			try {
				svc = svcType.newInstance();
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
