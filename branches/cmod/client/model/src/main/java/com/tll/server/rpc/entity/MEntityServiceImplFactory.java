/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;

import com.tll.SystemError;
import com.tll.common.search.ISearch;
import com.tll.model.IEntity;

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
	 * @param entityClass The entity type
	 * @param resolver The {@link IMEntityServiceImplResolver}
	 * @return the supporting entity service
	 * @throws SystemError When no {@link IMEntityServiceImpl} implementation is
	 *         found or a service impl instantiation error occurrs.
	 */
	public static IMEntityServiceImpl<? extends IEntity, ? extends ISearch> instance(Class<? extends IEntity> entityClass, IMEntityServiceImplResolver resolver) {
		Class<? extends IMEntityServiceImpl<? extends IEntity, ? extends ISearch>> svcType;
		IMEntityServiceImpl<? extends IEntity, ? extends ISearch> svc;
		
		svcType = resolver.resolveMEntityServiceImpl(entityClass);
		svc = map.get(svcType);
		if(svc == null) {
			try {
				svc = svcType.newInstance();
			}
			catch(InstantiationException e) {
				throw new SystemError("Unable to instantiate MEntityService class for entity type: " + entityClass, e);
			}
			catch(IllegalAccessException e) {
				throw new SystemError("Unable to access MEntityService class for entity type: " + entityClass, e);
			}
			map.put(svcType, svc);
		}
		return svc;
	}

}
