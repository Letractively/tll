/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.db4o;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;
import com.tll.model.bk.BusinessKeyFactory;


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
	 * @param businessKeyFactory
	 */
	@Inject
	public SiteStatisticsDao(EmbeddedObjectContainer oc, IDb4oNamedQueryTranslator namedQueryTranslator, BusinessKeyFactory businessKeyFactory) {
		super(oc, namedQueryTranslator, businessKeyFactory);
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
