/**
 * The Logic Lab
 * @author jpk
 * Feb 1, 2009
 */
package com.tll.model;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;

import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyNotDefinedException;
import com.tll.model.bk.BusinessKeyPropertyException;
import com.tll.model.bk.IBusinessKeyDefinition;
import com.tll.schema.Extended;
import com.tll.schema.Root;

/**
 * EntityUtil
 * @author jpk
 */
public class EntityUtil {
	
	private static final Log log = LogFactory.getLog(EntityUtil.class);

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

	/**
	 * Compare a clc of entity ids and entites ensuring the id list is referenced
	 * w/in the entity list
	 * @param <E>
	 * @param ids
	 * @param entities
	 * @return true/false
	 */
	public static final <E extends IEntity> boolean entitiesAndIdsEquals(Collection<?> ids, Collection<E> entities) {
		if(ids == null || entities == null) {
			return false;
		}
		if(ids.size() != entities.size()) {
			return false;
		}
		for(final E e : entities) {
			boolean found = false;
			for(final Object id : ids) {
				if(id.equals(e.getId())) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Use a static counter for created business key wise unique entity copies to
	 * ensure no collisions!
	 */
	public static int uniqueTokenCounter = 0;

	/**
	 * Ensures two entities are non-unique by business key.
	 * @param <E>
	 * @param e1
	 * @param e2
	 */
	public static final <E extends IEntity> void ensureNonUnique(E e1, E e2) {
		if(e2 instanceof ITimeStampEntity) {
			((ITimeStampEntity) e2).setDateCreated(((ITimeStampEntity) e2).getDateCreated());
			((ITimeStampEntity) e2).setDateModified(((ITimeStampEntity) e2).getDateModified());
		}
		try {
			BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());
			bkf.apply(e2, bkf.create(e1));
		}
		catch(final BusinessKeyNotDefinedException e) {
			// assume ok
		}
		catch(final BusinessKeyPropertyException e) {
			throw new IllegalStateException("Unable to unique-ify entities: " + e.getMessage(), e);
		}
	}

	/**
	 * Makes the provided entity [quasi] business key unique by altering one of
	 * the business key field values for all available business keys of the given
	 * entity.
	 * @param <E>
	 * @param e the entity to be altered
	 */
	@SuppressWarnings({
		"unchecked", "boxing"
	})
	public static <E extends IEntity> void makeBusinessKeyUnique(E e) {
		IBusinessKeyDefinition<E>[] bkdefs;
		try {
			BusinessKeyFactory bkf = new BusinessKeyFactory(new EntityMetadata());
			bkdefs = bkf.definitions((Class<E>) e.entityClass());
		}
		catch(final BusinessKeyNotDefinedException ex) {
			// ok
			return;
		}
		final String pktoken = '.' + IEntity.PK_FIELDNAME;
		final BeanWrapperImpl bw = new BeanWrapperImpl(e);
		boolean entityAltered = false;
		for(final IBusinessKeyDefinition bkdef : bkdefs) {
			for(final String fname : bkdef.getPropertyNames()) {
				// don't interrogate pk related key properties
				if(!fname.endsWith(pktoken)) {
					final Object fval = bw.getPropertyValue(fname);
					if(fval instanceof String) {
						String sval = fval.toString();
						final String ut = EntityUtil.nextUniqueToken();
						if(sval.length() > ut.length()) {
							sval = sval.substring(0, sval.length() - ut.length()) + ut;
						}
						else {
							sval += ut;
						}
						bw.setPropertyValue(fname, sval);
						entityAltered = true;
						break;
					}
					else if(fval instanceof Date) {
						final Date altered =
							new Date(((Date) fval).getTime() + (EntityUtil.nextUniqueInt() * 10000) + RandomUtils.nextInt(10000)
									+ EntityUtil.nextUniqueInt() + 1);
						bw.setPropertyValue(fname, altered);
						entityAltered = true;
						break;
					}
					else if(fval instanceof Float) {
						final Float n = (Float) fval;
						bw.setPropertyValue(fname, n + EntityUtil.nextUniqueInt());
						entityAltered = true;
						break;
					}
				}
			}
		}
		if(!entityAltered) {
			log.warn(e.descriptor() + " was not made business key unique");
		}
	}

	private static int nextUniqueInt() {
		return ++uniqueTokenCounter;
	}

	private static String nextUniqueToken() {
		return Integer.toString(nextUniqueInt());
	}
}
