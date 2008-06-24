/**
 * 
 */
package com.tll.model;

import java.util.Collection;

import com.tll.SystemError;
import com.tll.model.key.BusinessKey;
import com.tll.util.CommonUtil;
import com.tll.util.EnumUtil;
import com.tll.util.StringUtil;

/**
 * {@link IEntity} related utility methods.
 * @author jpk
 */
public final class EntityUtil {

	private static String packageName = "com.tll.model.impl";

	private static Class<? extends IEntity>[] entityClasses = null;

	/**
	 * @return All non-abstract IEntity implementation classes.
	 */
	public static Class<? extends IEntity>[] getEntityClasses() {
		if(entityClasses == null) {
			try {
				entityClasses = CommonUtil.getClasses(packageName, IEntity.class, true, null);
			}
			catch(ClassNotFoundException e) {
				throw new SystemError("Unable to provide entity classes: " + e.getMessage());
			}
		}
		return entityClasses;
	}

	/**
	 * Translates an {@link IEntity} {@link Class} to an {@link EntityType}.
	 * @param clazz The {@link IEntity} {@link Class}.
	 * @return The {@link EntityType}
	 * @throws SystemError upon error.
	 */
	public static <E extends IEntity> EntityType entityTypeFromClass(Class<E> clazz) {
		Class<? super E> c = clazz;
		while(true) {
			try {
				return EnumUtil.fromString(EntityType.class, StringUtil.javaClassNotationToEnumStyle(c.getSimpleName()));
			}
			catch(IllegalArgumentException iae) {
				// try the super
				if((c = c.getSuperclass()) == null) {
					throw new SystemError("Invalid entity class: " + clazz);
				}
			}
		}
	}

	/**
	 * Translates an {@link EntityType} to an {@link IEntity} {@link Class}.
	 * @param entityType The {@link EntityType}
	 * @return Class<E>
	 */
	public static <E extends IEntity> Class<E> entityClassFromType(EntityType entityType) {
		return entityClassFromType(entityType.name());
	}

	/**
	 * Translates an {@link EntityType} string element to an {@link IEntity}
	 * {@link Class}.
	 * @param <E>
	 * @param entityTypeName
	 * @return Class<E>
	 * @throws SystemError upon error.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> Class<E> entityClassFromType(String entityTypeName) {
		try {
			final String fqcn = packageName + "." + StringUtil.enumStyleToJavaClassNotation(entityTypeName);
			return (Class<E>) Class.forName(fqcn);
		}
		catch(ClassNotFoundException e) {
			throw new SystemError("Invalid entity type: " + entityTypeName);
		}
	}

	/**
	 * Get a readable type name.
	 * @param <E>
	 * @param clazz
	 * @return type name
	 */
	public static <E extends IEntity> String typeName(Class<E> clazz) {
		return EnumUtil.name(entityTypeFromClass(clazz));
	}

	/**
	 * Ensures all entities w/in the collection are unique against oneanother
	 * based on the defined business keys for corresponding the entity type.
	 * @param <E> The entity type
	 * @param clctn The entity collection. May not be <code>null</code>.
	 * @return <code>true</code> if the entity collection elements are unique
	 *         against oneanother.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> boolean isBusinessKeyUnique(Collection<E> clctn) {
		assert clctn != null;
		if(clctn.size() < 2) {
			return true;
		}
		try {
			for(E e : clctn) {
				BusinessKey[] keys = e.getBusinessKeys();
				for(BusinessKey key : keys) {
					for(E e2 : clctn) {
						if(e != e2) {
							BusinessKey[] otherKeys = e.getBusinessKeys();
							for(BusinessKey otherKey : otherKeys) {
								if(key.descriptor().equals(otherKey.descriptor()) && key.equals(otherKey)) {
									return false;
								}
							}
						}
					}
				}
			}
			return true;
		}
		catch(BusinessKeyNotDefinedException bknde) {
			// ok
			return true;
		}
	}

	private EntityUtil() {
	}
}
