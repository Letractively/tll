package com.tll.dao.orm;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.ISiteStatisticsDao;

/**
 * @author jpk
 */
public class SiteStatisticsDao extends EntityDao implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 */
	@Inject
	public SiteStatisticsDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler) {
		super(emPrvdr, dbDialectHandler);
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
