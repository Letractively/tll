package com.tll.model.key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.util.CommonUtil;

/**
 * Factory class that generates business keys and primary keys for the application. Keys can be
 * retrieved either by passing in the class or an instance of the class. If a class is passed in, an
 * empty key will be returned. If an object is used, the key will be pre-populated with the correct
 * values from the entity.
 * @author jpk
 */
public final class KeyFactory {

	/**
	 * The business key prototypes: key: biz key class, val: IBusinessKey instance (w/ empty values)
	 */
	private final static Map<Class<? extends IBusinessKey<? extends IEntity>>, IBusinessKey<? extends IEntity>> prototypes = new HashMap<Class<? extends IBusinessKey<? extends IEntity>>, IBusinessKey<? extends IEntity>>();

	static {
		initBusinessKeyPrototypes();
	}

	/**
	 * Construct a {@link NameKey} with empty name and default field name.
	 * @param <N>
	 * @param entityClass
	 * @return The generated name key
	 */
	public static <N extends INamedEntity> INameKey<N> getNameKey(Class<N> entityClass) {
		return getNameKey(entityClass, null, null);
	}

	/**
	 * Construct a {@link NameKey} w/ default field name.
	 * @param <N>
	 * @param entityClass
	 * @param name
	 * @return The generated name key
	 */
	public static <N extends INamedEntity> INameKey<N> getNameKey(Class<N> entityClass, String name) {
		return new NameKey<N>(entityClass, name, null);
	}

	/**
	 * Construct a {@link NameKey}.
	 * @param <N>
	 * @param entityClass
	 * @param name
	 * @param fieldName
	 * @return The generated name key
	 */
	public static <N extends INamedEntity> INameKey<N> getNameKey(Class<N> entityClass, String name, String fieldName) {
		return new NameKey<N>(entityClass, name, fieldName);
	}

	/**
	 * Returns the appropriate primary key based on the input class. The input class <i>must</i> be
	 * an entity within the application.
	 * @param entityClass an entity class
	 * @return the associated primary key
	 */
	public static final <E extends IEntity> IPrimaryKey<E> getPrimaryKey(Class<E> entityClass, Integer id) {
		return new PrimaryKey<E>(entityClass, id);
	}

	/**
	 * Returns the appropriate primary key based on the input entity. This method will pre-populate
	 * the key with the values from the entity.
	 * @param entity the entity
	 * @return the associated primary key
	 */
	public static final <E extends IEntity> IPrimaryKey<E> getPrimaryKey(E entity) {
		return new PrimaryKey<E>(entity);
	}

	/*
	 * business key related
	 */

	/**
	 * Initializes the business key prototype serviceMap
	 */
	@SuppressWarnings("unchecked")
	private static void initBusinessKeyPrototypes() {

		Class<?>[] keyClasses;
		try {
			// NOTE: exclude the NameKey class
			// keyClasses = CommonUtil.getClasses(KeyFactory.class.getPackage().getName(),
			// IBusinessKey.class, true, null);
			keyClasses = CommonUtil.getClasses("com.tll.model.impl.key", IBusinessKey.class, true, null);
		}
		catch(ClassNotFoundException e) {
			keyClasses = null;
		}

		if(keyClasses == null || keyClasses.length < 1)
			throw new IllegalArgumentException("No business key classes specified");

		for(Class<?> keyClass : keyClasses) {
			if(NameKey.class.equals(keyClass)) {
				continue; // skip the NameKey
			}
			IBusinessKey<? extends IEntity> key;
			try {
				Object obj = keyClass.newInstance();
				if(obj instanceof IBusinessKey == false) {
					throw new IllegalStateException("Non-business key class incorrectly declared in BusinessKeyFactory");
				}
				key = (IBusinessKey<? extends IEntity>) obj;
			}
			catch(InstantiationException ie) {
				throw new IllegalStateException("Unable to instantiate business key class '" + keyClass.getName() + "'");
			}
			catch(IllegalAccessException iae) {
				throw new IllegalStateException(
						"Illegal access exception occurred attempting to instantiate business key class '" + keyClass.getName()
								+ "'");
			}

			prototypes.put((Class<? extends IBusinessKey<? extends IEntity>>) keyClass, key);
		}
	}

	/**
	 * Provides a clone of a given business key type
	 * @param <B> business key type
	 * @param keyClass business key class
	 * @return cloned and empty business key of the given type
	 * @throws BusinessKeyNotDefinedException when no prototype key instance exists
	 */
	@SuppressWarnings("unchecked")
	private static <B extends IBusinessKey<? extends IEntity>> B getKeyClone(Class<B> keyClass)
			throws BusinessKeyNotDefinedException {

		if(!prototypes.containsKey(keyClass)) {
			throw new BusinessKeyNotDefinedException(keyClass, false);
		}

		return (B) prototypes.get(keyClass);
	}

