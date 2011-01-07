/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.db4o;

import java.util.List;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.ISiteStatisticsDao;
import com.tll.model.Address;
import com.tll.model.Customer;
import com.tll.model.Interface;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.Order;
import com.tll.model.ProductCategory;
import com.tll.model.ProductInventory;
import com.tll.model.User;
import com.tll.model.Visitor;
import com.tll.model.bk.BusinessKeyFactory;

/**
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
	public SiteStatisticsDao(EmbeddedObjectContainer oc, IDb4oNamedQueryTranslator namedQueryTranslator,
			BusinessKeyFactory businessKeyFactory) {
		super(oc, namedQueryTranslator, businessKeyFactory);
	}

	@Override
	public int numAddresses() {
		List<Address> clc = loadAllOfType(Address.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numCustomers() {
		List<Customer> clc = loadAllOfType(Customer.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numInterfaces() {
		List<Interface> clc = loadAllOfType(Interface.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numIsps() {
		List<Isp> clc = loadAllOfType(Isp.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numMerchants() {
		List<Merchant> clc = loadAllOfType(Merchant.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numOrders() {
		List<Order> clc = loadAllOfType(Order.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numProductCategories() {
		List<ProductCategory> clc = loadAllOfType(ProductCategory.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numProducts() {
		List<ProductInventory> clc = loadAllOfType(ProductInventory.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numShoppers() {
		List<Visitor> clc = loadAllOfType(Visitor.class, null);
		return clc == null ? 0 : clc.size();
	}

	@Override
	public int numUsers() {
		List<User> clc = loadAllOfType(User.class, null);
		return clc == null ? 0 : clc.size();
	}

}
