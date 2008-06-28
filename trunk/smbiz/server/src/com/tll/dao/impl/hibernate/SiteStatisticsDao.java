package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.dao.hibernate.HibernateJpaSupport;
import com.tll.dao.impl.ISiteStatisticsDao;

/**
 * @author jpk
 */
public class SiteStatisticsDao extends HibernateJpaSupport implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 */
	@Inject
	public SiteStatisticsDao(Provider<EntityManager> emPrvdr) {
		super(emPrvdr);
	}

	public int numIsps() {
		final Query q = getEntityManager().createNamedQuery("numISPs");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numMerchants() {
		final Query q = getEntityManager().createNamedQuery("numMerchants");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numCustomers() {
		final Query q = getEntityManager().createNamedQuery("numCustomers");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numShoppers() {
		final Query q = getEntityManager().createNamedQuery("numShoppers");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numAddresses() {
		final Query q = getEntityManager().createNamedQuery("numAddresses");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numUsers() {
		final Query q = getEntityManager().createNamedQuery("numUsers");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numOrders() {
		final Query q = getEntityManager().createNamedQuery("numOrders");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numProducts() {
		final Query q = getEntityManager().createNamedQuery("numProducts");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numProductCategories() {
		final Query q = getEntityManager().createNamedQuery("numProductCategories");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

	public int numInterfaces() {
		final Query q = getEntityManager().createNamedQuery("numInterfaces");
		final Integer i = (Integer) q.getResultList().get(0);
		return i == null ? 0 : i.intValue();
	}

}
