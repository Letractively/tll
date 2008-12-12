/**
 * The Logic Lab
 */
package com.tll.guice;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.dao.JpaMode;
import com.tll.util.EnumUtil;

/**
 * JpaModule - Uses Spring's transaction management framework.
 * @author jpk
 */
public class JpaModule extends GModule {

	/**
	 * Use Spring transaction management or local JPA transactions.
	 */
	private final JpaMode mode;

	/**
	 * The name of the persistence unit to employ. May be <code>null</code> if
	 * {@link #mode} is set to {@link JpaMode#NONE}.
	 */
	private final String persistenceUnitName;

	/**
	 * Constructor Default to Spring transaction management
	 */
	public JpaModule() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param mode
	 */
	public JpaModule(JpaMode mode) {
		this(mode, null);
	}

	/**
	 * Constructor
	 * @param mode
	 * @param persistenceUnitName If <code>null</code>, the app database name
	 *        is used.
	 */
	public JpaModule(JpaMode mode, String persistenceUnitName) {
		super();

		// resolve the jpa mode
		JpaMode jpaMode;
		try {
			jpaMode =
					(mode == null ? EnumUtil.fromString(JpaMode.class, Config.instance().getString(
							ConfigKeys.JPA_MODE_PARAM.getKey())) : mode);
		}
		catch(final IllegalArgumentException e) {
			jpaMode = JpaMode.NONE;
		}
		this.mode = jpaMode;
		if(log.isInfoEnabled()) log.info("Employing JPA mode: " + this.mode.name());

		if(this.mode != JpaMode.NONE) {
			// resolve the persistence unit name
			if(persistenceUnitName == null) {
				// grab the persistence unit defined in Configuration (if declared)
				persistenceUnitName = Config.instance().getString(ConfigKeys.DB_JPA_PERSISTENCE_UNIT_NAME.getKey());
				if(persistenceUnitName == null) {
					// fallback to the db name property
					persistenceUnitName = Config.instance().getString(ConfigKeys.DB_NAME.getKey());
				}
			}
			assert persistenceUnitName != null : "Can't resolve the jpa persistence unit name";
			this.persistenceUnitName = persistenceUnitName;
			if(log.isInfoEnabled()) log.info("Employing JPA Persistence Unit: " + this.persistenceUnitName);
		}
		else {
			this.persistenceUnitName = null;
		}
	}

	@Override
	protected void configure() {

		switch(mode) {

			case SPRING: {
				final EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);

				bind(EntityManagerFactory.class).toInstance(emf);

				final JpaTransactionManager jpatm = new JpaTransactionManager(emf);

				// required for AspectJ weaving of Spring's @Transactional annotation
				// (must be invoked PRIOR to an @Transactional method call
				AnnotationTransactionAspect.aspectOf().setTransactionManager(jpatm);

				// PlatformTransactionManager
				bind(PlatformTransactionManager.class).toInstance(jpatm);

				// EntityManager
				bind(EntityManager.class).toProvider(new Provider<EntityManager>() {

					public EntityManager get() {
						return SharedEntityManagerCreator.createSharedEntityManager(emf);
					}
				});

			}
				break;

			case LOCAL: {
				new EntityManagerFactoryHolder();
				EntityManagerFactoryHolder.singletonEmFactoryHolder.setEntityManagerFactory(Persistence
						.createEntityManagerFactory(persistenceUnitName));

				bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {

					public EntityManagerFactory get() {
						return EntityManagerFactoryHolder.getCurrentEntityManagerFactory();
					}
				}).in(Scopes.SINGLETON);

				bind(EntityManager.class).toProvider(new Provider<EntityManager>() {

					public EntityManager get() {
						return EntityManagerFactoryHolder.getCurrentEntityManager();
					}
				});
			}
				break;

			case NONE:
				break;

