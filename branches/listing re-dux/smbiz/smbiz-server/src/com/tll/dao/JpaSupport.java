package com.tll.dao;

import javax.persistence.EntityManager;

import com.google.inject.Provider;

/**
 * JpaSupport - JPA support to DAO implementations.
 * @author jpk
 */
public abstract class JpaSupport {

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
	public JpaSupport(Provider<EntityManager> emPrvdr) {
		this.emPrvdr = emPrvdr;
	}

	/**
	 * @return the current entity manager of the active scoped unit of work
	 */
	protected final EntityManager getEntityManager() {
		return emPrvdr.get();
	}
}
