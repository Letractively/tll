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
	 * Obtains the "root" entity class given an entity class by checking for the
	 * occurrence of either {@link Root} or {@link Extended} class level
	 * annotations.
	 * <p>
	 * The root entity class is relevant when we have an ORM related inheritance
	 * strategy applied to a family of like entities that extend from a common
	 * entity class.
	 * <p>
	 * E.g.: Asp, Isp, Merchant and Customer all extend from the Account entity.
	 * Therefore, the root entity is Account.
	 * @param entityClass An entity type to check
	 * @return The root entity type of the given entity type.
	 */
	public static Class<?> getRootEntityClass(Class<?> entityClass) {
		if(entityClass.getAnnotation(Extended.class) != null) {
			Class<?> ec = entityClass;
			do {
				ec = ec.getSuperclass();
			} while(ec != null && ec.getAnnotation(Root.class) == null);
			if(ec != null) return ec;
		}
		return entityClass;
	}
}
