/**
 * The Logic Lab
 * @author jpk
 * Jun 26, 2008
 */
package com.tll.model;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.impl.Account;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * BusinessKeyDefinitionFactory
 * @author jpk
 */
public abstract class BusinessKeyDefinitionFactory {

	private static final Map<Class<? extends IEntity>, IBusinessKeyDefinition[]> map =
			new HashMap<Class<? extends IEntity>, IBusinessKeyDefinition[]>();

	public static final IBusinessKeyDefinition<Account> nameBk =
			new BusinessKeyDefinition<Account>(Account.class, "Name", new String[] { "name" });

	static {
		map.put(Account.class, new IBusinessKeyDefinition[] { new BusinessKeyDefinition<Account>(Account.class, "Name",
				new String[] { "name" }) });
	}

	public static <E extends IEntity> IBusinessKeyDefinition<E>[] businessKeys(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = map.get(entityClass);
		if(defs == null) {
			throw new BusinessKeyNotDefinedException(entityClass);
		}
		return defs;
	}
}
