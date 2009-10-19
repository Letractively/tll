/**
 * The Logic Lab
 * @author jpk
 * Feb 1, 2009
 */
package com.tll.model;

import com.tll.model.schema.Extended;
import com.tll.model.schema.Root;

/**
 * EntityUtil
 * @author jpk
 */
public class EntityUtil {

	/**
	 * Obtains the "root" entity class given an entity class by reflection.
	 * <p>
	 * The root entity class is relevant when we have an ORM related inheritance
	 * strategy applied to a family of like entities that extend from a common
	 * abstract base entity class.
	 * <p>
	 * E.g.: Asp, Isp, Merchant and Customer all extend from the Account abstract
	 * entity. Therefore, the root entity is Account.
	 * @param entityClass
	 * @return The root entity.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends IEntity> getRootEntityClass(Class<? extends IEntity> entityClass) {
		if(entityClass.getAnnotation(Extended.class) != null) {
			Class<? extends IEntity> ec = entityClass;
			do {
				ec = (Class<? extends IEntity>) ec.getSuperclass();
			} while(ec != null && ec.getAnnotation(Root.class) == null);
			if(ec != null) return ec;
		}
		return entityClass;
	}
}
