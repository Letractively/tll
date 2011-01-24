package com.tll.service.entity;

import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;
import com.tll.service.IService;

/**
 * @author jon.kirton
 */
public class SiteStatisticsService implements IService {

	private final ISiteStatisticsDao dao;

	/**
	 * Constructor
	 * @param dao
	 */
	@Inject
	public SiteStatisticsService(ISiteStatisticsDao dao) {
		super();
		this.dao = dao;
	}

	public int numIsps() {
		return dao.numIsps();
	}

	public int numMerchants() {
		return dao.numMerchants();
	}

	public int numCustomers() {
		return dao.numCustomers();
	}

	public int numShoppers() {
		return dao.numShoppers();
	}

	public int numAddresses() {
		return dao.numAddresses();
	}

	public int numUsers() {
		return dao.numUsers();
	}
}
