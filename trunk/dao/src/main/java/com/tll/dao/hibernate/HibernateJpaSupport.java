package com.tll.dao.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.google.inject.Provider;
import com.tll.dao.JpaSupport;

/**
 * JpaSupport - JPA support to DAO implementations.
 * @author jpk
 */
public abstract class HibernateJpaSupport extends JpaSupport {

	/**
	 * Constructor
	 * @param emPrvdr
	 */
	protected HibernateJpaSupport(Provider<EntityManager> emPrvdr) {
		super(emPrvdr);
	}

	/**
	 * Obtains the underlying hibernate {@link Session} from a given
	 * {@link EntityManager}.
	 * @param em The entity manager
	 * @return The hibernate session instance bound to the given entity manager.
	 */
	protected final Session getSession(EntityManager em) {
		return (Session) em.getDelegate();
	}

}
