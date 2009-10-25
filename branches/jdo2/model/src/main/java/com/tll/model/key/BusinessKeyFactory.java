/**
 * The Logic Lab
 * @author jpk
 * Jun 26, 2008
 */
package com.tll.model.key;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * BusinessKeyFactory - Defines all entity business keys in the application and
 * provides utility methods relating to them.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public final class BusinessKeyFactory {

	/**
	 * BusinessKeyDefinition - Local impl of {@link IBusinessKeyDefinition}.
	 * @author jpk
	 * @param <E>
	 */
	private static final class BusinessKeyDefinition<E extends IEntity> implements IBusinessKeyDefinition<E> {

		private final Class<E> entityClass;
		private final String[] propertyNames;
		private final String businessKeyName;

		public BusinessKeyDefinition(Class<E> entityClass, String businessKeyName, String[] propertyNames) {
			if(entityClass == null) throw new IllegalArgumentException("An entity type must be specified.");
			if(propertyNames == null || propertyNames.length < 1) {
				throw new IllegalArgumentException("At least one property must be specified in a business key");
			}
			this.entityClass = entityClass;
			this.propertyNames = propertyNames;
			this.businessKeyName = businessKeyName;
		}

		public Class<E> getType() {
			return entityClass;
		}

		public String getBusinessKeyName() {
			return businessKeyName;
		}

		public String[] getPropertyNames() {
			return propertyNames;
		}
	}

	/**
	 * The cache of discovered business key definitions.
	 */
	private static final Map<Class<? extends IEntity>, Set<IBusinessKeyDefinition<? extends IEntity>>> map =
			new HashMap<Class<? extends IEntity>, Set<IBusinessKeyDefinition<? extends IEntity>>>();

	/**
	 * Interrogates an entity class' annotations creating a set of
	 * {@link BusinessKeyDefinition}s.
	 * @param <E>
	 * @param entityClass
	 * @return Set of bk definitions or <code>null</code> if no business keys are
	 *         defined for the given entity class.
	 */
	private static <E extends IEntity> Set<IBusinessKeyDefinition<E>> discoverBusinessKeys(Class<E> entityClass) {
		BusinessObject bo = entityClass.getAnnotation(BusinessObject.class);
		if(bo == null) {
			// try the root entity
			bo = EntityUtil.getRootEntityClass(entityClass).getAnnotation(BusinessObject.class);
		}
		if(bo == null) {
			// no bks defined
			return null;
		}
		final Set<IBusinessKeyDefinition<E>> set = new HashSet<IBusinessKeyDefinition<E>>();
		for(final BusinessKeyDef def : bo.businessKeys()) {
			set.add(new BusinessKeyDefinition<E>(entityClass, def.name(), def.properties()));
		}
		return set;
	}
	
	/**
	 * Does the given entity type have any defined business keys?
	 * @param <E>
	 * @param entityClass
	 * @return true/false
	 */
	public static <E extends IEntity> boolean hasBusinessKeys(Class<E> entityClass) {
		try {
			definitions(entityClass);
			return true;
		}
		catch(final BusinessKeyNotDefinedException e) {
			return false;
		}
	}

	/**
	 * Provides all defined business key definitions for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return All defined business key definitions
	 * @throws BusinessKeyNotDefinedException Whe no business keys are defined for
	 *         the given entity type.
	 */
	public static <E extends IEntity> IBusinessKeyDefinition<E>[] definitions(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {

		if(!map.containsKey(entityClass)) {
			map.put(entityClass, (Set) discoverBusinessKeys(entityClass));
		}

		final Set set = map.get(entityClass);
		if(set == null) {
			throw new BusinessKeyNotDefinedException(entityClass);
		}
		
		return (IBusinessKeyDefinition<E>[]) set.toArray(new IBusinessKeyDefinition[set.size()]);
	}

	/**
	 * Resolves a business key name to its definition for a given entity type.
	 * @param <E>
	 * @param entityClass
	 * @param businessKeyName
	 * @return The found definition or <code>null</code> if none exist for the
	 *         given name of the given entity type.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> IBusinessKeyDefinition<E> getDefinition(Class<E> entityClass, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		final IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		for(final IBusinessKeyDefinition<E> def : defs) {
			if(def.getBusinessKeyName().equals(businessKeyName)) {
				return def;
			}
		}
		return null;
	}

	/**
	 * Creates new and empty business keys for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return Array of empty business keys.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> IBusinessKey<E>[] create(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {
		final IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		final BusinessKey<E>[] bks = new BusinessKey[defs.length];
		for(int i = 0; i < defs.length; ++i) {
			bks[i] = new BusinessKey<E>(defs[i]);
		}
		return bks;
	}

	/**
	 * Creates a new and empty business key for the given entity type and business
	 * key name.
	 * @param <E> The entity type.
	 * @param def The business key definition
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	public static <E extends IEntity> IBusinessKey<E> create(IBusinessKeyDefinition<E> def)
			throws BusinessKeyNotDefinedException {
		return new BusinessKey<E>(def);
	}

	/**
	 * Creates a new and empty business key for the given entity type and business
	 * key name.
	 * @param <E> The entity type.
	 * @param entityClass The entity class
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	public static <E extends IEntity> IBusinessKey<E> create(Class<E> entityClass, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		return create(getDefinition(entityClass, businessKeyName));
	}

	/**
	 * Creates all defined business keys with state extracted from the given
	 * entity.
	 * @param <E> The entity type
	 * @param entity The entity from which business keys are created
	 * @return Array of business keys with state extracted from the entity.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 */
	public static <E extends IEntity> IBusinessKey<E>[] create(E entity) throws BusinessKeyNotDefinedException,
			BusinessKeyPropertyException {
		final IBusinessKeyDefinition<E>[] defs = definitions((Class<E>) entity.entityClass());
		final BusinessKey<E>[] bks = new BusinessKey[defs.length];
		for(int i = 0; i < defs.length; ++i) {
			bks[i] = new BusinessKey<E>(defs[i]);
		}
		fill(entity, bks);
		return bks;
	}

	/**
	 * Creates a single business key for the given entity type and business key
	 * name.
	 * @param <E> The entity type.
	 * @param entity The entity instance
	 * @param def The business key definition
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 */
	public static <E extends IEntity> IBusinessKey<E> create(E entity, IBusinessKeyDefinition<E> def)
			throws BusinessKeyNotDefinedException, BusinessKeyPropertyException {
		final IBusinessKey<E> bk = create(def);
		fill(new BeanWrapperImpl(entity), bk);
		return bk;
	}

	/**
	 * Creates a single business key for the given entity type and business key
	 * name.
	 * @param <E> The entity type.
	 * @param entity The entity instance
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 */
	public static <E extends IEntity> BusinessKey<E> create(E entity, String businessKeyName)
			throws BusinessKeyNotDefinedException, BusinessKeyPropertyException {
		final IBusinessKeyDefinition<E> theDef = getDefinition((Class<E>) entity.entityClass(), businessKeyName);
		final BusinessKey<E>[] bks = new BusinessKey[] { new BusinessKey(theDef) };
		fill(entity, bks);
		return bks[0];
	}
	
	/**
	 * Fills the given business key with values held in the given entity.
	 * @param <E> The entity type
	 * @param entity The entity whose state is applied to the given business keys.
	 * @param bks The business keys.
	 */
	private static <E extends IEntity> void fill(E entity, IBusinessKey<E>[] bks) {
		final BeanWrapperImpl wrappedEntity = new BeanWrapperImpl(entity);
		for(final IBusinessKey<E> bk : bks) {
			fill(wrappedEntity, bk);
		}
	}

	/**
	 * Fills the given business key with values held in the given entity.
	 * @param <E> The entity type
	 * @param wrappedEntity The entity whose state is applied to the given
	 *        business key.
	 * @param bk The business key.
	 */
	private static <E extends IEntity> void fill(BeanWrapperImpl wrappedEntity, IBusinessKey<E> bk) {
		for(final String pname : bk.getPropertyNames()) {
			try {
				bk.setPropertyValue(pname, wrappedEntity.getPropertyValue(pname));
			}
			catch(final NullValueInNestedPathException e) {
				// ok
			}
		}
	}
}
