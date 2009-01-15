package com.tll.dao.impl;

import com.tll.dao.IDao;



/**
 * @author jpk
 */
public interface ISiteStatisticsDao extends IDao {

  int numIsps();
  int numMerchants();
  int numCustomers();
  int numShoppers();
  int numAddresses();
  int numUsers();
  int numOrders();
  int numProducts();
  int numProductCategories();
  int numInterfaces();
}
