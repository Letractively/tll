/**
 * The Logic Lab
 * @author jpk
 * Feb 1, 2009
 */
package com.tll.model.key;

import java.util.Collection;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;

import com.tll.model.IEntity;


/**
 * BusinessKeyUtil
 * @author jpk
 */
public final class BusinessKeyUtil {

	/**
	 * Constructor
	 */
	private BusinessKeyUtil() {
		super();
	}

	/**
	 * Updates the the given entity with the property values held in the given
	 * business keys.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bk The business key whose state is applied to the given entity.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 */
	public static <E extends IEntity> void apply(E entity, IBusinessKey<E> bk) throws BusinessKeyPropertyException {
		BeanWrapperImpl bw = new BeanWrapperImpl(entity);
		for(String pname : bk.getPropertyNames()) {
			try {
				bw.setPropertyValue(pname, bk.getPropertyValue(pname));
			}
			catch(NullValueInNestedPathException e) {
				throw new BusinessKeyPropertyException(entity.entityClass(), bk.getBusinessKeyName(), e.getPropertyName());
			}
		}
	}

	/**
	 * Updates the the given entity with the property values held in the given
	 * business keys.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bks The business keys whose state is applied to the given entity.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 */
	public static <E extends IEntity> void apply(E entity, IBusinessKey<E>[] bks) throws BusinessKeyPropertyException {
		for(IBusinessKey<E> bk : bks) {
			apply(entity, bk);
		}
	}

	/**
	 * Compares an entities' state to that of an {@link IBusinessKey} instance.
	 * @param <E>
	 * @param entity
	 * @param bk
	 * @return true/false
	 */
	public static <E extends IEntity> boolean equals(E entity, IBusinessKey<E> bk) {
		if(entity != null && bk != null) {
			try {
				return bk.equals(BusinessKeyFactory.create(entity, bk));
			}
			catch(BusinessKeyNotDefinedException e) {
				// shouldn't happen
				throw new IllegalArgumentException("No business keys defined for: " + entity.typeDesc()
						+ " yet a business key of that type was provided.");
			}
			catch(BusinessKeyPropertyException e) {
				// woops
			}
		}
		return false;
	}

	/**
	 * Ensures all entities w/in the collection are unique against oneanother
	 * based on the defined business keys for corresponding the entity type.
	 * @param <E> The entity type
	 * @param clctn The entity collection. May not be <code>null</code>.
	 * @throws BusinessKeyPropertyException When a business key property is unable
	 *         to be set.
	 * @throws NonUniqueBusinessKeyException When the collection is found to be
	 *         non-unique by the defined business keys for the collection entity
	 *         type.
	 */
	public static <E extends IEntity> void isBusinessKeyUnique(Collection<E> clctn)
			throws BusinessKeyPropertyException, NonUniqueBusinessKeyException {
		if(clctn != null && clctn.size() > 1) {
			try {
				for(E e : clctn) {
					IBusinessKey<E>[] bks = BusinessKeyFactory.create(e);
					for(IBusinessKey<E> bk : bks) {
						for(E e2 : clctn) {
							if(e != e2) {
								IBusinessKey<E>[] otherBks = BusinessKeyFactory.create(e2);
								for(IBusinessKey<E> bk2 : otherBks) {
									if(bk2.equals(bk)) {
										throw new NonUniqueBusinessKeyException(bk);
									}
								}
							}
						}
					}
				}
			}
			catch(BusinessKeyNotDefinedException bknde) {
				// ok
			}
		}
	}

}