			case MOCK:
				bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {

					public EntityManagerFactory get() {
						return new EntityManagerFactory() {

							public boolean isOpen() {
								return false;
							}

							@SuppressWarnings("unchecked")
							public EntityManager createEntityManager(Map arg0) {
								return new EntityManager() {

									public void setFlushMode(FlushModeType arg0) {
									}

									public void remove(Object arg0) {
									}

									public void refresh(Object arg0) {
									}

									public void persist(Object arg0) {
									}

									public <T> T merge(T arg0) {
										return null;
									}

									public void lock(Object arg0, LockModeType arg1) {
									}

									public void joinTransaction() {
									}

									public boolean isOpen() {
										return false;
									}

									public EntityTransaction getTransaction() {
										return null;
									}

									public <T> T getReference(Class<T> arg0, Object arg1) {
										return null;
									}

									public FlushModeType getFlushMode() {
										return null;
									}

									public Object getDelegate() {
										return null;
									}

									public void flush() {
									}

									public <T> T find(Class<T> arg0, Object arg1) {
										return null;
									}

									public Query createQuery(String arg0) {
										return null;
									}

									public Query createNativeQuery(String arg0, String arg1) {
										return null;
									}

									public Query createNativeQuery(String arg0, Class arg1) {
										return null;
									}

									public Query createNativeQuery(String arg0) {
										return null;
									}

									public Query createNamedQuery(String arg0) {
										return null;
									}

									public boolean contains(Object arg0) {
										return false;
									}

									public void close() {
									}

									public void clear() {
									}
								};
							}

							public EntityManager createEntityManager() {
								return null;
							}

							public void close() {
							}
						};
					}
				}).in(Scopes.SINGLETON);

				bind(EntityManager.class).toProvider(new Provider<EntityManager>() {

					@Inject
					EntityManagerFactory emf;

					public EntityManager get() {
						return emf.createEntityManager();
					}
				});

				break;

		}// switch(mode)

	}

	/**
	 * Using warp-persist's <code>EntityManagerFactoryHolder</code> class for
	 * local JPA transactions: Created by IntelliJ IDEA. User: dprasanna Date:
	 * 31/05/2007 Time: 15:26:06 <p/> A placeholder that frees me from having to
	 * use statics to make a singleton EM factory, so I can use per-injector
	 * singletons vs. per JVM/classloader singletons (which doesnt really work for
	 * several reasons).
	 * @author dprasanna
	 * @since 1.0
	 */
	private static final class EntityManagerFactoryHolder {

		private EntityManagerFactory entityManagerFactory;

		// A hack to provide the EntityManager factory statically to non-guice
		// objects
		// (interceptors), that can be thrown away come guice1.1
		protected static volatile EntityManagerFactoryHolder singletonEmFactoryHolder;

		// have to manage the em oursevles--not neat like hibernate =(
		private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<EntityManager>();

		// store singleton
		public EntityManagerFactoryHolder() {
			singletonEmFactoryHolder = this;
		}

		EntityManagerFactory getEntityManagerFactory() {
			return entityManagerFactory;
		}

		synchronized void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
			if(null != this.entityManagerFactory)
				throw new RuntimeException(
						"Duplicate EntityManager factory creation! Only one EntityManager factory is allowed per injector");

			this.entityManagerFactory = entityManagerFactory;
		}

		static EntityManagerFactory getCurrentEntityManagerFactory() {
			return singletonEmFactoryHolder.getEntityManagerFactory();
		}

		static void closeCurrentEntityManager() {
			final EntityManager em = singletonEmFactoryHolder.entityManager.get();

			if(null != em) {
				if(em.isOpen()) em.close();
				singletonEmFactoryHolder.entityManager.set(null);
			}
		}

		static EntityManager getCurrentEntityManager() {
			EntityManager em = singletonEmFactoryHolder.entityManager.get();

			// create if absent
			if(null == em) {
				em = getCurrentEntityManagerFactory().createEntityManager();
				singletonEmFactoryHolder.entityManager.set(em);
			}

			return em;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			final EntityManagerFactoryHolder that = (EntityManagerFactoryHolder) o;

			return (entityManagerFactory == null ? that.entityManagerFactory == null : entityManagerFactory
					.equals(that.entityManagerFactory));

		}

		@Override
		public int hashCode() {
			return (entityManagerFactory != null ? entityManagerFactory.hashCode() : 0);
		}

		public EntityManager getEntityManager() {
			return getCurrentEntityManager();
		}
	}
}
