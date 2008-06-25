package com.tll.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

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
	 * MockEntityBeanFactory annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface MockEntityBeanFactory {
	}

	private final BeanFactory beanFactory;

	private final EntityAssembler entityAssembler;

	/**
	 * Constructor
	 * @param beanFactory
	 */
	@Inject
	public MockEntityProvider(@MockEntityBeanFactory
	BeanFactory beanFactory, EntityAssembler entityAssembler) {
		super();
		assert beanFactory != null : "The beanFactory is null";
		assert entityAssembler != null : "The entityAssembler is null";
		this.beanFactory = beanFactory;
		this.entityAssembler = entityAssembler;
	}

	private Object getBean(String name) {
		return beanFactory.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E getEntityCopy(String beanName) {
		return (E) getBean(beanName);
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @param num
	 * @return
	 */
	static <E extends IEntity> String getBeanEntityName(Class<E> entityClass, Integer num) {
		String s = entityClass.getSimpleName();
		if(num != null) {
			s += "." + num.toString();
		}
		return s;
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
		Integer num = null;
		do {
			String name = getBeanEntityName(entityClass, num);
			try {
				E e = (E) getBean(name);
				entityAssembler.setGenerated(e);
				set.add(e);
			}
			catch(NoSuchBeanDefinitionException nsbde) {
				if(num != null) {
					break;
				}
			}
			if(num == null) {
				num = 0;
			}
			num++;
		} while(num < 100);
		return set;
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @return An entity copy
	 * @throws Exception
	 */
	public <E extends IEntity> E getEntityCopy(Class<E> entityClass) throws Exception {
		return getEntityCopy(entityClass, null);
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @param num
	 * @return An entity copy
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> E getEntityCopy(Class<E> entityClass, Integer num) throws Exception {
		E e = null;
		do {
			String name = getBeanEntityName(entityClass, num);
			try {
				e = (E) getBean(name);
				break;
			}
			catch(NoSuchBeanDefinitionException nsbde) {
				if(num == null) {
					num = 0;
				}
				else {
					break;
				}
				num++;
			}
		} while(num < 10);
		if(e == null) {
			throw new Exception("Unable to retrieve mock entity '" + entityClass.getSimpleName());
		}
		entityAssembler.setGenerated(e);
		return e;
	}

	/**
	 * Generates a specified number of entity copies.
	 * @param <E>
	 * @param entityClass
	 * @param n Set of N entity copies
	 * @throws Exception
	 */
	public <E extends IEntity> Set<E> getNEntityCopies(Class<E> entityClass, int n) throws Exception {
		Set<E> set = new LinkedHashSet<E>(n);
		for(int i = 0; i < n; i++) {
			E e = getEntityCopy(entityClass);
			set.add(e);
		}
		return set;
	}

	/**
	 * @param <E>
	 * @param entityClass
	 * @param n
	 * @return Unique entity copy
	 * @throws Exception
	 */
	public <E extends IEntity> E getUniqueEntityCopy(Class<E> entityClass, int n) throws Exception {
		E e = getEntityCopy(entityClass);
		makeBusinessKeyUnique(e, n);
		return e;
	}

	/**
	 * Generates a specified number of UNIQUE entity copies by applying all
	 * defined business keys to each created entity and altering those properties
	 * slightly.
	 * @param <E>
	 * @param entityClass
	 * @param n
	 * @return N unique entity copies
	 * @throws Exception
	 */
	public <E extends IEntity> Set<E> getNUniqueEntityCopies(Class<E> entityClass, int n) throws Exception {
		Set<E> set = new LinkedHashSet<E>(n);
		for(int i = 0; i < n; i++) {
			E e = getEntityCopy(entityClass);
			makeBusinessKeyUnique(e, i);
			set.add(e);
		}
		return set;
	}

	/**
	 * Makes the provided entity [quasi] business key unique by altering one of
	 * the business key field values for all available business keys of the given
	 * entity.
	 * @param e the entity to be altered
	 * @param uniqueTokenNum serves as a suffix on a key field to make the entity
	 *        unique
	 * @throws BusinessKeyNotDefinedException
	 */
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> void makeBusinessKeyUnique(E e, int uniqueTokenNum)
			throws BusinessKeyNotDefinedException {
		BusinessKey[] keys = e.getBusinessKeys();
		String ut = Integer.toString(uniqueTokenNum);
		final BeanWrapperImpl bw = new BeanWrapperImpl(e);
		for(BusinessKey key : keys) {
			boolean entityAlteredByBk = false;
			for(String fname : key.getPropertyNames()) {
				Object fval = key.getFieldValue(fname);
				if(fval instanceof String) {
					bw.setPropertyValue(fname, fval.toString() + ut);
					entityAlteredByBk = true;
					break;
				}
				else if(fval instanceof Date) {
					bw.setPropertyValue(fname, new Date((new Date()).getTime() - (uniqueTokenNum * 1000)));
					entityAlteredByBk = true;
					break;
				}
				else if(fval instanceof Float) {
					Float n = (Float) fval;
					bw.setPropertyValue(fname, n + uniqueTokenNum);
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
