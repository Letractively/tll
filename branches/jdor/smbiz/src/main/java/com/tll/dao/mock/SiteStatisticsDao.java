/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.mock;

import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;
import com.tll.model.EntityGraph;


/**
 * SiteStatisticsDao
 * TODO implement
 * @author jpk
 */
public class SiteStatisticsDao extends EntityDao implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param entityGraph
	 */
	@Inject
	public SiteStatisticsDao(EntityGraph entityGraph) {
		super(entityGraph);
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
