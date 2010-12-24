/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.db4o;

import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;


/**
 * SiteStatisticsDao
 * TODO implement
 * @author jpk
 */
public class SiteStatisticsDao extends Db4oEntityDao implements ISiteStatisticsDao {

	/**
	 * Constructor
	 * @param oc
	 * @param namedQueryTranslator
	 */
	@Inject
	public SiteStatisticsDao(ObjectContainer oc, IDb4oNamedQueryTranslator namedQueryTranslator) {
		super(oc, namedQueryTranslator);
	}

	@Override
	public int numAddresses() {
		return 0;
	}

	@Override
	public int numCustomers() {
		return 0;
	}

	@Override
	public int numInterfaces() {
		return 0;
	}

	@Override
	public int numIsps() {
		return 0;
	}

	@Override
	public int numMerchants() {
		return 0;
	}

	@Override
	public int numOrders() {
		return 0;
	}

	@Override
	public int numProductCategories() {
		return 0;
	}

	@Override
	public int numProducts() {
		return 0;
	}

	@Override
	public int numShoppers() {
		return 0;
	}

	@Override
	public int numUsers() {
		return 0;
	}

}