	/**
	 * Provides clones of all defined business keys for the given entity type
	 * @param <E> entity type
	 * @return cloned and empty business key of the given type
	 * @throws BusinessKeyNotDefinedException when no prototype key instance exists
	 */
	@SuppressWarnings("unchecked")
	private static <E extends IEntity> List<IBusinessKey<E>> getKeyClones(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {

		List<IBusinessKey<E>> list = new ArrayList<IBusinessKey<E>>();

		for(IBusinessKey<? extends IEntity> key : prototypes.values()) {
			if(key.getType() != null && key.getType().isAssignableFrom(entityClass)) {
				list.add((IBusinessKey<E>) key.copy());
			}
		}

		if(list.isEmpty()) {
			throw new BusinessKeyNotDefinedException(entityClass, true);
		}

		return list;
	}

	/**
	 * Provides a business key definition given a business key class.
	 * @param <D> the business key definition type
	 * @param keyClass the business key class
	 * @return Key definition
	 */
	@SuppressWarnings("unchecked")
	public static <D extends IBusinessKeyDefinition<? extends IEntity>> D getKeyDefinition(Class<D> keyClass) {

		if(!prototypes.containsKey(keyClass)) {
			throw new IllegalArgumentException("No business key of class: '"
					+ (keyClass == null ? "NULL" : keyClass.getName()) + "' exists");
		}

		return (D) prototypes.get(keyClass).copy();
	}

	/**
	 * Provides thes business key definitions for a given entity class [type].
	 * @param <E> the entity type
	 * @param entityClass then entity class
	 * @return array of business key definitions
	 * @throws BusinessKeyNotDefinedException
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> IBusinessKeyDefinition<E>[] getKeyDefinitions(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {
		List<IBusinessKey<E>> list = getKeyClones(entityClass);
		return list.toArray(new IBusinessKeyDefinition[list.size()]);
	}

	/**
	 * Provides a fresh instance of a business key of the desired type <B> the business key type
	 * @param keyClass the business key class
	 * @return unfilled cloned instance of a business key of the given type
	 */
	public static <B extends IBusinessKey<? extends IEntity>> B getBusinessKey(Class<B> keyClass) {
		try {
			return getKeyClone(keyClass);
		}
		catch(BusinessKeyNotDefinedException e) {
			throw new IllegalArgumentException("No business key of class: '"
					+ (keyClass == null ? "NULL" : keyClass.getName()) + "' exists");
		}
	}

	/**
	 * Generates empty business keys for the given entity class
	 * @param <E> the entity type
	 * @param entityClass the entity type
	 * @return array of all defined business keys for the given entity type having empty state
	 * @throws BusinessKeyNotDefinedException
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> IBusinessKey<E>[] getBusinessKeys(Class<E> entityClass)
			throws BusinessKeyNotDefinedException {
		List<IBusinessKey<E>> list = getKeyClones(entityClass);
		return list.toArray(new IBusinessKey[list.size()]);
	}

	/**
	 * Extracts all defined business keys for a particular entity.
	 * @param <E> the entity type
	 * @param entity the entity
	 * @return array of business keys for the given entity type whose state is that of the entities'
	 * @throws BusinessKeyNotDefinedException when no business keys are defined for the given entity
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> IBusinessKey<E>[] getBusinessKeys(E entity) throws BusinessKeyNotDefinedException {
		IBusinessKey<E>[] bks = getBusinessKeys((Class<E>) entity.entityClass());
		BeanWrapper bw = new BeanWrapperImpl(entity);
		for(IBusinessKey<E> bk : bks) {
			String[] fields = bk.getFieldNames();
			for(String field : fields) {
				Object val;
				try {
					val = bw.getPropertyValue(field);
				}
				catch(NullValueInNestedPathException nvinpe) {
					val = null;
				}
				bk.setFieldValue(field, val);
			}
		}
		return bks;
	}

	/**
	 * Fills the provided business key with values gotten from the provided entity.
	 * @param <E>
	 * @param key
	 * @param entity
	 */
	public static <E extends IEntity> void setBusinessKey(IBusinessKey<E> key, E entity) {
		BeanWrapper bw = new BeanWrapperImpl(entity);
		String[] fields = key.getFieldNames();
		for(String field : fields) {
			Object val;
			try {
				val = bw.getPropertyValue(field);
			}
			catch(NullValueInNestedPathException nvinpe) {
				val = null;
			}
			key.setFieldValue(field, val);
		}
	}

	/**
	 * Do business keys exist for the given entity type?
	 * @param entityClass the entity class
	 * @return true if at least one business key definition exists for the given entity class
	 */
	@SuppressWarnings("unchecked")
	public static boolean businessKeysExist(Class<? extends IEntity> entityClass) {
		for(IBusinessKey bk : prototypes.values()) {
			if(bk.getType() != null && bk.getType().isAssignableFrom(entityClass)) {
				return true;
			}
		}

		return false;
	}

}