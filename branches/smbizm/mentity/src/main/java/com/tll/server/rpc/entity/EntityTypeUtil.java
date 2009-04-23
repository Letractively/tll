/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;
import com.tll.common.model.MutableEntityType;
import com.tll.model.IEntity;
import com.tll.util.StringUtil;


/**
 * EntityTypeUtil
 * @author jpk
 */
public abstract class EntityTypeUtil {

	/**
	 * Factory method for creating an {@link IEntityType} instance given and
	 * entity class.
	 * @param entityClass The entity class
	 * @return A new {@link IEntityType} instance
	 */
	public static IEntityType getEntityType(Class<? extends IEntity> entityClass) {
		return new MutableEntityType(entityClass.getName(), StringUtil.camelCaseToPresentation(entityClass.getSimpleName()));
	}

	/**
	 * Converts an {@link IEntityType} to an entity {@link Class}.
	 * @param entityType
	 * @return The associated java {@link Class}.
	 * @throws IllegalArgumentException When the given entity type can't be
	 *         resolved to a known class that derives from {@link IEntity}.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends IEntity> getEntityClass(IEntityType entityType) throws IllegalArgumentException {
		try {
			final Class<?> clz = Class.forName(entityType.getEntityClassName());
			if(!IEntity.class.isAssignableFrom(clz)) {
				throw new IllegalArgumentException("The translated class does not derive from IEntity.");
			}
			return (Class<? extends IEntity>) clz;
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalArgumentException("Can't resolve the entity type's entity class name: "
					+ entityType.getEntityClassName());
		}
	}
}
