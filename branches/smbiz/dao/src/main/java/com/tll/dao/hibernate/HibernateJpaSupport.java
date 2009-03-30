package com.tll.dao.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.google.inject.Provider;

/**
 * JpaSupport - JPA support to DAO implementations.
 * @author jpk
 */
public abstract class HibernateJpaSupport {

	/**
	 * The {@link EntityManager} provider.
	 * <p>
	 * <strong>NOTE: </strong>To ensure thread safety, do <em>not</em> publish
	 * this member.
	 */
	protected final Provider<EntityManager> emPrvdr;

	/**
	 * Constructor
	 * @param emPrvdr
	 */
	public HibernateJpaSupport(Provider<EntityManager> emPrvdr) {
		this.emPrvdr = emPrvdr;
	}

	/**
	 * Obtains a <em><b>new</b></em> {@link EntityManager} instance each time this
	 * method is called!
	 * @return A newly created {@link EntityManager}.
	 */
	protected final EntityManager getEntityManager() {
		return emPrvdr.get();
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
