/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.model;

import com.tll.SystemError;

/**
 * EntityTypeUtil
 * @author jpk
 */
public abstract class EntityTypeUtil {

	private static final String MODEL_PACKAGE_NAME = IEntityType.class.getPackage().getName();

	/**
	 * Translates an {@link IEntityType} to an entity {@link Class}.
	 * @param <E> entity type
	 * @param entityType The {@link IEntityType}
	 * @return Class<E>
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> Class<E> entityClassFromType(IEntityType entityType) {
		try {
			return (Class<E>) Class.forName(MODEL_PACKAGE_NAME + "." + entityType.getEntityClassName());
		}
		catch(ClassNotFoundException e) {
			throw new SystemError("Invalid entity type: " + entityType.getEntityClassName());
		}
	}
}
