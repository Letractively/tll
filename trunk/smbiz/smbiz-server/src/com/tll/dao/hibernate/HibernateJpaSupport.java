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
	 * @return the current hibernate session of the active scoped unit of work
	 */
	protected final Session getSession() {
		return (Session) getEntityManager().getDelegate();
	}

}
