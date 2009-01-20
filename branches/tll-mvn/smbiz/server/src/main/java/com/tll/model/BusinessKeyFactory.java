/**
 * The Logic Lab
 * @author jpk
 * Jun 26, 2008
 */
package com.tll.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import com.tll.model.key.BusinessKey;
import com.tll.model.key.IBusinessKeyDefinition;
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
	private static final Map<Class<? extends IEntity>, Set<BusinessKeyDefinition>> map =
			new HashMap<Class<? extends IEntity>, Set<BusinessKeyDefinition>>();

	/**
	 * Interrogates an entity class' annotations creating a set of
	 * {@link BusinessKeyDefinition}s.
	 * @param <E>
	 * @param entityClass
	 * @return Set of bk definitions or empty set if no business keys defined.
	 */
	private static <E extends IEntity> Set<BusinessKeyDefinition> discoverBusinessKeys(Class<E> entityClass) {
		Set<BusinessKeyDefinition> set = new HashSet<BusinessKeyDefinition>();
		BusinessObject bo = entityClass.getAnnotation(BusinessObject.class);
		if(bo != null) {
			for(BusinessKeyDef def : bo.businessKeys()) {
				set.add(new BusinessKeyDefinition<E>(entityClass, def.name(), def.properties()));
			}
		}
		return set;
	}

	/**
	 * Provides all defined business key definitions for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return All defined business key definitions
	 * @throws BusinessKeyNotDefinedException Whe no business keys are defined for
	 *         the given entity type.
	 */
	public static <E extends IEntity> BusinessKeyDefinition<E>[] definitions(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {

		Set<BusinessKeyDefinition> set = map.get(entityClass);

		if(set == null) {
			// generate business key defs for this entity type
			if((set = discoverBusinessKeys(entityClass)) == null) {
				throw new BusinessKeyNotDefinedException(entityClass);
			}
			map.put(entityClass, set);
		}

		return set.toArray(new BusinessKeyDefinition[set.size()]);
	}

	/**
	 * Creates new and empty business keys for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return Array of empty business keys.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> BusinessKey<E>[] create(Class<E> entityClass) throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		BusinessKey<E>[] bks = new BusinessKey[defs.length];
		for(int i = 0; i < defs.length; ++i) {
			bks[i] = new BusinessKey<E>(defs[i]);
		}
		return bks;
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
	public static <E extends IEntity> BusinessKey<E> create(Class<E> entityClass, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions(entityClass);
		IBusinessKeyDefinition<E> theDef = null;
		for(IBusinessKeyDefinition<E> def : defs) {
			if(def.getBusinessKeyName().equals(businessKeyName)) {
				theDef = def;
				break;
			}
		}
		if(theDef == null) {
			throw new BusinessKeyNotDefinedException(entityClass, businessKeyName);
		}
		return new BusinessKey<E>(theDef);
	}

	/**
	 * Creates all defined business keys with state extracted from the given
	 * entity.
	 * @param <E> The entity type
	 * @param entity The entity from which business keys are created
	 * @return Array of business keys with state extracted from the entity.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	public static <E extends IEntity> BusinessKey<E>[] create(E entity) throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions((Class<E>) entity.entityClass());
		BusinessKey<E>[] bks = new BusinessKey[defs.length];
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
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	public static <E extends IEntity> BusinessKey<E> create(E entity, String businessKeyName)
			throws BusinessKeyNotDefinedException {
		IBusinessKeyDefinition<E>[] defs = definitions((Class<E>) entity.entityClass());
		IBusinessKeyDefinition<E> theDef = null;
		for(IBusinessKeyDefinition<E> def : defs) {
			if(def.getBusinessKeyName().equals(businessKeyName)) {
				theDef = def;
				break;
			}
		}
		if(theDef == null) {
			throw new BusinessKeyNotDefinedException(entity.entityClass(), businessKeyName);
		}
		BusinessKey<E>[] bks = new BusinessKey[] { new BusinessKey(theDef) };
		fill(entity, bks);
		return bks[0];
	}

	/**
	 * Updates the the given entity with the property values held in the given
	 * business keys.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bks The business keys whose state is applied to the given entity.
	 */
	public static <E extends IEntity> void apply(E entity, BusinessKey<E>[] bks) {
		BeanWrapperImpl bw = new BeanWrapperImpl(entity);
		for(BusinessKey<E> bk : bks) {
			for(String pname : bk.getPropertyNames()) {
				try {
					bw.setPropertyValue(pname, bk.getPropertyValue(pname));
				}
				catch(NullValueInNestedPathException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Fills the given business keys with the state of the given entity that type
	 * matches.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bks The business keys for the entity type that are to be filled by
	 *        the values held in the given entity.
	 */
	public static <E extends IEntity> void fill(E entity, BusinessKey<E>[] bks) {
		BeanWrapperImpl bw = new BeanWrapperImpl(entity);
		for(BusinessKey<E> bk : bks) {
			for(String pname : bk.getPropertyNames()) {
				try {
					bk.setPropertyValue(pname, bw.getPropertyValue(pname));
				}
				catch(NullValueInNestedPathException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Ensures all entities w/in the collection are unique against oneanother
	 * based on the defined business keys for corresponding the entity type.
	 * @param <E> The entity type
	 * @param clctn The entity collection. May not be <code>null</code>.
	 * @return <code>true</code> if the entity collection elements are unique
	 *         against oneanother.
	 */
	public static <E extends IEntity> boolean isBusinessKeyUnique(Collection<E> clctn) {
		assert clctn != null;
		if(clctn.size() < 2) {
			return true;
		}
		try {
			for(E e : clctn) {
				BusinessKey[] bks = create(e);
				for(BusinessKey bk : bks) {
					for(E e2 : clctn) {
						if(e != e2) {
							BusinessKey[] otherBks = create(e2);
							for(BusinessKey bk2 : otherBks) {
								if(bk2.getBusinessKeyName().equals(bk.getBusinessKeyName()) && bk2.equals(bk)) {
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
}
