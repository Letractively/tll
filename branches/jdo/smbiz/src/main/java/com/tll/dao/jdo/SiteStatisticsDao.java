package com.tll.dao.jdo;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;
import com.tll.dao.jdo.EntityDao;
import com.tll.db.IDbDialectHandler;

/**
 * @author jpk
 */
public class SiteStatisticsDao extends EntityDao implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param pmf
	 * @param dbDialectHandler
	 */
	@Inject
	public SiteStatisticsDao(PersistenceManagerFactory pmf, IDbDialectHandler dbDialectHandler) {
		super(pmf, dbDialectHandler);
	}

	public int numIsps() {
		final Query q = getPersistenceManager().newQuery("numISPs");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numMerchants() {
		final Query q = getPersistenceManager().newQuery("numMerchants");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numCustomers() {
		final Query q = getPersistenceManager().newQuery("numCustomers");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numShoppers() {
		final Query q = getPersistenceManager().newQuery("numShoppers");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numAddresses() {
		final Query q = getPersistenceManager().newQuery("numAddresses");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numUsers() {
		final Query q = getPersistenceManager().newQuery("numUsers");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numOrders() {
		final Query q = getPersistenceManager().newQuery("numOrders");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numProducts() {
		final Query q = getPersistenceManager().newQuery("numProducts");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numProductCategories() {
		final Query q = getPersistenceManager().newQuery("numProductCategories");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

	public int numInterfaces() {
		final Query q = getPersistenceManager().newQuery("numInterfaces");
		final Integer i = (Integer) q.execute();
		return i == null ? 0 : i.intValue();
	}

}
