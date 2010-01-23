package com.tll.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.BusinessKeyNotDefinedException;
import com.tll.model.bk.IBusinessKeyDefinition;

/**
 * EntityBeanFactory - Provides prototype entity instances via a Spring bean
 * context.
 * @author jpk
 */
public final class EntityBeanFactory {

	/**
	 * The default path file name of the [Spring] bean context file.
	 */
	private static final String DEFAULT_BEAN_DEF_FILENAME = "mock-entities.xml";

	private static final Log log = LogFactory.getLog(EntityBeanFactory.class);

	/**
	 * Use a static counter for created business key wise unique entity copies to
	 * ensure no collisions!
	 */
	private static int uniqueTokenCounter = 0;

	/**
	 * Loads a {@link ListableBeanFactory} given a {@link URI} ref to it.
	 * @param beanDefRef May be <code>null</code> in which case the default bean
	 *        def filename is used
	 * @return newly created {@link ListableBeanFactory} impl instance
	 */
	public static ListableBeanFactory loadBeanDefinitions(URI beanDefRef) {
		if(beanDefRef == null) try {
			beanDefRef = new URI(DEFAULT_BEAN_DEF_FILENAME);
		}
		catch(final URISyntaxException e1) {
			throw new IllegalStateException("Can't locate entity bean def file: " + DEFAULT_BEAN_DEF_FILENAME);
		}

		// NOTE: we revert to the system class loader as opposed to Spring's
		// default current thread context class loader
		// so gmaven invoked groovy scripts don't blow up
		try {
			final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(new String[] {
				beanDefRef.toString()
			}, false);
			ac.setClassLoader(AbstractEntityGraphPopulator.class.getClassLoader());
			ac.refresh();
			return ac;
		}
		catch(final BeanDefinitionStoreException e) {
			// presume the file could't be found at root of classpath
			// so fallback on a file-based class loader
			final FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(new String[] {
				beanDefRef.toString()
			}, false);
			ac.setClassLoader(AbstractEntityGraphPopulator.class.getClassLoader());
			ac.refresh();
			return ac;
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
			bkdefs = BusinessKeyFactory.definitions((Class<E>) e.entityClass());
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
						final String ut = nextUniqueToken();
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
							new Date(((Date) fval).getTime() + (nextUniqueInt() * 10000) + RandomUtils.nextInt(10000)
									+ nextUniqueInt() + 1);
						bw.setPropertyValue(fname, altered);
						entityAltered = true;
						break;
					}
					else if(fval instanceof Float) {
						final Float n = (Float) fval;
						bw.setPropertyValue(fname, n + nextUniqueInt());
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

	private final ListableBeanFactory beanFactory;

	private final IPrimaryKeyGenerator<?> pkGenerator;

	/**
	 * Constructor
	 * @param beanFactory
	 * @param pkGenerator Optional
	 */
	public EntityBeanFactory(ListableBeanFactory beanFactory, IPrimaryKeyGenerator<?> pkGenerator) {
		super();
		if(beanFactory == null) throw new IllegalArgumentException("The beanFactory is null");
		this.beanFactory = beanFactory;
		this.pkGenerator = pkGenerator;
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> E[] getBeansOfType(Class<E> type) {
		final Map<String, E> map = beanFactory.getBeansOfType(type);
		if(map == null) return null;
		return (E[]) map.values().toArray(new IEntity[map.size()]);
	}

	private <E extends IEntity> E getBean(Class<E> type) {
		final E[] arr = getBeansOfType(type);
		return (arr == null || arr.length == 0) ? null : arr[0];
	}

	/**
	 * Gets all entity copies held in the factory of the given type.
	 * <p>
	 * NOTE: the copies are un-altered in terms of business key uniqueness.
	 * @param <E>
	 * @param entityClass
	 * @return Set of entity copies
	 */
	public <E extends IEntity> Set<E> getAllEntityCopies(Class<E> entityClass) {
		final Set<E> set = new LinkedHashSet<E>();
		final E[] arr = getBeansOfType(entityClass);
		if(arr != null && arr.length > 0) {
			for(final E e : arr) {
				if(pkGenerator != null) {
					e.setPrimaryKey(pkGenerator.generateIdentifier(e.entityClass()));
				}
				set.add(e);
			}
		}
		return set;
	}

	/**
	 * Gets an entity copy by type.
	 * @param <E>
	 * @param entityClass
	 * @param makeUnique Attempt to make the copied entity business key unique?
	 * @return A fresh entity copy or <code>null</code> if there are no instances
	 *         present having the given entity type (class).
	 */
	public <E extends IEntity> E getEntityCopy(Class<E> entityClass, boolean makeUnique) {
		final E e = getBean(entityClass);
		if(e != null) {
			if(pkGenerator != null) {
				e.setPrimaryKey(pkGenerator.generateIdentifier(entityClass));
			}
			if(makeUnique) {
				makeBusinessKeyUnique(e);
			}
		}
		log.debug("Entity copy created: " + e);
		return e;
	}

	/**
	 * Generates a specified number of entity instances of a particular type.
	 * @param <E>
	 * @param entityClass the desired entity type
	 * @param n The number of copies to provide
	 * @param makeUnique Attempt to make the copied entities business key unique?
	 * @return Set of <code>n</code> entity copies of the given type that may be
	 *         business key unique or an empty set of no entities of the given
	 *         type exist.
	 */
	public <E extends IEntity> Set<E> getNEntityCopies(Class<E> entityClass, int n, boolean makeUnique) {
		final Set<E> set = new LinkedHashSet<E>(n);
		for(int i = 0; i < n; i++) {
			final E e = getEntityCopy(entityClass, makeUnique);
			if(e != null) set.add(e);
		}
		return set;
	}
}
