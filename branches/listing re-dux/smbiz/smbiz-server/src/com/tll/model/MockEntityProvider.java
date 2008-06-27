package com.tll.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.ListableBeanFactory;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.tll.model.key.BusinessKey;

/**
 * MockEntityProvider - Provides prototype entity instances via a Spring bean
 * context.
 * @author jpk
 */
public final class MockEntityProvider {

	private static final Log log = LogFactory.getLog(MockEntityProvider.class);

	/**
	 * Use a static counter for created business key wise unique entity copies to
	 * ensure no collisions!
	 */
	private static int uniqueTokenCounter = 0;

	/**
	 * MockEntityBeanFactory annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface MockEntityBeanFactory {
	}

	private final ListableBeanFactory beanFactory;

	private final EntityAssembler entityAssembler;

	/**
	 * Constructor
	 * @param beanFactory
	 */
	@Inject
	public MockEntityProvider(@MockEntityBeanFactory
	ListableBeanFactory beanFactory, EntityAssembler entityAssembler) {
		super();
		assert beanFactory != null : "The beanFactory is null";
		assert entityAssembler != null : "The entityAssembler is null";
		this.beanFactory = beanFactory;
		this.entityAssembler = entityAssembler;
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> E[] getBeansOfType(Class<E> type) {
		Map<String, E> map = beanFactory.getBeansOfType(type);
		if(map == null) return null;
		return (E[]) map.values().toArray(new IEntity[map.size()]);
	}

	private <E extends IEntity> E getBean(Class<E> type) {
		E[] arr = getBeansOfType(type);
		return (arr == null || arr.length == 0) ? null : arr[0];
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @return Set of entity copies
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> Set<E> getAllEntityCopies(Class<E> entityClass) throws Exception {
		Set<E> set = new LinkedHashSet<E>();
		E[] arr = getBeansOfType(entityClass);
		if(arr != null && arr.length > 0) {
			for(E e : arr) {
				entityAssembler.setGenerated(e);
				set.add(e);
			}
		}
		return set;
	}

	/**
	 * Gets an entity copy by type.
	 * @param <E>
	 * @param entityClass
	 * @return A fresh entity copy
	 * @throws Exception
	 */
	public <E extends IEntity> E getEntityCopy(Class<E> entityClass) throws Exception {
		E e = getBean(entityClass);
		entityAssembler.setGenerated(e);
		makeBusinessKeyUnique(e);
		return e;
	}

	/**
	 * Generates a specified number of UNIQUE entity copies by applying all
	 * defined business keys to each created entity and altering those properties
	 * slightly.
	 * @param <E>
	 * @param entityClass
	 * @param n The number of copies to provide
	 * @return n unique entity copies
	 * @throws Exception
	 */
	public <E extends IEntity> Set<E> getNEntityCopies(Class<E> entityClass, int n) throws Exception {
		Set<E> set = new LinkedHashSet<E>(n);
		for(int i = 0; i < n; i++) {
			set.add(getEntityCopy(entityClass));
		}
		return set;
	}

	/**
	 * Makes the provided entity [quasi] business key unique by altering one of
	 * the business key field values for all available business keys of the given
	 * entity.
	 * @param e the entity to be altered
	 * @throws BusinessKeyNotDefinedException
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> void makeBusinessKeyUnique(E e) throws BusinessKeyNotDefinedException {
		BusinessKey[] keys = BusinessKeyFactory.create(e);
		final int uniqueNum = ++uniqueTokenCounter;
		String ut = Integer.toString(uniqueNum);
		final BeanWrapperImpl bw = new BeanWrapperImpl(e);
		for(BusinessKey key : keys) {
			boolean entityAlteredByBk = false;
			for(String fname : key.getPropertyNames()) {
				if(fname.endsWith(".id")) continue;
				Object fval = key.getPropertyValue(fname);
				if(fval instanceof String) {
					String sval = fval.toString();
					if(sval.length() > ut.length()) {
						sval = sval.substring(0, sval.length() - ut.length()) + ut;
					}
					else {
						sval += ut;
					}
					bw.setPropertyValue(fname, sval);
					entityAlteredByBk = true;
					break;
				}
				else if(fval instanceof Date) {
					bw.setPropertyValue(fname, new Date((new Date()).getTime() - (uniqueNum * 1000)));
					entityAlteredByBk = true;
					break;
				}
				else if(fval instanceof Float) {
					Float n = (Float) fval;
					bw.setPropertyValue(fname, n + uniqueNum);
					entityAlteredByBk = true;
					break;
				}
			}
			if(!entityAlteredByBk) {
				log.warn(e.descriptor() + " was not altered by BK: " + key.descriptor());
			}
		}
	}

}
