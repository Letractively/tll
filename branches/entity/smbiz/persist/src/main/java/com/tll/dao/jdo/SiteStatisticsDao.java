/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.jdo;

import javax.jdo.PersistenceManagerFactory;

import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;


/**
 * SiteStatisticsDao
 * TODO implement
 * @author jpk
 */
public class SiteStatisticsDao extends JdoRdbmsEntityDao implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param pmf
	 */
	@Inject
	public SiteStatisticsDao(PersistenceManagerFactory pmf) {
		super(pmf);
	}

	public int numAddresses() {
		return 0;
	}

	public int numCustomers() {
		return 0;
	}

	public int numInterfaces() {
		return 0;
	}

	public int numIsps() {
		return 0;
	}

	public int numMerchants() {
		return 0;
	}

	public int numOrders() {
		return 0;
	}

	public int numProductCategories() {
		return 0;
	}

	public int numProducts() {
		return 0;
	}

	public int numShoppers() {
		return 0;
	}

	public int numUsers() {
		return 0;
	}

}
